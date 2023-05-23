package com.palantir.controller;

import com.palantir.controller.request.ArticleCreateRequest;
import com.palantir.controller.request.ArticleModifyRequest;
import com.palantir.controller.request.CommentPostRequest;
import com.palantir.controller.response.ArticleResponse;
import com.palantir.controller.response.CommentResponse;
import com.palantir.controller.response.Response;
import com.palantir.model.Account;
import com.palantir.model.Article;
import com.palantir.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "api/v1/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public Response<Void> create(@RequestBody ArticleCreateRequest request, Authentication authentication) {
        articleService.create(request.getTitle(), request.getContent(), authentication.getName());
        return Response.success();
    }

    @PutMapping(value = "/{articleId}")
    public Response<ArticleResponse> modify(@PathVariable("articleId") Long articleId,
                                 @RequestBody ArticleModifyRequest request, Authentication authentication) {
        Article theArticle = articleService.modify(request.getTitle(), request.getContent(), authentication.getName(), articleId);
        return Response.success(ArticleResponse.fromArticle(theArticle));
    }

    @DeleteMapping(value = "/{articleId}")
    public Response<Void> delete(@PathVariable("articleId") Long articleId, Authentication authentication) {
        articleService.delete(authentication.getName(), articleId);
        return Response.success();
    }

    @GetMapping
    public Response<Page<ArticleResponse>> list(Pageable pageable) {
        return Response.success(articleService.list(pageable).map(ArticleResponse::fromArticle));
    }

    @GetMapping(value = "/my")
    public Response<Page<ArticleResponse>> my(Pageable pageable, Authentication authentication) {
        return Response.success(articleService.myList(authentication.getName(), pageable).map(ArticleResponse::fromArticle));
    }

    @PostMapping(value = "/{articleId}/like")
    public Response<Void> like(@PathVariable("articleId") Long articleId, Authentication authentication) {
        articleService.like(articleId, authentication.getName());
        return Response.success();
    }

    @GetMapping(value = "/{articleId}/like")
    public Response<Long> likeCount(@PathVariable("articleId") Long articleId) {
        return Response.success(articleService.likeCount(articleId));
    }

    @PostMapping(value = "/{articleId}/comment")
    public Response<Void> comment(@PathVariable("articleId") Long articleId, @RequestBody CommentPostRequest request,
                                  Authentication authentication) {
        articleService.comment(articleId, authentication.getName(), request.getContent());
        return Response.success();
    }

    @GetMapping(value = "/{articleId}/comment")
    public Response<Page<CommentResponse>> comment(@PathVariable("articleId") Long articleId, Pageable pageable) {
        return Response.success(articleService.getComment(articleId, pageable).map(CommentResponse::fromComment));
    }
}


