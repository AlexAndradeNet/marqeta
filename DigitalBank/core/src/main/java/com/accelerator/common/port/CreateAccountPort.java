/* BlankFactor (C)2023 */
package com.accelerator.common.port;

import com.accelerator.common.dto.Account;
import com.accelerator.common.dto.request.AccountRequest;
import jakarta.validation.ValidationException;

public interface CreateAccountPort {

    Account createAccount(AccountRequest accountRequest) throws ValidationException;
}
