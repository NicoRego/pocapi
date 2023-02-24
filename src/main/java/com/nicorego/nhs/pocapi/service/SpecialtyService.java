package com.nicorego.nhs.pocapi.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nicorego.nhs.pocapi.model.Specialty;
import com.nicorego.nhs.pocapi.repository.SpecialtyRepository;

@Service
public class SpecialtyService {

	@Autowired
	private SpecialtyRepository specialtyRepository;
	
	public Iterable<Specialty> getSpecialties(){
		return specialtyRepository.findAll();
	}

	public Optional<Specialty> getSpecialtyById(int id) {
		return specialtyRepository.findById(id);
	}

}
