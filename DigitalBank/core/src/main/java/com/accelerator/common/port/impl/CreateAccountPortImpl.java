/* BlankFactor (C)2023 */
package com.accelerator.common.port.impl;

import com.accelerator.common.dto.Account;
import com.accelerator.common.dto.request.AccountRequest;
import com.accelerator.common.mapper.AccountMapper;
import com.accelerator.common.port.CreateAccountPort;
import com.accelerator.common.service.CreateAccountService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateAccountPortImpl implements CreateAccountPort {

    private final AccountMapper accountMapper;
    private final CreateAccountService accountCreateService;

    @Override
    public Account createAccount(AccountRequest accountRequest) throws ValidationException {
        Account account = accountMapper.toDTO(accountRequest);
        return accountCreateService.createAccount(account);
    }
}
