package com.palantir.service;

import com.palantir.exception.ErrorCode;
import com.palantir.exception.PalantirException;
import com.palantir.model.Article;
import com.palantir.model.entity.AccountEntity;
import com.palantir.model.entity.ArticleEntity;
import com.palantir.repository.AccountEntityRepository;
import com.palantir.repository.ArticleEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleEntityRepository articleEntityRepository;
    private final AccountEntityRepository accountEntityRepository;

    @Transactional
    public void create(String title, String content, String accountId) {
        AccountEntity theAccount = validAccount(accountId);
        articleEntityRepository.save(ArticleEntity.of(title, content, theAccount));
    }

    @Transactional
    public Article modify(String title, String content, String accountId, Long articleId) {
        AccountEntity theAccount = validAccount(accountId);
        ArticleEntity theArticle = validArticle(articleId);

        checkPermission(accountId, articleId, theAccount, theArticle);

        theArticle.updateTitle(title);
        theArticle.updateContent(content);

        return Article.fromEntity(articleEntityRepository.saveAndFlush(theArticle));
    }

    @Transactional
    public void delete(String accountId, Long articleId) {
        AccountEntity theAccount = validAccount(accountId);
        ArticleEntity theArticle = validArticle(articleId);
        checkPermission(accountId, articleId, theAccount, theArticle);

        articleEntityRepository.delete(theArticle);
    }

    private void checkPermission(String accountId, Long articleId, AccountEntity theAccount, ArticleEntity theArticle) {
        if (theArticle.getWriter() != theAccount) {
            throw new PalantirException(ErrorCode.INVALID_PERMISSION,
                    String.format("%s has no permission with %s", accountId, articleId));
        }
    }

    private ArticleEntity validArticle(Long articleId) {
        return articleEntityRepository.findById(articleId).orElseThrow(() ->
                new PalantirException(ErrorCode.ARTICLE_NOT_FOUND, String.format("%s not found", articleId))
        );
    }

    private AccountEntity validAccount(String accountId) {
        return accountEntityRepository.findByAccountId(accountId).orElseThrow(
                () -> new PalantirException(ErrorCode.ACCOUNT_NOT_FOUND, String.format("%s not found", accountId))
        );
    }
}
