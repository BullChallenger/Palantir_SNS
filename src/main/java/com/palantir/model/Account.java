package com.palantir.model;

import com.palantir.model.entity.AccountEntity;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;

// TODO: implement
@Getter
@Builder
public class Account implements UserDetails {

    private Long id;
    private String accountId;
    private String password;
    private AccountRole accountRole;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static Account fromEntity(AccountEntity entity) {
        return Account.builder()
                        .id(entity.getId())
                        .accountId(entity.getAccountId())
                        .password(entity.getPassword())
                        .accountRole(entity.getRole())
                        .createdAt(entity.getCreatedAt())
                        .updatedAt(entity.getUpdatedAt())
                        .deletedAt(entity.getDeletedAt())
                    .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority(this.getAccountRole().toString()));
    }

    @Override
    public String getUsername() {
        return this.accountId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.deletedAt == null;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.deletedAt == null;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.deletedAt == null;
    }

    @Override
    public boolean isEnabled() {
        return this.deletedAt == null;
    }
}
