package com.palantir.repository;

import com.palantir.model.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountEntityRepository extends JpaRepository<AccountEntity, Long> {

    Optional<AccountEntity> findByAccountId(String accountId);
}
