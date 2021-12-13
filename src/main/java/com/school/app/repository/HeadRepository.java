package com.school.app.repository;

import com.school.app.domain.Head;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Head entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HeadRepository extends JpaRepository<Head, Long> {}
