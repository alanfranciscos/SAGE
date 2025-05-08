package com.sage.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sage.model.ControlResident;

public interface ControlResidentRepository extends JpaRepository<ControlResident, Long> {

    Optional<ControlResident> findByControlId(String controlId);
}
