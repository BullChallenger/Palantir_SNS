package com.palantir.model;

import com.palantir.model.entity.AccountEntity;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

// TODO: implement
@Getter
@Builder
public class Account {

    private String accountId;
    private String password;
    private AccountRole accountRole;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static Account fromEntity(AccountEntity entity) {
        return Account.builder()
                        .accountId(entity.getAccountId())
                        .password(entity.getPassword())
                        .accountRole(entity.getRole())
                        .createdAt(entity.getCreatedAt())
                        .updatedAt(entity.getUpdatedAt())
                        .deletedAt(entity.getDeletedAt())
                            .build();
    }
}
