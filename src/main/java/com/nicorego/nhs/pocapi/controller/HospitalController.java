package com.nicorego.nhs.pocapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nicorego.nhs.pocapi.model.Hospital;
import com.nicorego.nhs.pocapi.repository.HospitalRepository;
import com.nicorego.nhs.pocapi.service.HospitalService;

@RestController
public class HospitalController {

    final private HospitalService hospitalService;
    final private HospitalRepository hospitalRepository;

    @Autowired
    public HospitalController(HospitalService hospitalService, HospitalRepository hospitalRepository) {
        this.hospitalService = hospitalService;
        this.hospitalRepository = hospitalRepository;
    }

    @GetMapping("/search/nearest")
    @ResponseBody
    public ResponseEntity<Hospital> getNearestAvailableHospital(@RequestParam("latitude") Double latitude,
                                                                @RequestParam("longitude") Double longitude,
                                                                @RequestParam("specialty") Integer specialtyId) {

        Hospital nearestHospital = hospitalService.getNearestAvailableHospital(latitude, longitude, specialtyId);

        // Build header
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.setCacheControl("no-cache");

        // Return response
        if (nearestHospital == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).headers(header).body(nearestHospital);
        }
    }

    @PutMapping("/room/book_bed")
    @ResponseBody
    public ResponseEntity<String> bookBed(@RequestParam Integer hospitalId) {
        Hospital bookedHospital = hospitalRepository.findById(hospitalId).orElse(null);
        if (bookedHospital == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hospital not found");
        }

        try {
            hospitalService.bookBed(bookedHospital);
            return ResponseEntity.ok("Room booked");
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No free beds available");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

}

