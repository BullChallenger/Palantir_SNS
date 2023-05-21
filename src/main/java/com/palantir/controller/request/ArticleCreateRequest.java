package com.palantir.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticleCreateRequest {
    private String title;
    private String content;
}
