/* BlankFactor (C)2023 */
package com.accelerator.common.repository;

import com.accelerator.common.model.AccountModel;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<AccountModel, Long> {

    Optional<AccountModel> findByCardId(String cardId);
}
