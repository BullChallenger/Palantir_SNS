package com.palantir.fixture;

import com.palantir.model.entity.AccountEntity;

public class AccountEntityFixture {

    public static AccountEntity get(String accountId, String password) {
        AccountEntity result = new AccountEntity(accountId, password);
        result.setId(1L);

        return  result;
    }
}
