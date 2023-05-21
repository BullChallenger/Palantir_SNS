package com.palantir.fixture;

import com.palantir.model.entity.AccountEntity;

public class AccountEntityFixture {

    public static AccountEntity get(Long id, String accountId, String password) {
        return  AccountEntity.builder()
                                .id(id)
                                .accountId(accountId)
                                .password(password)
                            .build();
    }
}
