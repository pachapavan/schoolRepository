package com.school.app.repository;

import com.school.app.domain.StaffSalary;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the StaffSalary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StaffSalaryRepository extends JpaRepository<StaffSalary, Long> {}
