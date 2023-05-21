package com.palantir.model;

import com.palantir.model.entity.AccountEntity;
import com.palantir.model.entity.ArticleEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
public class Article {

    private Long articleId;

    private String title;

    private String content;

    private Account writer;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;

    public static Article fromEntity(ArticleEntity entity) {
        return Article.builder()
                .articleId(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(Account.fromEntity(entity.getWriter()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }

}
