package com.school.app.repository;

import com.school.app.domain.Button;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Button entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ButtonRepository extends JpaRepository<Button, Long> {}
