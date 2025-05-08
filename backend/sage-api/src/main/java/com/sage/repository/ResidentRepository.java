package com.sage.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sage.model.Resident;

public interface ResidentRepository extends JpaRepository<Resident, UUID> {

}
