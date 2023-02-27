package com.nicorego.nhs.pocapi.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nicorego.nhs.pocapi.model.Hospital;
import com.nicorego.nhs.pocapi.repository.HospitalRepository;

import static com.nicorego.nhs.pocapi.business.Distance.distanceHaversine;

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

	public Iterable<Hospital> getHospitalsBySpecialtyAndFreeBeds(int specialtyId, int minFreeBeds) {
		return hospitalRepository.findBySpecialtyAndFreeBeds(specialtyId, minFreeBeds);
	}
	public Iterable<Hospital> getHospitalsBySpecialty(int specialtyId) {
		return hospitalRepository.findBySpecialty(specialtyId);
	}

	public Hospital getNearestAvailableHospital(Double latitude, Double longitude, Integer specialtyId) {

		System.out.println("Processing request...");

		// List for distance and free beds
		ArrayList<Double> distanceHospitals = new ArrayList<>();
		System.out.println("New hospital list declared");

		// Filter hospitals by specialty and free beds
		List<Hospital> filteredHospitals = hospitalRepository.findBySpecialtyAndFreeBeds(specialtyId, 0);
		System.out.println("Hospitals filtered by specialty and free beds");

		// Get distance from latitude and longitude for each hospital in list
		for (Hospital filteredHospital : filteredHospitals) {
			double dist = distanceHaversine(latitude, longitude, filteredHospital.getLatitude(),
					filteredHospital.getLongitude());
			distanceHospitals.add(dist);
		}
		System.out.println("Distances calculated");

		// Find and return index of nearest hospital
		return filteredHospitals.get(distanceHospitals.indexOf(Collections.min(distanceHospitals)));
	}

	public Hospital bookBed(Hospital hospital) {

		// Decrease available beds
		if (hospital.getFreeBeds() > 0) {
			hospital.setFreeBeds(hospital.getFreeBeds() - 1);

			// Save hospital
			saveHospital(hospital);
		}

		return hospital;
	}

	public Hospital unbookBed(Hospital hospital) {

		// Increase available beds
		hospital.setFreeBeds(hospital.getFreeBeds() + 1);

		// Save hospital
		saveHospital(hospital);

		return hospital;
	}

	public Hospital saveHospital(Hospital hospital) {

		return hospitalRepository.save(hospital);
	}


}
