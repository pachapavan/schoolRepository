package com.school.app.repository;

import com.school.app.domain.ClassName;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ClassName entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassNameRepository extends JpaRepository<ClassName, Long> {
}
