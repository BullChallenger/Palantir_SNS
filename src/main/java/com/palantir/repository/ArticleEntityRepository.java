package com.palantir.repository;

import com.palantir.model.entity.AccountEntity;
import com.palantir.model.entity.ArticleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleEntityRepository extends JpaRepository<ArticleEntity, Long> {

    Page<ArticleEntity> findAllByWriter(AccountEntity account, Pageable pageable);
<<<<<<< HEAD
=======

>>>>>>> main
}
