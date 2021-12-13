package com.school.app.repository;

import com.school.app.domain.BusRouteName;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BusRouteName entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BusRouteNameRepository extends JpaRepository<BusRouteName, Long> {}
