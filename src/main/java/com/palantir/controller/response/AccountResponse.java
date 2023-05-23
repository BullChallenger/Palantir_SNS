package com.palantir.controller.response;

import com.palantir.model.Account;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AccountResponse {

    private Long id;
    private String accountId;

    @Builder
    public AccountResponse(Long id, String accountId) {
        this.id = id;
        this.accountId = accountId;
    }

    public static AccountResponse fromAccount(Account account) {
        return AccountResponse.builder()
                                        .id(account.getId())
                                        .accountId(account.getUsername())
                                    .build();
    }
}
