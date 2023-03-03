package com.nicorego.nhs.pocapi.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nicorego.nhs.pocapi.model.Hospital;
import com.nicorego.nhs.pocapi.repository.HospitalRepository;
import com.nicorego.nhs.pocapi.service.HospitalService;
import com.nicorego.nhs.pocapi.service.SpecialtyService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.nicorego.nhs.pocapi.utils.JsonMapper.getHospitalJson;

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
    public ResponseEntity<ObjectNode> getNearestAvailableHospital(
            @ApiParam(value = "Latitude, longitude and specialty id are required", required = true)
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestParam("specialty") Integer specialtyId) {

        // Get nearest available hospital
        Hospital nearestAvailableHospital = this.hospitalService.getNearestAvailableHospital(latitude, longitude, specialtyId);

        // Check if an hospital has been found
        if (nearestAvailableHospital == null) {
            Hospital notFoundHospital = new Hospital();
            notFoundHospital.setContextMessage(String.format("No nearest/available hospital found for specialty '%d' @ %f,%f",specialtyId,latitude,longitude));
            ObjectNode notFoundHospitalJson = getHospitalJson(notFoundHospital);
            return new ResponseEntity<>(notFoundHospitalJson,HttpStatus.NOT_FOUND);
        } else {
            try {
                ObjectNode nearestAvailableHospitalJson = getHospitalJson(nearestAvailableHospital);
                return new ResponseEntity<>(nearestAvailableHospitalJson, HttpStatus.OK);
            } catch (Exception e) {
                ObjectNode nearestAvailableHospitalJson = getHospitalJson(new Hospital());
                return new ResponseEntity<>(nearestAvailableHospitalJson, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @PutMapping("/bed/booking")
    public ResponseEntity<ObjectNode> bedBooking (
            @RequestParam("hospital") Integer hospitalId) {

        // Select hospital
        Hospital selectedHospital = this.hospitalRepository.findById(hospitalId).orElse(null);

        if (selectedHospital == null) {
            Hospital bookedHospital = new Hospital();
            bookedHospital.setContextMessage(String.format("Hospital %d not found", hospitalId));
            ObjectNode bookedHospitalJson = getHospitalJson(bookedHospital);
            return new ResponseEntity<>(bookedHospitalJson,HttpStatus.NOT_FOUND);
        }

        // Get free beds number
        int freeBeds = selectedHospital.getFreeBeds();

        // Book a bed
        hospitalService.bedBooking(selectedHospital);

        if ((selectedHospital.getFreeBeds() + 1) == freeBeds){
            try {
                selectedHospital.setContextMessage(String.format("Bed booked successfully in hospital %d", hospitalId));
                ObjectNode bookedHospitalJson = getHospitalJson(selectedHospital);
                return new ResponseEntity<>(bookedHospitalJson, HttpStatus.OK);
            } catch (Exception e) {
                selectedHospital.setContextMessage(e.getMessage());
                ObjectNode bookedHospitalJson = getHospitalJson(selectedHospital);
                return new ResponseEntity<>(bookedHospitalJson, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            if (selectedHospital.getFreeBeds() == 0) {
                selectedHospital.setContextMessage(String.format("No bed available in Hospital %d", hospitalId));
            } else {
                selectedHospital.setContextMessage(String.format("Unable to book a bed in Hospital %d", hospitalId));
            }
            ObjectNode bookedHospitalJson = getHospitalJson(selectedHospital);
            return new ResponseEntity<>(bookedHospitalJson,HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/bed/booking/cancel")
    public ResponseEntity<ObjectNode> cancelBedBooking(
            @RequestParam("hospital") Integer hospitalId) {

        // Select hospital
        Hospital selectedHospital = this.hospitalRepository.findById(hospitalId).orElse(null);

        if (selectedHospital == null) {
            Hospital cancelBookedHospital = new Hospital();
            cancelBookedHospital.setContextMessage(String.format("Hospital %d not found", hospitalId));
            ObjectNode cancelBookedHospitalJson = getHospitalJson(cancelBookedHospital);
            return new ResponseEntity<>(cancelBookedHospitalJson,HttpStatus.NOT_FOUND);
        }

        // Get free beds number
        int freeBeds = selectedHospital.getFreeBeds();

        // Cancel booking
        hospitalService.cancelBedBooking(selectedHospital);

        if ((selectedHospital.getFreeBeds() - 1) == freeBeds) {
            try {
                selectedHospital.setContextMessage(String.format("Booking cancelled successfully in hospital %d", hospitalId));
                ObjectNode cancelBookedHospitalJson = getHospitalJson(selectedHospital);
                return new ResponseEntity<>(cancelBookedHospitalJson, HttpStatus.OK);
            } catch (Exception e) {
                selectedHospital.setContextMessage(e.getMessage());
                ObjectNode cancelBookedHospitalJson = getHospitalJson(selectedHospital);
                return new ResponseEntity<>(cancelBookedHospitalJson, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            selectedHospital.setContextMessage(String.format("Unable cancel booking in Hospital %d", hospitalId));
            ObjectNode cancelBookedHospitalJson = getHospitalJson(selectedHospital);
            return new ResponseEntity<>(cancelBookedHospitalJson,HttpStatus.OK);
        }
    }

}

