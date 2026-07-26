/* BlankFactor (C)2023 */
package com.accelerator.authorization.service.impl;

import com.accelerator.authorization.dto.Authorization;
import com.accelerator.authorization.dto.AuthorizationError;
import com.accelerator.authorization.exception.InvalidAuthorizationException;
import com.accelerator.authorization.exception.InvalidCardException;
import com.accelerator.authorization.exception.InvalidReversalException;
import com.accelerator.authorization.service.AuthorizationReversalService;
import com.accelerator.common.dto.Account;
import com.accelerator.common.dto.Transaction;
import com.accelerator.common.exception.DuplicateTransactionException;
import com.accelerator.common.port.AccountPort;
import com.accelerator.common.port.TransactionPort;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationReversalServiceImpl implements AuthorizationReversalService {
    private final Logger logger = LoggerFactory.getLogger(AuthorizationReversalServiceImpl.class);
    private final AccountPort accountPort;
    private final TransactionPort transactionPort;

    @Override
    public void reverseAuthorization(Authorization authorization)
            throws InvalidAuthorizationException, DuplicateTransactionException {
        Optional<Transaction> transactionResult =
                transactionPort.getByTransactionToken(authorization.getTransactionToken());
        if (transactionResult.isPresent()) {
            return;
        }
        Transaction transaction =
                transactionPort
                        .getByTransactionToken(authorization.getPrecedingTransactionToken())
                        .orElseThrow(
                                () ->
                                        new InvalidReversalException(
                                                AuthorizationError
                                                        .PRECEDING_TRANSACTION_TOKEN_NOT_FOUND
                                                        .getValue()));
        logger.info(
                "Reversal authorization - processing authorization {}",
                authorization.getPrecedingTransactionToken());
        Account account = transaction.getAccount();
        if (!account.getCardId().equals(authorization.getCardToken())) {
            throw new InvalidCardException(authorization);
        }
        BigDecimal reservationBalance = account.getReservationBalance();
        BigDecimal amount = authorization.getAmount();
        int balanceResult = reservationBalance.compareTo(amount);
        if (balanceResult == 0) {
            updateAmountsInTheAccount(account, amount);
            createTransaction(account, authorization);
        } else {
            throw new InvalidReversalException(AuthorizationError.INSUFFICIENT_FUNDS.name());
        }
    }

    private void updateAmountsInTheAccount(Account account, BigDecimal amount) {
        BigDecimal reservationBalance = account.getReservationBalance();
        BigDecimal accountBalance = account.getAccountBalance();

        logger.info(
                "Reversal authorization - current balance {} - current reservation {}",
                reservationBalance,
                accountBalance);
        account.setReservationBalance(reservationBalance.subtract(amount));
        account.setAccountBalance(accountBalance.add(amount));
        accountPort.updateAccount(account);
        logger.info(
                "Reversal authorization - after reversal balance {} - after clearing reservation"
                        + " {}",
                account.getAccountBalance(),
                account.getReservationBalance());
    }

    private void createTransaction(Account account, Authorization authorization)
            throws DuplicateTransactionException {
        Transaction transaction =
                new Transaction(
                        authorization.getTransactionToken(),
                        authorization.getType().getValue(),
                        account,
                        authorization.getAmount(),
                        authorization.getStatus().getValue(),
                        authorization.getPrecedingTransactionToken(),
                        authorization.getPayload());
        transactionPort.createTransaction(transaction);
    }
}
