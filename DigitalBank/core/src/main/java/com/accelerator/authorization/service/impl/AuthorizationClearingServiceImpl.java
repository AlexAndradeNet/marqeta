/* BlankFactor (C)2023 */
package com.accelerator.authorization.service.impl;

import com.accelerator.authorization.dto.Authorization;
import com.accelerator.authorization.dto.AuthorizationError;
import com.accelerator.authorization.exception.*;
import com.accelerator.authorization.service.AuthorizationClearingService;
import com.accelerator.common.dto.Account;
import com.accelerator.common.dto.Transaction;
import com.accelerator.common.exception.DuplicateTransactionException;
import com.accelerator.common.exception.TransitAccountException;
import com.accelerator.common.port.AccountPort;
import com.accelerator.common.port.TransactionPort;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationClearingServiceImpl implements AuthorizationClearingService {

    private final Logger logger = LoggerFactory.getLogger(AuthorizationClearingServiceImpl.class);
    private final AccountPort accountPort;
    private final TransactionPort transactionPort;

    @Value("${db.account.transit-account-id}")
    private Long transitAccountId;

    @Override
    // TODO: save invalid transactions
    public void clearAuthorization(Authorization authorization)
            throws InvalidAuthorizationException,
                    TransitAccountException,
                    DuplicateTransactionException {
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
                                        new InvalidClearingException(
                                                AuthorizationError
                                                        .PRECEDING_TRANSACTION_TOKEN_NOT_FOUND
                                                        .getValue()));
        logger.info(
                "Clearing authorization - processing authorization {}",
                authorization.getPrecedingTransactionToken());
        Account account = transaction.getAccount();
        if (!account.getCardId().equals(authorization.getCardToken())) {
            throw new InvalidCardException(authorization);
        }
        BigDecimal reservationBalance = account.getReservationBalance();
        BigDecimal amount = authorization.getAmount();
        int balanceResult = reservationBalance.compareTo(amount);
        if (balanceResult >= 0) {
            updateAccount(account, amount);
            createTransaction(account, authorization);
        } else {
            throw new InsufficientFoundsException(authorization);
        }
    }

    private void updateAccount(Account account, BigDecimal amount) throws TransitAccountException {
        BigDecimal reservationBalance = account.getReservationBalance();

        Account accountForClearing =
                accountPort
                        .getById(transitAccountId)
                        .orElseThrow(
                                () -> new TransitAccountException("Transit account not found"));
        logger.info(
                "Clearing authorization - current balance {} - current reservation {}",
                account.getAccountBalance(),
                account.getReservationBalance());
        logger.info(
                "Clearing authorization - transit balance {} - transit reservation {}",
                accountForClearing.getAccountBalance(),
                accountForClearing.getReservationBalance());
        account.setReservationBalance(reservationBalance.subtract(amount));
        accountForClearing.setAccountBalance(accountForClearing.getAccountBalance().add(amount));
        accountPort.updateAccount(account);
        accountPort.updateAccount(accountForClearing);
        logger.info(
                "Clearing authorization - after clearing balance {} - after clearing reservation"
                        + " {}",
                account.getAccountBalance(),
                account.getReservationBalance());
        logger.info(
                "Clearing authorization - after transit balance {} - after transit reservation {}",
                accountForClearing.getAccountBalance(),
                accountForClearing.getReservationBalance());
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
