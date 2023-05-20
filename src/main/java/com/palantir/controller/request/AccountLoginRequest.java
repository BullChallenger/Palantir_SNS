package com.palantir.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountLoginRequest {

    private String accountId;
    private String password;

}
