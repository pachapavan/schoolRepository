package com.school.app.repository;

import com.school.app.domain.FormWrap;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FormWrap entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormWrapRepository extends JpaRepository<FormWrap, String> {}
