package com.school.app.repository;

import com.school.app.domain.ObjectContainingString;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ObjectContainingString entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ObjectContainingStringRepository extends JpaRepository<ObjectContainingString, Long> {}
