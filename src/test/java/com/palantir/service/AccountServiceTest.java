package com.palantir.service;

import com.palantir.exception.PalantirException;
import com.palantir.fixture.AccountEntityFixture;
import com.palantir.model.entity.AccountEntity;
import com.palantir.repository.AccountEntityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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

    @Test
    public void signUpSuccessTest() {
        String accountId = "testAccount";
        String accountPassword = "password";

        AccountEntity fixture = AccountEntityFixture.get(accountId, accountPassword);

        when(accountEntityRepository.findByAccountId(accountId)).thenReturn(Optional.empty());
        when(accountEntityRepository.save(any())).thenReturn(Optional.of(fixture));

        assertDoesNotThrow(() -> accountService.signUp(accountId, accountPassword));
    }

    @Test
    public void signUpFailedCauseDuplicateAccountIdTest() {
        String accountId = "testAccount";
        String accountPassword = "password";

        AccountEntity fixture = AccountEntityFixture.get(accountId, accountPassword);

        when(accountEntityRepository.findByAccountId(accountId)).thenReturn(Optional.of(fixture));
        when(accountEntityRepository.save(any())).thenReturn(Optional.of(fixture));

        assertThrows(PalantirException.class, () -> accountService.signUp(accountId, accountPassword));
    }

    @Test
    public void loginSuccessTest() {
        String accountId = "testAccount";
        String accountPassword = "password";

        AccountEntity fixture = AccountEntityFixture.get(accountId, accountPassword);

        when(accountEntityRepository.findByAccountId(accountId)).thenReturn(Optional.of(fixture));
        assertDoesNotThrow(() -> accountService.login(accountId, accountPassword));
    }

    @Test
    public void loginFailedCauseNotFoundTest() {
        String accountId = "testAccount";
        String accountPassword = "password";

        when(accountEntityRepository.findByAccountId(accountId)).thenReturn(Optional.empty());

        assertThrows(PalantirException.class, () -> accountService.login(accountId, accountPassword));
    }

    @Test
    public void loginFailedCauseWrongPasswordTest() {
        String accountId = "testAccount";
        String accountPassword = "password";
        String wrongPassword = "wrongPassword";

        AccountEntity fixture = AccountEntityFixture.get(accountId, accountPassword);

        when(accountEntityRepository.findByAccountId(accountId)).thenReturn(Optional.of(fixture));

        assertThrows(PalantirException.class, () -> accountService.login(accountId, wrongPassword));
    }
}
