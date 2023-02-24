package com.nicorego.nhs.pocapi;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.nicorego.nhs.pocapi.model.Hospital;
import com.nicorego.nhs.pocapi.model.Specialty;
import com.nicorego.nhs.pocapi.service.HospitalService;
import com.nicorego.nhs.pocapi.service.SpecialtyService;

import jakarta.transaction.Transactional;

@SpringBootApplication
public class PocapiApplication implements CommandLineRunner {

	@Autowired
	private HospitalService hospitalService;
	
	@Autowired
	private SpecialtyService specialtyService;
	
	public static void main(String[] args) {
		SpringApplication.run(PocapiApplication.class, args);
	}

	@Transactional
	@Override
	public void run(String... args) throws Exception {
			
		// Get all hospitals
		System.out.println("");
		System.out.println("Test - ALL HOSPITALS");
		System.out.println("====================");
		System.out.println("");
		Iterable<Hospital> iterableHospitals = hospitalService.getHospitals();
		iterableHospitals.forEach(hospital -> System.out.println(hospital.getId() + "-" + hospital.getName() + "-" + hospital.getFreeBeds()));
		
		// Get one hospital
		System.out.println("");
		System.out.println("Test - ONE HOSPITAL");
		System.out.println("===================");
		System.out.println("");
		
		Optional<Hospital> optionalHospital = hospitalService.getHospitalById(1);
		Hospital hospitalId = optionalHospital.get();
		System.out.println(hospitalId.getId() + "-" + hospitalId.getName());
		
	
		// Get one specialty and related hospitals
		System.out.println("");
		System.out.println("Test - ONE SPECIALTY AND ITS HOSPITALS");
		System.out.println("======================================");
		System.out.println("");
		
		Optional<Specialty> optSpecialtyForHospitals = specialtyService.getSpecialtyById(2);
		Specialty specialtyIdForHospitals = optSpecialtyForHospitals.get();
		
		System.out.println("Spécialité : " + specialtyIdForHospitals.getName());
		System.out.println("");
		System.out.println("Hôpitaux associés :");
		
		specialtyIdForHospitals.getHospitals().forEach(
				specialty -> System.out.println("- " + specialty.getName()));
				 
		// Get one hospital and related specialties
		System.out.println("");
		System.out.println("Test - ONE HOSPITALS AND ITS SPECIALTIES");
		System.out.println("=====================================++=");
		System.out.println("");
				
		Optional<Hospital> optHospitalForSpecialties = hospitalService.getHospitalById(1);
		Hospital hospitalIdForSpecialties = optHospitalForSpecialties.get();

		System.out.println("Hôpital : " + hospitalIdForSpecialties.getName());
		System.out.println("");
		System.out.println("Spécialités associées :");
		
		hospitalIdForSpecialties.getSpecialties().forEach(
				specialtiesForHopsital -> System.out.println(specialtiesForHopsital.getId() + " - " + specialtiesForHopsital.getName()));

	}
}
