/* BlankFactor (C)2023 */
package com.accelerator.common.mapper.impl;

import com.accelerator.common.dto.Transaction;
import com.accelerator.common.mapper.AccountMapper;
import com.accelerator.common.mapper.TransactionMapper;
import com.accelerator.common.model.TransactionModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionMapperImpl implements TransactionMapper {

    private final AccountMapper accountMapper;

    @Override
    public Transaction toDto(TransactionModel transactionModel) {
        if (transactionModel == null) {
            return null;
        }

        return Transaction.builder()
                .transactionId(transactionModel.getTransactionId())
                .transactionToken(transactionModel.getTransactionToken())
                .transactionType(transactionModel.getTransactionType())
                .account(accountMapper.toDTO(transactionModel.getAccount()))
                .amount(transactionModel.getAmount())
                .transactionStatus(transactionModel.getTransactionStatus())
                .precedingTransactionToken(transactionModel.getPrecedingTransactionToken())
                .transactionDate(transactionModel.getTransactionDate())
                .payload(transactionModel.getPayload())
                .build();
    }

    @Override
    public TransactionModel toModel(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        return TransactionModel.builder()
                .transactionId(transaction.getTransactionId())
                .transactionToken(transaction.getTransactionToken())
                .transactionType(transaction.getTransactionType())
                .account(accountMapper.toModel(transaction.getAccount()))
                .amount(transaction.getAmount())
                .transactionStatus(transaction.getTransactionStatus())
                .precedingTransactionToken(transaction.getPrecedingTransactionToken())
                .transactionDate(transaction.getTransactionDate())
                .payload(transaction.getPayload())
                .build();
    }
}
