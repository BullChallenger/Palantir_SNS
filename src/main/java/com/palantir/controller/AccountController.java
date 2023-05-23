package com.palantir.controller;

import com.palantir.controller.request.AccountLoginRequest;
import com.palantir.controller.request.AccountSignUpRequest;
import com.palantir.controller.response.AccountLoginResponse;
import com.palantir.controller.response.AccountSignUpResponse;
import com.palantir.controller.response.AlarmResponse;
import com.palantir.controller.response.Response;
import com.palantir.exception.ErrorCode;
import com.palantir.exception.PalantirException;
import com.palantir.model.Account;
import com.palantir.model.Alarm;
import com.palantir.service.AccountService;
import com.palantir.service.AlarmService;
import com.palantir.util.ClassUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AlarmService alarmService;

    // TODO: implement
    @PostMapping(value = "/signUp")
    public Response<AccountSignUpResponse> signUp(@RequestBody AccountSignUpRequest request) {
        Account theAccount = accountService.signUp(request.getAccountId(), request.getPassword());
        return Response.success(AccountSignUpResponse.fromAccount(theAccount));
    }

    @PostMapping(value = "/login")
    public Response<AccountLoginResponse> login(@RequestBody AccountLoginRequest request) {
        String token = accountService.login(request.getAccountId(), request.getPassword());
        return Response.success(new AccountLoginResponse(token));
    }

    @GetMapping(value = "/alarm")
    public Response<Page<AlarmResponse>> alarm(Pageable pageable, Authentication authentication) {
        Account theAccount = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), Account.class).orElseThrow(
                () -> new PalantirException(ErrorCode.INTERNAL_SERVER_ERROR, "Casting to Account class failed.")
        );
        return Response.success(accountService.alarmList(theAccount.getId(), pageable).map(AlarmResponse::fromAlarm));
    }

    @GetMapping(value = "/alarm/subscribe")
    public SseEmitter subscribe(Authentication authentication) {
        Account theAccount = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), Account.class).orElseThrow(
                () -> new PalantirException(ErrorCode.INTERNAL_SERVER_ERROR, "Casting to Account class failed.")
        );
        return alarmService.connectAlarm(theAccount.getId());   
    }
}
