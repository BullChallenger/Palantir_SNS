package com.palantir.controller;

import com.palantir.controller.request.AccountSignUpRequest;
import com.palantir.controller.response.AccountSignUpResponse;
import com.palantir.controller.response.Response;
import com.palantir.model.Account;
import com.palantir.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // TODO: implement
    @PostMapping(value = "/signUp")
    public Response<AccountSignUpResponse> signUp(@RequestBody AccountSignUpRequest request) {
        Account theAccount = accountService.signUp(request.getAccountId(), request.getPassword());
        return Response.success(AccountSignUpResponse.fromAccount(theAccount));
    }
}
