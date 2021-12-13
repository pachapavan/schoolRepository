package com.school.app.repository;

import com.school.app.domain.DisplayAtt;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DisplayAtt entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DisplayAttRepository extends JpaRepository<DisplayAtt, Long> {}
