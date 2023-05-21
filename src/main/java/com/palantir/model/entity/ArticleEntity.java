package com.palantir.model.entity;

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
@Table(name = "article")
@Getter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE article SET deleted_at = NOW() WHERE id=?")
@Where(clause = "deleted_at IS NULL")
public class ArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountEntity writer;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @Builder
    public ArticleEntity(Long id, String title, String content, AccountEntity writer) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
    }

    @PrePersist
    void createdAt() {
        this.createdAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public static ArticleEntity of(String title, String content, AccountEntity writer) {
        ArticleEntity theArticleEntity = ArticleEntity.builder()
                .title(title)
                .content(content)
                .writer(writer)
                .build();
        return theArticleEntity;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
