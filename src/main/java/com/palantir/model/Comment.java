package com.palantir.model;

import com.palantir.model.entity.ArticleEntity;
import com.palantir.model.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
public class Comment {

    private Long commentId;

    private String content;

    private String writerName;

    private Long articleId;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;

    public static Comment fromEntity(CommentEntity entity) {
        return Comment.builder()
                .commentId(entity.getId())
                .content(entity.getContent())
                .writerName(entity.getAccount().getAccountId())
                .articleId(entity.getArticle().getId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }

}
