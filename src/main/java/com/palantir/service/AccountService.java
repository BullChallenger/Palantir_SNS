package com.palantir.service;

import com.palantir.exception.ErrorCode;
import com.palantir.exception.PalantirException;
import com.palantir.model.Account;
import com.palantir.model.Alarm;
import com.palantir.model.entity.AccountEntity;
import com.palantir.repository.AccountCacheRepository;
import com.palantir.repository.AccountEntityRepository;
import com.palantir.repository.AlarmEntityRepository;
import com.palantir.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountEntityRepository accountEntityRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AlarmEntityRepository alarmEntityRepository;
    private final AccountCacheRepository accountCacheRepository;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    public Account loadAccountByAccountId(String accountId) {
        return accountCacheRepository.getAccount(accountId).orElseGet(
                () -> accountEntityRepository.findByAccountId(accountId).map(Account::fromEntity).orElseThrow(
                        () -> new PalantirException(ErrorCode.ACCOUNT_NOT_FOUND, String.format("%s not found", accountId))
                ));
    }

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
        Account theAccount = loadAccountByAccountId(accountId);
        accountCacheRepository.setAccount(theAccount);
        // Check Password
        if (!passwordEncoder.matches(password, theAccount.getPassword())) {
            throw new PalantirException(ErrorCode.INVALID_PASSWORD);
        }

        String token = JwtTokenUtils.generateToken(accountId, secretKey, expiredTimeMs);

        // Create Token
        return token;
    }

    public Page<Alarm> alarmList(Long accountId, Pageable pageable) {
        return alarmEntityRepository.findAllByReceiverId(accountId, pageable).map(Alarm::fromEntity);
    }
}
