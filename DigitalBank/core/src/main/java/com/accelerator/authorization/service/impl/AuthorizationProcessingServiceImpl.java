/* BlankFactor (C)2023 */
package com.accelerator.authorization.service.impl;

import static com.accelerator.authorization.dto.AuthorizationStatus.DECLINED;

import com.accelerator.authorization.dto.Authorization;
import com.accelerator.authorization.exception.InsufficientFoundsException;
import com.accelerator.authorization.exception.InvalidAuthorizationException;
import com.accelerator.authorization.exception.InvalidCardException;
import com.accelerator.authorization.service.AuthorizationLedgerService;
import com.accelerator.authorization.service.AuthorizationProcessingService;
import com.accelerator.common.dto.Account;
import com.accelerator.common.dto.Transaction;
import com.accelerator.common.exception.DuplicateTransactionException;
import com.accelerator.common.exception.TransitAccountException;
import com.accelerator.common.port.AccountPort;
import com.accelerator.common.port.TransactionPort;
import com.accelerator.ledger.exception.LedgerAPIException;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthorizationProcessingServiceImpl implements AuthorizationProcessingService {

    private final AccountPort accountPort;
    private final TransactionPort transactionRepoPort;
    private final AuthorizationLedgerService authorizationLedgerService;

    @Override
    @Transactional
    public void processAuthorization(Authorization authorization)
            throws InvalidAuthorizationException,
                    TransitAccountException,
                    LedgerAPIException,
                    DuplicateTransactionException {

        Account account =
                accountPort
                        .getByCardId(authorization.getCardToken())
                        .orElseThrow(() -> new InvalidCardException(authorization));
        BigDecimal accountBalance = account.getAccountBalance();
        BigDecimal amount = authorization.getAmount();
        int balanceResult = accountBalance.compareTo(amount);
        if (balanceResult >= 0) {
            account.setAccountBalance(accountBalance.subtract(amount));
            BigDecimal reservationBalance = account.getReservationBalance();
            account.setReservationBalance(reservationBalance.add(amount));
            // TODO: rollback if any exception
            authorizationLedgerService.processReservation(authorization, account);
            accountPort.updateAccount(account);
            Transaction transaction = createTransaction(account, authorization);
            transactionRepoPort.createTransaction(transaction);
        } else {
            // SONAR: This line is covered, but JaCoCo doesn't recognize it as such because it can
            // mark methods that always throw an exception. More details can be found at:
            // https://stackoverflow.com/questions/54032393/eclemmajacocco-shows-line-not-covered-even-if-the-line-is-executed
            // We need a method to prevent the transaction from being rolled back.
            createDeclinedTransaction(account, authorization);
        }
    }

    @Transactional(noRollbackFor = InvalidAuthorizationException.class)
    public void createDeclinedTransaction(Account account, Authorization authorization)
            throws InvalidAuthorizationException, DuplicateTransactionException {
        Transaction transaction = createTransaction(account, authorization);
        transaction.setTransactionStatus(DECLINED.getValue());
        transactionRepoPort.createTransaction(transaction);
        throw new InsufficientFoundsException(authorization);
    }

    private Transaction createTransaction(Account account, Authorization authorization) {
        return new Transaction(
                authorization.getTransactionToken(),
                authorization.getType().getValue(),
                account,
                authorization.getAmount(),
                authorization.getStatus().getValue(),
                null,
                authorization.getPayload());
    }
}
