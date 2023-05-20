package com.palantir.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

// TODO: implement
@Getter
@AllArgsConstructor
public class PalantirException extends RuntimeException {

    private ErrorCode errorCode;
    private String message;

    @Override
    public String getMessage() {
        if (message == null) {
            return errorCode.getErrorMessage();
        }

        return String.format("%s. %s", errorCode.getErrorMessage(), message);
    }
}
