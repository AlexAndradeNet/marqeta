/* BlankFactor (C)2023 */
package com.accelerator.common.adapter;

import com.accelerator.common.dto.Transaction;
import com.accelerator.common.exception.DuplicateTransactionException;
import com.accelerator.common.mapper.TransactionMapper;
import com.accelerator.common.model.TransactionModel;
import com.accelerator.common.port.TransactionPort;
import com.accelerator.common.repository.TransactionRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionPostgreSQLAdapter implements TransactionPort {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public void createTransaction(Transaction transaction) throws DuplicateTransactionException {
        TransactionModel transactionModel = transactionMapper.toModel(transaction);
        if (transactionRepository
                .findByTransactionToken(transaction.getTransactionToken())
                .isPresent()) {
            throw new DuplicateTransactionException(
                    "Transaction token already processed.", transaction.getTransactionToken());
        }
        transactionRepository.save(transactionModel);
    }

    @Override
    public Optional<Transaction> getByTransactionToken(String token) {
        Transaction transaction =
                transactionMapper.toDto(
                        transactionRepository.findByTransactionToken(token).orElse(null));
        return Optional.ofNullable(transaction);
    }

    public Optional<Transaction> getById(long transactionId) {
        return transactionRepository.findById(transactionId).map(transactionMapper::toDto);
    }

    @Override
    public void deleteAll() {
        transactionRepository.deleteAll();
    }
}
