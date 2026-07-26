/* BlankFactor (C)2023 */
package com.accelerator.common.mapper.impl;

import com.accelerator.common.dto.Account;
import com.accelerator.common.dto.request.AccountRequest;
import com.accelerator.common.mapper.AccountMapper;
import com.accelerator.common.model.AccountModel;
import jakarta.validation.ValidationException;
import java.security.SecureRandom;
import org.springframework.stereotype.Component;

@Component
public class AccountMapperImpl implements AccountMapper {

    private final SecureRandom random = new SecureRandom();

    @Override
    public Account toDTO(AccountModel accountModel) {
        if (accountModel == null) {
            return null;
        }
        return Account.builder()
                .accountId(accountModel.getAccountId())
                .userId(accountModel.getUserId())
                .cardId(accountModel.getCardId())
                .accountBalance(accountModel.getAccountBalance())
                .reservationBalance(accountModel.getReservationBalance())
                .build();
    }

    @Override
    public AccountModel toModel(Account account) {
        if (account == null) {
            return null;
        }

        return AccountModel.builder()
                .accountId(account.getAccountId())
                .userId(account.getUserId())
                .cardId(account.getCardId())
                .accountBalance(account.getAccountBalance())
                .reservationBalance(account.getReservationBalance())
                .build();
    }

    @Override
    public Account toDTO(AccountRequest accountRequest) {
        if (!isGuid(accountRequest.getCardId())) {
            throw new ValidationException("cardId must be a valid GUID");
        }

        return Account.builder()
                .accountId(null)
                .userId(String.valueOf(random.nextLong(1L, Long.MAX_VALUE)))
                .cardId(accountRequest.getCardId())
                .accountBalance(accountRequest.getAccountBalance())
                .reservationBalance(accountRequest.getReservationBalance())
                .build();
    }

    private boolean isGuid(String guid) {
        return guid.matches("[0-9a-f]{8}-([0-9a-f]{4}-){3}[0-9a-f]{12}");
    }
}
