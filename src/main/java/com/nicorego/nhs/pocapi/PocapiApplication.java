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
import com.nicorego.nhs.pocapi.business.Distance;

import jakarta.transaction.Transactional;

import static java.lang.Math.round;

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
		System.out.println();
		System.out.println("Test - ALL HOSPITALS");
		System.out.println("====================");
		System.out.println();
		Iterable<Hospital> iterableHospitals = hospitalService.getHospitals();
		iterableHospitals.forEach(hospital -> System.out.println(hospital.getId() + "-" + hospital.getName() + "-" + hospital.getFreeBeds()));
		
		// Get one hospital
		System.out.println();
		System.out.println("Test - ONE HOSPITAL");
		System.out.println("===================");
		System.out.println();
		
		Optional<Hospital> optionalHospital = hospitalService.getHospitalById(1);
		Hospital hospitalId = optionalHospital.get();
		System.out.println(hospitalId.getId() + "-" + hospitalId.getName());
		
	
		// Get one specialty and related hospitals
		System.out.println();
		System.out.println("Test - ONE SPECIALTY AND ITS HOSPITALS");
		System.out.println("======================================");
		System.out.println();
		
		Optional<Specialty> optSpecialtyForHospitals = specialtyService.getSpecialtyById(2);
		Specialty specialtyIdForHospitals = optSpecialtyForHospitals.get();
		
		System.out.println("Spécialité : " + specialtyIdForHospitals.getName());
		System.out.println();
		System.out.println("Hôpitaux associés :");
		
		specialtyIdForHospitals.getHospitals().forEach(
				specialty -> System.out.println("- " + specialty.getName()));
				 
		// Get one hospital and related specialties
		System.out.println();
		System.out.println("Test - ONE HOSPITALS AND ITS SPECIALTIES");
		System.out.println("========================================");
		System.out.println();
				
		Optional<Hospital> optHospitalForSpecialties = hospitalService.getHospitalById(1);
		Hospital hospitalIdForSpecialties = optHospitalForSpecialties.get();

		System.out.println("Hôpital : " + hospitalIdForSpecialties.getName());
		System.out.println();
		System.out.println("Spécialités associées :");
		
		hospitalIdForSpecialties.getSpecialties().forEach(
				specialtiesForHopsital -> System.out.println(specialtiesForHopsital.getId() + " - " + specialtiesForHopsital.getName()));

		// Get Hospitals by specialty and free beds

		System.out.println();
		System.out.println("Test - FIND HOSPITALS FOR A GIVEN SPECIALTY AND WITH A MINIMUM OF FREE BEDS");
		System.out.println("===========================================================================");
		System.out.println();

		int givenSpecialtyId = 1;
		int minFreeBeds = 0;

		// Get specialty
		Optional<Specialty> optGivenSpecialty = specialtyService.getSpecialtyById(givenSpecialtyId);
		Specialty givenSpecialty = optGivenSpecialty.get();

		// Resolve query
		Iterable<Hospital> iterableHospitalsSpecBeds = hospitalService.getHospitalsBySpecialtyAndFreeBeds(givenSpecialtyId,minFreeBeds);

		// Print out result
		System.out.println("Spécialité : " + givenSpecialty.getName());
		System.out.println();

		System.out.println("Hôpitaux associés :");
		iterableHospitalsSpecBeds.forEach(hospitalSpecBeds -> System.out.println(hospitalSpecBeds.getId() + "-" + hospitalSpecBeds.getName() + "-" + hospitalSpecBeds.getFreeBeds()));

		// Get Hospitals by specialty and free beds

		System.out.println();
		System.out.println("Test - FIND HOSPITALS FOR A GIVEN SPECIALTY");
		System.out.println("===========================================");
		System.out.println();

		int given2ndSpecialtyId = 1;

		// Get specialty
		Optional<Specialty> opt2ndGivenSpecialty = specialtyService.getSpecialtyById(given2ndSpecialtyId);
		Specialty given2ndSpecialty = opt2ndGivenSpecialty.get();

		// Resolve query
		Iterable<Hospital> iterableHospitalsSpec = hospitalService.getHospitalsBySpecialty(given2ndSpecialtyId);

		// Print out result
		System.out.println("Spécialité : " + given2ndSpecialty.getName());
		System.out.println();

		System.out.println("Hôpitaux associés :");
		iterableHospitalsSpec.forEach(hospitalSpec -> System.out.println(hospitalSpec.getId() + "-" + hospitalSpec.getName() + "-" + hospitalSpec.getFreeBeds()));

		// Get nearest hospitals for a given specialty and with free beds

		System.out.println();
		System.out.println("Test - FIND NEAREST HOSPITALS FOR A GIVEN SPECIALTY AND WITH FREE BEDS");
		System.out.println("===========================================");
		System.out.println();

		int given3rdSpecialtyId = 1;
		double latitude = 50.637062;
		double longitude = 3.064312;

		System.out.println();

		Hospital nearestAvailableHospital = hospitalService.getNearestAvailableHospital(latitude,  longitude, given3rdSpecialtyId);
		Specialty given3rdSpecialty = opt2ndGivenSpecialty.get();

		// Print out result
		System.out.println("Spécialité : " + given3rdSpecialty.getName());
		System.out.println();

		System.out.println("Hôpital disponible :");
		System.out.println(nearestAvailableHospital.getName());
		System.out.println();
		System.out.println("Distance du point :");
		System.out.println(round(Distance.distanceHaversine(nearestAvailableHospital.getLatitude(), nearestAvailableHospital.getLongitude(), latitude, longitude)) + " kms");

	}
}
