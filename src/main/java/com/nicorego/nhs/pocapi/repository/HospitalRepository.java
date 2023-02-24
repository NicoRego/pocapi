package com.nicorego.nhs.pocapi.repository;

import org.springframework.data.repository.CrudRepository;

import com.nicorego.nhs.pocapi.model.Hospital;

public interface HospitalRepository extends CrudRepository<Hospital, Integer>{

}
