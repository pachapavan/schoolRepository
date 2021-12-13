package com.school.app.repository;

import com.school.app.domain.BusStops;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BusStops entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BusStopsRepository extends JpaRepository<BusStops, Long> {}
