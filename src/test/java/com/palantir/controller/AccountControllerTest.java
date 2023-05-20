package com.palantir.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palantir.controller.request.AccountLoginRequest;
import com.palantir.controller.request.AccountSignUpRequest;
import com.palantir.exception.ErrorCode;
import com.palantir.exception.PalantirException;
import com.palantir.model.Account;
import com.palantir.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @Test
    public void signUpTest() throws Exception {
        String accountId = "testAccount";
        String accountPassword = "password";

        when(accountService.signUp(accountId, accountPassword)).thenReturn(mock(Account.class));

        mockMvc.perform(post("/api/v1/account/signUp")
                .contentType(MediaType.APPLICATION_JSON)
                // TODO: add request body
                .content(objectMapper.writeValueAsBytes(new AccountSignUpRequest(
                        accountId, accountPassword
                )))
        ).andDo(print())
        .andExpect(status().isOk());
    }

    @Test
    public void signUpDuplicateAccountTest() throws Exception {
        String accountId = "testAccount";
        String accountPassword = "password";

        when(accountService.signUp(accountId, accountPassword))
                .thenThrow(new PalantirException(ErrorCode.DUPLICATED_ACCOUNT_ID));

        mockMvc.perform(post("/api/v1/account/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        // TODO: add request body
                        .content(objectMapper.writeValueAsBytes(new AccountSignUpRequest(
                                accountId, accountPassword
                        )))
                ).andDo(print())
                .andExpect(status().isConflict());
    }


    @Test
    public void loginTest() throws Exception {
        String accountId = "testAccount";
        String accountPassword = "password";

        when(accountService.login(accountId, accountPassword)).thenReturn("test_token");

        mockMvc.perform(post("/api/v1/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        // TODO: add request body
                        .content(objectMapper.writeValueAsBytes(new AccountLoginRequest(
                                accountId, accountPassword
                        )))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void loginWrongIdTest() throws Exception {
        String accountId = "testAccount";
        String accountPassword = "password";

        when(accountService.login(accountId, accountPassword)).thenThrow(new PalantirException(ErrorCode.ACCOUNT_NOT_FOUND));

        mockMvc.perform(post("/api/v1/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        // TODO: add request body
                        .content(objectMapper.writeValueAsBytes(new AccountLoginRequest(
                                accountId, accountPassword
                        )))
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void loginWrongPasswordTest() throws Exception {
        String accountId = "testAccount";
        String accountPassword = "password";

        when(accountService.login(accountId, accountPassword))
                .thenThrow(new PalantirException(ErrorCode.INVALID_PASSWORD));

        mockMvc.perform(post("/api/v1/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        // TODO: add request body
                        .content(objectMapper.writeValueAsBytes(new AccountLoginRequest(
                                accountId, accountPassword
                        )))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
