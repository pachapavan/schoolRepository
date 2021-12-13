package com.school.app.repository;

import com.school.app.domain.TabelValues;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TabelValues entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TabelValuesRepository extends JpaRepository<TabelValues, Long> {}
