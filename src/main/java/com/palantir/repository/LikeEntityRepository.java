package com.palantir.repository;

import com.palantir.model.entity.AccountEntity;
import com.palantir.model.entity.ArticleEntity;
import com.palantir.model.entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
=======
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

>>>>>>> main
import java.util.List;
import java.util.Optional;

public interface LikeEntityRepository extends JpaRepository<LikeEntity, Long> {

    Optional<LikeEntity> findByAccountAndArticle(AccountEntity account, ArticleEntity article);

<<<<<<< HEAD
    long countByArticle(ArticleEntity article);

    @Transactional
    @Modifying
    @Query(value = "UPDATE LikeEntity entity SET entity.deletedAt = NOW() WHERE entity.article =: article")
    void deleteAllByArticle(@Param("article") ArticleEntity article);
=======
    @Query(value = "SELECT count(*) FROM LikeEntity entity WHERE entity.article =:article")
    Integer countByArticle(@Param("article") ArticleEntity article);

    List<LikeEntity> findAllByArticle(ArticleEntity article);
>>>>>>> main
}
