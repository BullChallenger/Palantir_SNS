package com.palantir.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATED_ACCOUNT_ID(HttpStatus.CONFLICT, "Account ID is duplicated"),;

    private HttpStatus status;
    private String errorMessage;
}
