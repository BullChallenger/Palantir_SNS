package com.palantir.repository;

import com.palantir.model.entity.ArticleEntity;
import com.palantir.model.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface CommentEntityRepository extends JpaRepository<CommentEntity, Long> {

    Page<CommentEntity> findAllByArticle(ArticleEntity article, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "UPDATE CommentEntity entity SET entity.deletedAt = NOW() WHERE entity.article =: article")
    void deleteAllByArticle(@Param("article") ArticleEntity article);
}
