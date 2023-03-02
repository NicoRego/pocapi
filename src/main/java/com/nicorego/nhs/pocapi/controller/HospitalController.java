package com.nicorego.nhs.pocapi.controller;

import com.nicorego.nhs.pocapi.service.SpecialtyService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nicorego.nhs.pocapi.model.Hospital;
import com.nicorego.nhs.pocapi.repository.HospitalRepository;
import com.nicorego.nhs.pocapi.service.HospitalService;

@RestController
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private SpecialtyService specialtyService;

    @Autowired
    private HospitalRepository hospitalRepository;

    @GetMapping("/search/nearest")
    @ResponseBody
    public ResponseEntity<String> getNearestAvailableHospital(
            @ApiParam(value = "Latitude, longitude and specialty id are required", required = true)
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestParam("specialty") Integer specialtyId) {

        Hospital nearestAvailableHospital = this.hospitalService.getNearestAvailableHospital(latitude, longitude, specialtyId);

        if (nearestAvailableHospital != null) {
            try {
                String nearestAvailableHospitalString = nearestAvailableHospital.toString();
                return new ResponseEntity<>(nearestAvailableHospitalString, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(
                    String.format(
                            "No nearest/available hospital found for specialty '%d' @ %f.%f",
                            specialtyId,
                            latitude,
                            longitude),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/bed/booking")
    public ResponseEntity<String> bedBooking (
            @RequestParam("hospital") Integer hospitalId) {

        Hospital bookedHospital = this.hospitalRepository.findById(hospitalId).orElse(null);

        if (bookedHospital == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hospital not found");
        }

        try {
            if (hospitalService.bedBooking(bookedHospital)) {
                return ResponseEntity.ok("Room booked");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No free beds available");
            }

        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No free beds available");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @PutMapping("/bed/booking/cancel")
    public ResponseEntity<String> cancelBedBooking(
            @RequestParam("hospital") Integer hospitalId) {

        Hospital bookedHospital = this.hospitalRepository.findById(hospitalId).orElse(null);

        if (bookedHospital == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hospital not found");
        }

        try {
            hospitalService.cancelBedBooking(bookedHospital);
            return ResponseEntity.ok("Booking cancelled");
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to cancel booking");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }
}

