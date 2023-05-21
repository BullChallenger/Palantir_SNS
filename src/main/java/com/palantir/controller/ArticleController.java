package com.palantir.controller;

import com.palantir.controller.request.ArticleCreateRequest;
import com.palantir.controller.request.ArticleModifyRequest;
import com.palantir.controller.response.ArticleResponse;
import com.palantir.controller.response.Response;
import com.palantir.model.Article;
import com.palantir.service.ArticleService;
import lombok.RequiredArgsConstructor;
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
}
