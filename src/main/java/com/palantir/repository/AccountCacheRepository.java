package com.palantir.repository;

import com.palantir.model.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AccountCacheRepository {

    private final RedisTemplate<String, Account> accountRedisTemplate;

    private final static Duration ACCOUNT_CACHE_TTL = Duration.ofDays(3);

    public void setAccount(Account account) {
        String key = getKey(account.getUsername());
        log.info("Set Account to Redis {}: {}", key, account);
        accountRedisTemplate.opsForValue().set(key, account, ACCOUNT_CACHE_TTL);
    }

    public Optional<Account> getAccount(String accountId) {
        String key = getKey(accountId);
        Account theAccount = accountRedisTemplate.opsForValue().get(getKey(accountId));
        log.info("Get data from Redis {}: {}", key, theAccount);
        return Optional.ofNullable(theAccount);
    }

    private String getKey(String accountId) {
        return "ACCOUNT:" + accountId;
    }
}
