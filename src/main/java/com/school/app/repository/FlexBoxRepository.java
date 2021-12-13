package com.school.app.repository;

import com.school.app.domain.FlexBox;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FlexBox entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FlexBoxRepository extends JpaRepository<FlexBox, Long> {}
