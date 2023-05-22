package com.palantir.service;

import com.palantir.exception.ErrorCode;
import com.palantir.exception.PalantirException;
import com.palantir.fixture.AccountEntityFixture;
import com.palantir.fixture.ArticleEntityFixture;
import com.palantir.model.entity.AccountEntity;
import com.palantir.model.entity.ArticleEntity;
import com.palantir.repository.AccountEntityRepository;
import com.palantir.repository.ArticleEntityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @MockBean
    private ArticleEntityRepository articleEntityRepository;

    @MockBean
    private AccountEntityRepository accountEntityRepository;

    @Test
    public void articleCreateTest() {
        String title = "title";
        String content = "content";
        String writer = "writer";

        when(accountEntityRepository.findByAccountId(writer)).thenReturn(Optional.of(mock(AccountEntity.class)));
        when(articleEntityRepository.save(any())).thenReturn(mock(ArticleEntity.class));

        assertDoesNotThrow(() -> articleService.create(title, content, writer));
    }

    @Test
    public void articleCreateFailedCauseNotFoundAccountTest() {
        String title = "title";
        String content = "content";
        String writer = "writer";

        when(accountEntityRepository.findByAccountId(writer)).thenReturn(Optional.empty());
        when(articleEntityRepository.save(any())).thenReturn(mock(ArticleEntity.class));

        PalantirException e = assertThrows(PalantirException.class, () -> articleService.create(title, content, writer));
        assertThat(ErrorCode.ACCOUNT_NOT_FOUND).isEqualTo(e.getErrorCode());
    }

    @Test
    public void articleModifyTest() {
        String title = "title";
        String content = "content";
        String writer = "writer";
        Long articleId = 1L;

        ArticleEntity theArticle = ArticleEntityFixture.get(writer, articleId, 1L);
        AccountEntity theAccount = theArticle.getWriter();

        when(accountEntityRepository.findByAccountId(writer)).thenReturn(Optional.of(theAccount));
        when(articleEntityRepository.findById(articleId)).thenReturn(Optional.of(theArticle));
        when(articleEntityRepository.saveAndFlush(any())).thenReturn(theArticle);

        assertDoesNotThrow(() -> articleService.create(title, content, writer));
    }

    @Test
    public void articleModifyFailedCuzNotFoundArticleTest() {
        String title = "title";
        String content = "content";
        String writer = "writer";
        Long articleId = 1L;

        ArticleEntity theArticle = ArticleEntityFixture.get(writer, articleId, 1L);
        AccountEntity theAccount = theArticle.getWriter();

        when(accountEntityRepository.findByAccountId(writer)).thenReturn(Optional.of(theAccount));
        when(articleEntityRepository.findById(articleId)).thenReturn(Optional.empty());

        PalantirException e = assertThrows(PalantirException.class, () -> articleService.modify(title, content,
                writer, 2L));
        assertEquals(ErrorCode.ARTICLE_NOT_FOUND, e.getErrorCode());
    }

    @Test
    public void articleModifyFailedCuzUnAuthorizedTest() {
        String title = "title";
        String content = "content";
        String writer = "writer";
        Long articleId = 1L;

        ArticleEntity theArticle = ArticleEntityFixture.get(writer, articleId, 1L);
        AccountEntity nonWriter = AccountEntityFixture.get(2L, "wrong" + writer, "password");

        when(accountEntityRepository.findByAccountId(writer)).thenReturn(Optional.of(nonWriter));
        when(articleEntityRepository.findById(articleId)).thenReturn(Optional.of(theArticle));

        PalantirException e = assertThrows(PalantirException.class, () -> articleService.modify(title, content, writer, 1L));
        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    public void articleDeleteTest() {
        String writer = "writer";
        Long articleId = 1L;

        ArticleEntity theArticle = ArticleEntityFixture.get(writer, articleId, 1L);
        AccountEntity theAccount = theArticle.getWriter();

        when(accountEntityRepository.findByAccountId(writer)).thenReturn(Optional.of(theAccount));
        when(articleEntityRepository.findById(articleId)).thenReturn(Optional.of(theArticle));

        assertDoesNotThrow(() -> articleService.delete(writer, articleId));
    }

    @Test
    public void articleDeleteFailedCuzNotFoundArticleTest() {
        String writer = "writer";
        Long articleId = 1L;

        ArticleEntity theArticle = ArticleEntityFixture.get(writer, articleId, 1L);
        AccountEntity theAccount = theArticle.getWriter();

        when(accountEntityRepository.findByAccountId(writer)).thenReturn(Optional.of(theAccount));
        when(articleEntityRepository.findById(articleId)).thenReturn(Optional.empty());

        PalantirException e = assertThrows(PalantirException.class, () -> articleService.delete(writer, articleId));
        assertEquals(ErrorCode.ARTICLE_NOT_FOUND, e.getErrorCode());
    }

    @Test
    public void articleDeleteFailedCuzUnAuthorizedTest() {
        String writer = "writer";
        Long articleId = 1L;

        ArticleEntity theArticle = ArticleEntityFixture.get(writer, articleId, 1L);
        AccountEntity nonWriter = AccountEntityFixture.get(2L, "wrong" + writer, "password");

        when(accountEntityRepository.findByAccountId(writer)).thenReturn(Optional.of(nonWriter));
        when(articleEntityRepository.findById(articleId)).thenReturn(Optional.of(theArticle));

        PalantirException e = assertThrows(PalantirException.class, () -> articleService.delete(writer, articleId));
        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
    }

    @Test
    public void getFeedListTest() {
        Pageable pageable = mock(Pageable.class);
        when(articleEntityRepository.findAll(pageable)).thenReturn(Page.empty());

        assertDoesNotThrow(() -> articleService.list(pageable));
    }

    @Test
    public void getMyFeedListTest() {
        Pageable pageable = mock(Pageable.class);
        AccountEntity theAccount = mock(AccountEntity.class);

        when(accountEntityRepository.findByAccountId(any())).thenReturn(Optional.of(theAccount));
        when(articleEntityRepository.findAllByWriter(theAccount, pageable)).thenReturn(Page.empty());

        assertDoesNotThrow(() -> articleService.myList(theAccount.getAccountId(), pageable));
    }
}
