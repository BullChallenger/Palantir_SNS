package com.palantir.controller.response;

import com.palantir.model.Account;
import com.palantir.model.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
public class ArticleResponse {

    private Long articleId;

    private String title;

    private String content;

    private AccountResponse writer;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;

    public static ArticleResponse fromArticle(Article article) {
        return ArticleResponse.builder()
                                .articleId(article.getArticleId())
                                .title(article.getTitle())
                                .content(article.getContent())
                                .writer(AccountResponse.fromAccount(article.getWriter()))
                                .createdAt(article.getCreatedAt())
                                .updatedAt(article.getUpdatedAt())
                                .deletedAt(article.getDeletedAt())
                            .build();
    }
}
