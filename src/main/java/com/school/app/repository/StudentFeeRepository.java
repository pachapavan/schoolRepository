package com.school.app.repository;

import com.school.app.domain.StudentFee;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the StudentFee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentFeeRepository extends JpaRepository<StudentFee, Long> {}
