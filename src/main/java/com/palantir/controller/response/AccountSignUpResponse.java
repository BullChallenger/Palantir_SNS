package com.palantir.controller.response;

import com.palantir.model.Account;
import com.palantir.model.AccountRole;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AccountSignUpResponse {

    private String accountId;
    private AccountRole accountRole;

    @Builder
    public AccountSignUpResponse(String accountId, AccountRole accountRole) {
        this.accountId = accountId;
        this.accountRole = accountRole;
    }

    public static AccountSignUpResponse fromAccount(Account account) {
        return AccountSignUpResponse.builder()
                                        .accountId(account.getAccountId())
                                        .accountRole(account.getAccountRole())
                                    .build();
    }
}
