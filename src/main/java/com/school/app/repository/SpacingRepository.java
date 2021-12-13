package com.school.app.repository;

import com.school.app.domain.Spacing;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Spacing entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpacingRepository extends JpaRepository<Spacing, Long> {}
