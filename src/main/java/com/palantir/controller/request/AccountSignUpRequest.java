package com.palantir.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountSignUpRequest {

    private String accountId;
    private String password;

}
