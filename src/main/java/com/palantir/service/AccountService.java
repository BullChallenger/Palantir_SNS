package com.palantir.service;

import com.palantir.exception.ErrorCode;
import com.palantir.exception.PalantirException;
import com.palantir.model.Account;
import com.palantir.model.entity.AccountEntity;
import com.palantir.repository.AccountEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountEntityRepository accountEntityRepository;

    // TODO: implement
    public Account signUp(String accountId, String password) {
        // Check Duplicated Account
        accountEntityRepository.findByAccountId(accountId).ifPresent( accountEntity -> {
            throw new PalantirException(ErrorCode.DUPLICATED_ACCOUNT_ID, String.format("%s is duplicated", accountId));
        });

        // Registration
        AccountEntity theAccountEntity = accountEntityRepository.save(AccountEntity.of(accountId, password));

        return Account.fromEntity(theAccountEntity);
    }

    // TODO: implement
    public String login(String accountId, String password) {
        // Check SignUp Account
        AccountEntity theAccountEntity = accountEntityRepository.findByAccountId(accountId).orElseThrow(
                () -> new PalantirException(ErrorCode.DUPLICATED_ACCOUNT_ID, "")
        );

        // Check Password
        if (!theAccountEntity.getPassword().equals(password)) {
            throw new PalantirException(ErrorCode.DUPLICATED_ACCOUNT_ID, "");
        }

        // Create Token
        return "";
    }
}
