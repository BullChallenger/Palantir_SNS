package com.palantir.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palantir.controller.request.ArticleCreateRequest;
import com.palantir.controller.request.ArticleModifyRequest;
import com.palantir.exception.ErrorCode;
import com.palantir.exception.PalantirException;
import com.palantir.fixture.ArticleEntityFixture;
import com.palantir.model.Article;
import com.palantir.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ArticleService articleService;

    @Test
    @WithMockUser
    public void createArticleTest() throws Exception {
        String title = "title";
        String content = "content";

        mockMvc.perform(post("/api/v1/article")
                        .contentType(MediaType.APPLICATION_JSON)
                        // TODO: add request body
                        .content(objectMapper.writeValueAsBytes(new ArticleCreateRequest(
                                title, content
                        )))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void createArticleFailedCauseUnAuthorizedTest() throws Exception {
        String title = "title";
        String content = "content";

        mockMvc.perform(post("/api/v1/article")
                        .contentType(MediaType.APPLICATION_JSON)
                        // TODO: add request body
                        .content(objectMapper.writeValueAsBytes(new ArticleCreateRequest(
                                title, content
                        )))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void modifyArticleTest() throws Exception {
        String title = "title";
        String content = "content";

        when(articleService.modify(eq(title), eq(content), any(), any()))
                .thenReturn(Article.fromEntity(ArticleEntityFixture.get("writer", 1L, 1L)));

        mockMvc.perform(put("/api/v1/article/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        // TODO: add request body
                        .content(objectMapper.writeValueAsBytes(new ArticleModifyRequest(
                                title, content
                        )))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void modifyArticleFailedCuzUnAuthorizedTest() throws Exception {
        String title = "title";
        String content = "content";

        mockMvc.perform(put("/api/v1/article/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        // TODO: add request body
                        .content(objectMapper.writeValueAsBytes(new ArticleModifyRequest(
                                title, content
                        )))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void modifyArticleFailedCuzNotWriterTest() throws Exception {
        String title = "title";
        String content = "content";

        doThrow(new PalantirException(ErrorCode.INVALID_PERMISSION))
                .when(articleService).modify(eq(title), eq(content), any(), eq(1L));

        mockMvc.perform(put("/api/v1/article/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        // TODO: add request body
                        .content(objectMapper.writeValueAsBytes(new ArticleModifyRequest(
                                title, content
                        )))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void modifyArticleFailedCuzNotFoundArticleTest() throws Exception {
        String title = "title";
        String content = "content";

        doThrow(new PalantirException(ErrorCode.ARTICLE_NOT_FOUND))
                .when(articleService).modify(eq(title), eq(content), any(), eq(1L));

        mockMvc.perform(put("/api/v1/article/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        // TODO: add request body
                        .content(objectMapper.writeValueAsBytes(new ArticleModifyRequest(
                                title, content
                        )))
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void deleteArticleTest() throws Exception {
        mockMvc.perform(delete("/api/v1/article/1")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void deleteArticleFailedCuzUnAuthorizedTest() throws Exception {
        mockMvc.perform(delete("/api/v1/article/1")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void deleteArticleFailedCuzNonWriterTest() throws Exception {
        doThrow(new PalantirException(ErrorCode.INVALID_PERMISSION)).when(articleService).delete(any(), any());

        mockMvc.perform(delete("/api/v1/article/1")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void deleteArticleFailedCuzNotFoundArticleTest() throws Exception {
        doThrow(new PalantirException(ErrorCode.ARTICLE_NOT_FOUND)).when(articleService).delete(any(), any());

        mockMvc.perform(delete("/api/v1/article/1")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isNotFound());
    }
}
