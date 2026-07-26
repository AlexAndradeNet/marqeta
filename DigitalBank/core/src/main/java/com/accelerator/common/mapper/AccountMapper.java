/* BlankFactor (C)2023 */
package com.accelerator.common.mapper;

import com.accelerator.common.dto.Account;
import com.accelerator.common.dto.request.AccountRequest;
import com.accelerator.common.model.AccountModel;

public interface AccountMapper {

    Account toDTO(AccountModel accountModel);

    AccountModel toModel(Account account);

    Account toDTO(AccountRequest accountRequest);
}
