package com.palantir.service;

import com.palantir.exception.ErrorCode;
import com.palantir.exception.PalantirException;
import com.palantir.fixture.AccountEntityFixture;
import com.palantir.model.entity.AccountEntity;
import com.palantir.repository.AccountEntityRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @MockBean
    private AccountEntityRepository accountEntityRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void signUpSuccessTest() {
        String accountId = "testAccount";
        String accountPassword = "password";

        AccountEntity fixture = AccountEntityFixture.get(accountId, accountPassword);

        when(accountEntityRepository.findByAccountId(accountId)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(accountPassword)).thenReturn("encrypt_password");
        when(accountEntityRepository.save(any())).thenReturn(fixture);

        assertDoesNotThrow(() -> accountService.signUp(accountId, accountPassword));
    }

    @Test
    public void signUpFailedCauseDuplicateAccountIdTest() {
        String accountId = "testAccount";
        String accountPassword = "password";

        AccountEntity fixture = AccountEntityFixture.get(accountId, accountPassword);

        when(accountEntityRepository.findByAccountId(accountId)).thenReturn(Optional.of(fixture));
        when(passwordEncoder.encode(accountPassword)).thenReturn("encrypt_password");
        when(accountEntityRepository.save(any())).thenReturn(Optional.of(fixture));

        PalantirException e = assertThrows(PalantirException.class, () -> accountService.signUp(accountId, accountPassword));
        assertEquals(ErrorCode.DUPLICATED_ACCOUNT_ID, e.getErrorCode());
    }

    @Test
    public void loginSuccessTest() {
        String accountId = "testAccount";
        String accountPassword = "password";

        AccountEntity fixture = AccountEntityFixture.get(accountId, accountPassword);

        when(accountEntityRepository.findByAccountId(accountId)).thenReturn(Optional.of(fixture));
        when(passwordEncoder.matches(accountPassword, fixture.getPassword())).thenReturn(true);

        assertDoesNotThrow(() -> accountService.login(accountId, accountPassword));
    }

    @Test
    public void loginFailedCauseNotFoundTest() {
        String accountId = "testAccount";
        String accountPassword = "password";

        when(accountEntityRepository.findByAccountId(accountId)).thenReturn(Optional.empty());

        PalantirException e = assertThrows(PalantirException.class, () -> accountService.login(accountId, accountPassword));
        assertEquals(ErrorCode.ACCOUNT_NOT_FOUND, e.getErrorCode());
    }

    @Test
    public void loginFailedCauseWrongPasswordTest() {
        String accountId = "testAccount";
        String accountPassword = "password";
        String wrongPassword = "wrongPassword";

        AccountEntity fixture = AccountEntityFixture.get(accountId, accountPassword);

        when(accountEntityRepository.findByAccountId(accountId)).thenReturn(Optional.of(fixture));

        PalantirException e = assertThrows(PalantirException.class, () -> accountService.login(accountId, wrongPassword));
        assertEquals(ErrorCode.INVALID_PASSWORD, e.getErrorCode());
    }
}
