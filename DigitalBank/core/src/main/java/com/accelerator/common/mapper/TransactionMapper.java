/* BlankFactor (C)2023 */
package com.accelerator.common.mapper;

import com.accelerator.common.dto.Transaction;
import com.accelerator.common.model.TransactionModel;

public interface TransactionMapper {

    Transaction toDto(TransactionModel transactionModel);

    TransactionModel toModel(Transaction transaction);
}
