package com.school.app.repository;

import com.school.app.domain.Table;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Table entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TableRepository extends JpaRepository<Table, Long> {}
