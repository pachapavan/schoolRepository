package com.school.app.repository;

import com.school.app.domain.BusRoute;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BusRoute entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BusRouteRepository extends JpaRepository<BusRoute, Long> {}
