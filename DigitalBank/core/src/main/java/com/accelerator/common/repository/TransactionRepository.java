/* BlankFactor (C)2023 */
package com.accelerator.common.repository;

import com.accelerator.common.model.TransactionModel;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<TransactionModel, Long> {

    Optional<TransactionModel> findByTransactionToken(String transactionToken);
}
