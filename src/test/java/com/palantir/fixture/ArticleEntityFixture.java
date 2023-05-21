package com.palantir.fixture;

import com.palantir.model.entity.AccountEntity;
import com.palantir.model.entity.ArticleEntity;

public class ArticleEntityFixture {

    public static ArticleEntity get(String accountId, Long articleId, Long id) {
        AccountEntity theAccount = AccountEntity.builder()
                                                    .id(id)
                                                    .accountId(accountId)
                                                .build();

        return ArticleEntity.builder()
                                .id(articleId)
                                .writer(theAccount)
                            .build();
    }
}
