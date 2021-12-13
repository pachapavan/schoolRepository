package com.school.app.repository;

import com.school.app.domain.Icon;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Icon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IconRepository extends JpaRepository<Icon, Long> {}
