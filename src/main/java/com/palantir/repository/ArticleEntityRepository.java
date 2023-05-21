package com.palantir.repository;

import com.palantir.model.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleEntityRepository extends JpaRepository<ArticleEntity, Long> {
}
