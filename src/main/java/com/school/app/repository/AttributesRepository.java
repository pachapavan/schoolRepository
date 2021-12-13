package com.school.app.repository;

import com.school.app.domain.Attributes;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Attributes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttributesRepository extends JpaRepository<Attributes, Long> {}
