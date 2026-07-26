/* BlankFactor (C)2023 */
package com.accelerator.common.port;

import com.accelerator.common.dto.Account;
import java.util.Optional;

public interface AccountPort {

    Optional<Account> getById(Long accountId);

    Optional<Account> getByCardId(String cardId);

    void updateAccount(Account account);

    Account save(Account account);

    void deleteAll();
}
