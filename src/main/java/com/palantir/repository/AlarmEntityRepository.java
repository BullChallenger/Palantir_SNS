package com.palantir.repository;

import com.palantir.model.entity.AccountEntity;
import com.palantir.model.entity.AlarmEntity;
import com.palantir.model.entity.ArticleEntity;
import com.palantir.model.entity.LikeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AlarmEntityRepository extends JpaRepository<AlarmEntity, Long> {

    Page<AlarmEntity> findAllByReceiver(AccountEntity receiver, Pageable pageable);
<<<<<<< HEAD

    Page<AlarmEntity> findAllByReceiverId(Long accountId, Pageable pageable);
=======
>>>>>>> main
}
