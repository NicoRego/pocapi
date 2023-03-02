package com.nicorego.nhs.pocapi;

import com.nicorego.nhs.pocapi.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.nicorego.nhs.pocapi.service.HospitalService;
import com.nicorego.nhs.pocapi.service.SpecialtyService;

@SpringBootApplication
public class PocapiApplication implements CommandLineRunner {

	@Autowired
	private HospitalService hospitalService;
	@Autowired
	private SpecialtyService specialtyService;
	@Autowired
	private HospitalRepository hospitalRepository;

	@Autowired
	public PocapiApplication(HospitalService hospitalService, SpecialtyService specialtyService, HospitalRepository hospitalRepository) {
		this.hospitalService = hospitalService;
		this.specialtyService = specialtyService;
		this.hospitalRepository = hospitalRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(PocapiApplication.class, args);
	}

	@Override
	public void run(String... args) {
		//
		// PersonalTests personalTests = new PersonalTests();
		// personalTests.main();
	}

}
