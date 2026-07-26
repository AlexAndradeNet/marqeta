/* BlankFactor (C)2023 */
package com.accelerator.common.port;

import com.accelerator.common.dto.Transaction;
import com.accelerator.common.exception.DuplicateTransactionException;
import java.util.Optional;

public interface TransactionPort {

    void createTransaction(Transaction transaction) throws DuplicateTransactionException;

    Optional<Transaction> getByTransactionToken(String token);

    Optional<Transaction> getById(long transactionId);

    void deleteAll();
}
