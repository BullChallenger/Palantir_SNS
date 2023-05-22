package com.palantir.repository;

import com.palantir.model.entity.ArticleEntity;
import com.palantir.model.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentEntityRepository extends JpaRepository<CommentEntity, Long> {

    Page<CommentEntity> findAllByArticle(ArticleEntity article, Pageable pageable);

}
