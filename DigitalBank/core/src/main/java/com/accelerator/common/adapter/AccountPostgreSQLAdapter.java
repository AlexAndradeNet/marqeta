/* BlankFactor (C)2023 */
package com.accelerator.common.adapter;

import com.accelerator.common.dto.Account;
import com.accelerator.common.mapper.AccountMapper;
import com.accelerator.common.model.AccountModel;
import com.accelerator.common.port.AccountPort;
import com.accelerator.common.repository.AccountRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountPostgreSQLAdapter implements AccountPort {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    public Optional<Account> getById(Long accountId) {
        Account account = accountMapper.toDTO(accountRepository.findById(accountId).orElse(null));
        return Optional.ofNullable(account);
    }

    @Override
    public Optional<Account> getByCardId(String cardId) {
        Account account = accountMapper.toDTO(accountRepository.findByCardId(cardId).orElse(null));
        return Optional.ofNullable(account);
    }

    @Override
    public void updateAccount(Account account) {
        save(account);
    }

    @Override
    public Account save(Account account) {
        AccountModel accountModel = accountMapper.toModel(account);
        accountModel = accountRepository.save(accountModel);
        return accountMapper.toDTO(accountModel);
    }

    @Override
    public void deleteAll() {
        accountRepository.deleteAll();
    }
}
