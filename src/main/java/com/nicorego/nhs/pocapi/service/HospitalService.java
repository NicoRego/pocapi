package com.nicorego.nhs.pocapi.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nicorego.nhs.pocapi.model.Hospital;
import com.nicorego.nhs.pocapi.repository.HospitalRepository;

import static com.nicorego.nhs.pocapi.utils.Distance.distanceHaversine;
import static com.nicorego.nhs.pocapi.utils.JsonMapper.getHospitalJson;

@Service
public class HospitalService {

	@Autowired
	private HospitalRepository hospitalRepository;

	@Autowired
	private SpecialtyService specialtyService;

	public Iterable<Hospital> getHospitals(){
		return this.hospitalRepository.findAll();
	}

	public Optional<Hospital> getHospitalById(Integer id) {
		return this.hospitalRepository.findById(id);
	}

	public Iterable<Hospital> getHospitalsBySpecialtyAndFreeBeds(Integer specialtyId, int minFreeBeds) {
		return this.hospitalRepository.findBySpecialtyAndFreeBeds(specialtyId, minFreeBeds);
	}
	public Iterable<Hospital> getHospitalsBySpecialty(int specialtyId) {
		return this.hospitalRepository.findBySpecialty(specialtyId);
	}

	public Hospital getNearestAvailableHospital(Double latitude, Double longitude, Integer specialtyId) {

		// Pre-Requisites
		if (latitude == 0 || longitude == 0) {
			return null;
		}
		if (this.specialtyService.getSpecialtyById(specialtyId).isEmpty()) {
			return null;
		}

		// Filter hospitals by specialty and free beds
		List<Hospital> filteredHospitals = this.hospitalRepository.findBySpecialtyAndFreeBeds(specialtyId, 0);

		// Check if some hospitals found
		if (filteredHospitals.size() == 0) {return null;}

		// List for distance and free beds
		ArrayList<Double> distanceHospitals = new ArrayList<>();
		// Get distance from latitude and longitude for each hospital in list
		for (Hospital filteredHospital : filteredHospitals) {
			double dist = distanceHaversine(latitude, longitude, filteredHospital.getLatitude(),
					filteredHospital.getLongitude());
			distanceHospitals.add(dist);
		}

		// Find and return index of the nearest hospital
		return filteredHospitals.get(distanceHospitals.indexOf(Collections.min(distanceHospitals)));
	}

	@Transactional
	public Hospital bedBooking(Hospital hospital) {

		// Decrease available beds
		if (hospital.getFreeBeds() > 0) {
			hospital.setFreeBeds(hospital.getFreeBeds() - 1);
			// Save hospital
			saveHospital(hospital);
		}

		return hospital;
	}

	@Transactional
	public boolean bedBookingById(Integer hospitalId) {

		// Set return boolean status
		boolean booked = false;

		// Get hospital
		Optional<Hospital> hospital = this.hospitalRepository.findById(hospitalId);

		if (hospital.isPresent()) {

			// Store locally free beds number
			int freeBeds = hospital.get().getFreeBeds();

			// Decrease available beds
			if (freeBeds > 0) {
				hospital.get().setFreeBeds(hospital.get().getFreeBeds() - 1);
				// Save hospital
				saveHospital(hospital.get());
				if (freeBeds == hospital.get().getFreeBeds()+1) {
					booked = true;
				}
			}

		}

		return booked;

	}

	@Transactional
	public Hospital cancelBedBooking(Hospital hospital) {

		// Increase available beds
		hospital.setFreeBeds(hospital.getFreeBeds() + 1);
		// Save hospital
		saveHospital(hospital);

		return hospital;
	}

	@Transactional
	public boolean cancelBedBookingById(Integer hospitalId) {

		Integer storedFreeBeds;
		boolean cancelled = false;

		// Get hospital
		Hospital hospital = this.hospitalRepository.findById(hospitalId).orElse(null);

		if (hospital == null) {
			return false;
		}

		// Store locally free beds number
		try {
			storedFreeBeds = hospital.getFreeBeds();
		} catch (Exception e){
			return false;
		}

		// Increase available beds
		hospital.setFreeBeds(hospital.getFreeBeds() + 1);

		// Save hospital
		saveHospital(hospital);
		if (hospital.getFreeBeds() == (storedFreeBeds + 1)) {
			cancelled = true;
		}

		return cancelled;
	}

	public Hospital saveHospital(Hospital hospital) {

		return this.hospitalRepository.save(hospital);
	}


}
