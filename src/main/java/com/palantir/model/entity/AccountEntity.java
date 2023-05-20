package com.palantir.model.entity;

import com.palantir.model.AccountRole;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "account")
@Getter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE account SET deleted_at = NOW() WHERE id=?")
@Where(clause = "deleted_at IS NULL")
public class AccountEntity {

    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private AccountRole role = AccountRole.USER;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @Builder
    public AccountEntity(String accountId, String password) {
        this.accountId = accountId;
        this.password = password;
    }

    @PrePersist
    void createdAt() {
        this.createdAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public static AccountEntity of(String accountId, String password) {
        AccountEntity theAccountEntity = AccountEntity.builder()
                                                        .accountId(accountId)
                                                        .password(password)
                                                        .build();
        return theAccountEntity;
    }
}
