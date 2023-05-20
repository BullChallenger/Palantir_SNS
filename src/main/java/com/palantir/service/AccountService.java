package com.palantir.service;

import com.palantir.exception.ErrorCode;
import com.palantir.exception.PalantirException;
import com.palantir.model.Account;
import com.palantir.model.entity.AccountEntity;
import com.palantir.repository.AccountEntityRepository;
import com.palantir.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountEntityRepository accountEntityRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    // TODO: implement
    @Transactional
    public Account signUp(String accountId, String password) {
        // Check Duplicated Account
        accountEntityRepository.findByAccountId(accountId).ifPresent( accountEntity -> {
            throw new PalantirException(ErrorCode.DUPLICATED_ACCOUNT_ID, String.format("%s is duplicated", accountId));
        });

        // Encode password
        String bCryptPassword = passwordEncoder.encode(password);

        // Registration
        AccountEntity theAccountEntity = accountEntityRepository.save(AccountEntity.of(accountId, bCryptPassword));

        return Account.fromEntity(theAccountEntity);
    }

    // TODO: implement
    public String login(String accountId, String password) {
        // Check SignUp Account
        AccountEntity theAccountEntity = accountEntityRepository.findByAccountId(accountId).orElseThrow(
                () -> new PalantirException(ErrorCode.ACCOUNT_NOT_FOUND, String.format("%s not founded!", accountId))
        );

        // Check Password
        if (!passwordEncoder.matches(password, theAccountEntity.getPassword())) {
            throw new PalantirException(ErrorCode.INVALID_PASSWORD);
        }

        String token = JwtTokenUtils.generateToken(accountId, secretKey, expiredTimeMs);

        // Create Token
        return token;
    }
}
