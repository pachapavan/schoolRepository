package com.school.app.repository;

import com.school.app.domain.StudentMarkes;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the StudentMarkes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentMarkesRepository extends JpaRepository<StudentMarkes, Long> {}
