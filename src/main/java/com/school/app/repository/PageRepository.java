package com.school.app.repository;

import com.school.app.domain.Page;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Page entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PageRepository extends JpaRepository<Page, Long> {}
