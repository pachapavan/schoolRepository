package com.school.app.repository;

import com.school.app.domain.Text;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Text entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TextRepository extends JpaRepository<Text, Long> {}
