package com.palantir.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "likes")
@Getter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE likes SET deleted_at = NOW() WHERE id=?")
@Where(clause = "deleted_at IS NULL")
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private ArticleEntity article;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @Builder
    public LikeEntity(AccountEntity account, ArticleEntity article) {
        this.account = account;
        this.article = article;
    }

    @PrePersist
    void createdAt() {
        this.createdAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public static LikeEntity of(AccountEntity account, ArticleEntity article) {
        LikeEntity theLikeEntity = LikeEntity.builder()
                                                .account(account)
                                                .article(article)
                                             .build();
        return theLikeEntity;
    }
}
