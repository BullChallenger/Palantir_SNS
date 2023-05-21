package com.palantir.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATED_ACCOUNT_ID(HttpStatus.CONFLICT, "Account ID is duplicated"),
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "Account Not Found"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Password is invalid"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Token is invalid"),
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "Article Not Found"),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "Permission is invalid"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error!");

    private HttpStatus status;
    private String errorMessage;
}
