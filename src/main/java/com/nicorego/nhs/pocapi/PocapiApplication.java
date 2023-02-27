package com.nicorego.nhs.pocapi;

import java.util.Optional;

import com.nicorego.nhs.pocapi.repository.HospitalRepository;
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

	private final HospitalService hospitalService;
	private final SpecialtyService specialtyService;
	private final HospitalRepository hospitalRepository;

	@Autowired
	public PocapiApplication(HospitalService hospitalService, SpecialtyService specialtyService, HospitalRepository hospitalRepository) {
		this.hospitalService = hospitalService;
		this.specialtyService = specialtyService;
		this.hospitalRepository = hospitalRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(PocapiApplication.class, args);
	}

	@Transactional
	@Override
	public void run(String... args) throws Exception {
		boolean runTest = false;

		if (runTest) {
			// Get all hospitals *******************************************************************************************

			System.out.println();
			System.out.println("====================");
			System.out.println("Test - ALL HOSPITALS");
			System.out.println("====================");
			System.out.println();
			Iterable<Hospital> iterableHospitals = hospitalService.getHospitals();
			iterableHospitals.forEach(hospital -> System.out.println(hospital.getId_hospital() + "-" + hospital.getHospital_name() + "-" + hospital.getFreeBeds()));

			// Get one hospital ********************************************************************************************

			System.out.println();
			System.out.println("===================");
			System.out.println("Test - ONE HOSPITAL");
			System.out.println("===================");
			System.out.println();

			Optional<Hospital> optionalHospital = hospitalService.getHospitalById(1);
			if (optionalHospital.isPresent()) {
				Hospital oneHospital = optionalHospital.get();
				System.out.println(oneHospital.getId_hospital() + "-" + oneHospital.getHospital_name());
			}

			// Get one specialty and related hospitals *********************************************************************

			System.out.println();
			System.out.println("======================================");
			System.out.println("Test - ONE SPECIALTY AND ITS HOSPITALS");
			System.out.println("======================================");
			System.out.println();

			Optional<Specialty> optSpecialtyForHospitals = specialtyService.getSpecialtyById(2);
			if (optSpecialtyForHospitals.isPresent()) {
				Specialty specialtyForHospitals = optSpecialtyForHospitals.get();


				System.out.println("Spécialité : " + specialtyForHospitals.getSpecialty_name());
				System.out.println();
				System.out.println("Hôpitaux associés :");

				specialtyForHospitals.getHospitals().forEach(
						specialty -> System.out.println("- " + specialty.getHospital_name()));
			}

			// Get one hospital and related specialties ********************************************************************

			System.out.println();
			System.out.println("===========================================");
			System.out.println("Test - ALL HOSPITALS AND THEIRS SPECIALTIES");
			System.out.println("===========================================");

			long maxHospitals = hospitalRepository.count();

			System.out.println("Nb d'entités dans hospitalRepository = " + maxHospitals);
			Optional<Hospital> optHospitalForSpecialties;
			for (int hospId = 1; hospId <= maxHospitals; ++hospId) {
				optHospitalForSpecialties = hospitalService.getHospitalById(hospId);
				if (optHospitalForSpecialties.isPresent()) {
					Hospital hospitalForSpecialties = optHospitalForSpecialties.get();

					System.out.println();
					System.out.println(String.format("Hôpital n° %d : %s ", hospId, hospitalForSpecialties.getHospital_name()));
					System.out.println("Nombre de lits disponibles : " + hospitalForSpecialties.getFreeBeds());
					System.out.println("Spécialités associées :");

					hospitalForSpecialties.getSpecialties().forEach(
							specialtiesForHopsital -> System.out.println(specialtiesForHopsital.getId_specialty() + " - " + specialtiesForHopsital.getSpecialty_name()));
				} else {
					System.out.println(String.format("Hôpital %d introuvable", hospId));
				}
			}
			// Get Hospitals by specialty and free beds ********************************************************************

			System.out.println();
			System.out.println("===========================================================================");
			System.out.println("Test - FIND HOSPITALS FOR A GIVEN SPECIALTY AND WITH A MINIMUM OF FREE BEDS");
			System.out.println("===========================================================================");
			System.out.println();

			int givenSpecialtyId = 1;
			int minFreeBeds = 0;

			// Get specialty
			Optional<Specialty> optGivenSpecialty = specialtyService.getSpecialtyById(givenSpecialtyId);
			if (optGivenSpecialty.isPresent()) {
				Specialty givenSpecialty = optGivenSpecialty.get();

				// Resolve query
				Iterable<Hospital> iterableHospitalsSpecBeds = hospitalService.getHospitalsBySpecialtyAndFreeBeds(givenSpecialtyId, minFreeBeds);

				// Print out result
				System.out.println("Spécialité : " + givenSpecialty.getSpecialty_name());
				System.out.println();

				System.out.println("Hôpitaux associés :");
				iterableHospitalsSpecBeds.forEach(hospitalSpecBeds -> System.out.println(hospitalSpecBeds.getId_hospital() + "-" + hospitalSpecBeds.getHospital_name() + "-" + hospitalSpecBeds.getFreeBeds()));
			}

			// Get Hospitals by specialty and free beds ********************************************************************

			System.out.println();
			System.out.println("===========================================");
			System.out.println("Test - FIND HOSPITALS FOR A GIVEN SPECIALTY");
			System.out.println("===========================================");
			System.out.println();

			int given2ndSpecialtyId = 1;

			// Get specialty
			Optional<Specialty> opt2ndGivenSpecialty = specialtyService.getSpecialtyById(given2ndSpecialtyId);
			if (opt2ndGivenSpecialty.isPresent()) {
				Specialty given2ndSpecialty = opt2ndGivenSpecialty.get();

				// Resolve query
				Iterable<Hospital> iterableHospitalsSpec = hospitalService.getHospitalsBySpecialty(given2ndSpecialtyId);

				// Print out result
				System.out.println("Spécialité : " + given2ndSpecialty.getSpecialty_name());
				System.out.println();

				System.out.println("Hôpitaux associés :");
				iterableHospitalsSpec.forEach(hospitalSpec -> System.out.println(hospitalSpec.getId_hospital() + "-" + hospitalSpec.getHospital_name() + "-" + hospitalSpec.getFreeBeds()));
			}
			// Get nearest hospitals for a given specialty and with free beds **********************************************

			System.out.println();
			System.out.println("======================================================================");
			System.out.println("Test - FIND NEAREST HOSPITALS FOR A GIVEN SPECIALTY AND WITH FREE BEDS");
			System.out.println("======================================================================");
			System.out.println();

			int given3rdSpecialtyId = 1;
			// Near Hôpital Saint Vincent de Paul
			// double latitude = 50.637062;
			// double longitude = 3.064312;
			// Near Centre Hospitalier Universitaire de Lille
			double latitude = 50.616312;
			double longitude = 3.051438;

			System.out.println();

			Optional<Specialty> optNearestSpecialty = specialtyService.getSpecialtyById(given3rdSpecialtyId);

			if (optNearestSpecialty.isPresent()) {
				Hospital nearestAvailableHospital = hospitalService.getNearestAvailableHospital(latitude, longitude, given3rdSpecialtyId);
				Specialty given3rdSpecialty = optNearestSpecialty.get();

				// Print out result
				System.out.println("Spécialité : " + given3rdSpecialty.getSpecialty_name());
				System.out.println();

				System.out.println(String.format("Hôpital disponible : %s", nearestAvailableHospital.getHospital_name()));
				System.out.println();
				System.out.println("Distance du point : " + round(Distance.distanceHaversine(nearestAvailableHospital.getLatitude(), nearestAvailableHospital.getLongitude(), latitude, longitude)) + " km(s)");
			}
			// Book a room in nearest available hospital *******************************************************************

			System.out.println();
			System.out.println("===============================================");
			System.out.println("Test - BOOK A BED IN NEAREST AVAILABLE HOSPITAL");
			System.out.println("===============================================");
			System.out.println();

			Hospital nearestAvailableHospital2nd = hospitalService.getNearestAvailableHospital(latitude, longitude, given3rdSpecialtyId);

			System.out.println(String.format("Hôpital réservé : %s", nearestAvailableHospital2nd.getHospital_name()));
			System.out.println(String.format("Nombre de lits disponibles après réservation : %d", nearestAvailableHospital2nd.getFreeBeds()));
			System.out.println();

			Hospital bookedHospital = hospitalService.bookBed(nearestAvailableHospital2nd);

			System.out.println(String.format("Nombre de lits disponibles après réservation : %d", bookedHospital.getFreeBeds()));
			System.out.println();

			// Unnook a room in nearest available hospital

			System.out.println();
			System.out.println("==================================");
			System.out.println("Test - UNBOOK A BED IN AN HOSPITAL");
			System.out.println("==================================");
			System.out.println();

			System.out.println(String.format("Hôpital réservé : %s", bookedHospital.getHospital_name()));
			System.out.println(String.format("Nombre de lits disponibles avant réservation : %d", bookedHospital.getFreeBeds()));
			System.out.println();

			Hospital unbookedHospital = hospitalService.unbookBed(bookedHospital);

			System.out.println(String.format("Nombre de lits disponibles après réservation : %d", unbookedHospital.getFreeBeds()));
		}
	}

}
