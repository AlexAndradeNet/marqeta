/* BlankFactor (C)2023 */
package com.accelerator.common.service;

import com.accelerator.common.dto.Account;
import jakarta.validation.ValidationException;

public interface CreateAccountService {
    Account createAccount(Account account) throws ValidationException;
}
