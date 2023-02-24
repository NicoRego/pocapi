package com.nicorego.nhs.pocapi.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nicorego.nhs.pocapi.model.Hospital;
import com.nicorego.nhs.pocapi.repository.HospitalRepository;

@Service
public class HospitalService {

	@Autowired
	private HospitalRepository hospitalRepository;
	
	public Iterable<Hospital> getHospitals(){
		return hospitalRepository.findAll();
	}

	public Optional<Hospital> getHospitalById(int id) {
		return hospitalRepository.findById(id);
	}

}
