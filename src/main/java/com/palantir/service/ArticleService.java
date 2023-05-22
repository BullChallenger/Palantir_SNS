package com.palantir.service;

import com.palantir.controller.request.CommentPostRequest;
import com.palantir.exception.ErrorCode;
import com.palantir.exception.PalantirException;
import com.palantir.model.AlarmArgs;
import com.palantir.model.AlarmType;
import com.palantir.model.Article;
import com.palantir.model.Comment;
import com.palantir.model.entity.*;
import com.palantir.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleEntityRepository articleEntityRepository;
    private final AccountEntityRepository accountEntityRepository;
    private final LikeEntityRepository likeEntityRepository;
    private final CommentEntityRepository commentEntityRepository;
    private final AlarmEntityRepository alarmEntityRepository;

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

    public Page<Article> list(Pageable pageable) {
        return articleEntityRepository.findAll(pageable).map(Article::fromEntity);
    }

    public Page<Article> myList(String accountId, Pageable pageable) {
        AccountEntity theAccount = validAccount(accountId);
        return articleEntityRepository.findAllByWriter(theAccount, pageable).map(Article::fromEntity);
    }

    @Transactional
    public void like(Long articleId, String accountId) {
        AccountEntity theAccount = validAccount(accountId);
        ArticleEntity theArticle = validArticle(articleId);

        likeEntityRepository.findByAccountAndArticle(theAccount, theArticle).ifPresent( like -> {
            throw new PalantirException(ErrorCode.ALREADY_LIKED, String.format("%s already liked article %d", accountId, articleId));
        });

        likeEntityRepository.save(LikeEntity.of(theAccount, theArticle));

        alarmEntityRepository.save(AlarmEntity.of(theArticle.getWriter(),
                                                    AlarmType.NEW_LIKE_ON_ARTICLE,
                                                    new AlarmArgs(theAccount.getId(), theArticle.getId())
        ));
    }

    public int likeCount(Long   articleId) {
        ArticleEntity theArticle = validArticle(articleId);
        return likeEntityRepository.countByArticle(theArticle);
    }

    @Transactional
    public void comment(Long articleId, String accountId, String content) {
        AccountEntity theAccount = validAccount(accountId);
        ArticleEntity theArticle = validArticle(articleId);

        commentEntityRepository.save(CommentEntity.of(theAccount, theArticle, content));

        alarmEntityRepository.save(AlarmEntity.of(theArticle.getWriter(),
                                                    AlarmType.NEW_COMMENT_ON_ARTICLE,
                                                    new AlarmArgs(theAccount.getId(), theArticle.getId())
        ));
    }

    public Page<Comment> getComment(Long articleId, Pageable pageable) {
        ArticleEntity theArticle = validArticle(articleId);
        return commentEntityRepository.findAllByArticle(theArticle, pageable).map(Comment::fromEntity);
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
