/* BlankFactor (C)2023 */
package com.accelerator.common.service.impl;

import com.accelerator.common.dto.Account;
import com.accelerator.common.port.AccountPort;
import com.accelerator.common.service.CreateAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateAccountServiceImpl implements CreateAccountService {

    private final AccountPort accountPort;

    @Override
    public Account createAccount(Account account) {
        return accountPort.save(account);
    }
}
