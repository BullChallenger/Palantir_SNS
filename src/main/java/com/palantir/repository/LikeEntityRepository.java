package com.palantir.repository;

import com.palantir.model.entity.AccountEntity;
import com.palantir.model.entity.ArticleEntity;
import com.palantir.model.entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeEntityRepository extends JpaRepository<LikeEntity, Long> {

    Optional<LikeEntity> findByAccountAndArticle(AccountEntity account, ArticleEntity article);

    @Query(value = "SELECT count(*) FROM LikeEntity entity WHERE entity.article =:article")
    Integer countByArticle(@Param("article") ArticleEntity article);

    List<LikeEntity> findAllByArticle(ArticleEntity article);
}
