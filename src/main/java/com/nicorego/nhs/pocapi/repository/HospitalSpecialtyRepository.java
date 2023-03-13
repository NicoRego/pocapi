package com.nicorego.nhs.pocapi.repository;

import com.nicorego.nhs.pocapi.model.HospitalSpecialty;
import com.nicorego.nhs.pocapi.model.HospitalSpecialtyId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalSpecialtyRepository extends JpaRepository<HospitalSpecialty, HospitalSpecialtyId> {
}