package com.nicorego.nhs.pocapi.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nicorego.nhs.pocapi.model.Hospital;
import com.nicorego.nhs.pocapi.service.HospitalService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.nicorego.nhs.pocapi.utils.JsonMapper.getHospitalJson;

@RestController
public class HospitalController {

    private final HospitalService hospitalService;

    @Autowired
    public HospitalController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @GetMapping("/search/nearest")
    @ResponseBody
    public ResponseEntity<ObjectNode> getNearestAvailableHospital(
            @ApiParam(value = "Latitude, longitude and specialty id are required", required = true)
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestParam("specialty") Integer specialtyId) {

        // Get nearest available hospital
        Hospital nearestAvailableHospital = this.hospitalService.getNearestAvailableHospital(latitude, longitude, specialtyId);

        // Check if a hospital has been found
        if (nearestAvailableHospital == null) {
            Hospital notFoundHospital = new Hospital();
            notFoundHospital.setContextMessage(String.format("No nearest/available hospital found for specialty '%d' @ %s,%s", specialtyId, latitude.toString(), longitude.toString()));
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
    @ResponseBody
    public ResponseEntity<ObjectNode> bedBooking (
            @RequestParam("hospital") Integer hospitalId) {

        // Select hospital
        Optional<Hospital> selectedHospital = this.hospitalService.getHospitalById(hospitalId);

        if (selectedHospital.isEmpty()) {
            Hospital bookedHospital = new Hospital();

            bookedHospital.setContextMessage(String.format("Hospital %d not found", hospitalId));
            ObjectNode bookedHospitalJson = getHospitalJson(bookedHospital);
            return new ResponseEntity<>(bookedHospitalJson,HttpStatus.NOT_FOUND);
        }

        // Get free beds number
        int freeBeds = selectedHospital.get().getFreeBeds();

        // Book a bed
        Hospital resultHospital = hospitalService.bedBooking(selectedHospital.get());

        if ((resultHospital.getFreeBeds() + 1) == freeBeds){
            try {
                resultHospital.setContextMessage(String.format("Bed booked successfully in hospital %d", hospitalId));
                ObjectNode bookedHospitalJson = getHospitalJson(resultHospital);
                return new ResponseEntity<>(bookedHospitalJson, HttpStatus.OK);
            } catch (Exception e) {
                resultHospital.setContextMessage(e.getMessage() + " - " + e.getClass());
                ObjectNode bookedHospitalJson = getHospitalJson(selectedHospital.get());
                return new ResponseEntity<>(bookedHospitalJson, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            if (resultHospital.getFreeBeds() == 0) {
                resultHospital.setContextMessage(String.format("No bed available in Hospital %d", hospitalId));
            } else {
                resultHospital.setContextMessage(String.format("Unable to book a bed in Hospital %d", hospitalId));
            }
            ObjectNode bookedHospitalJson = getHospitalJson(resultHospital);
            return new ResponseEntity<>(bookedHospitalJson,HttpStatus.OK);
        }
    }

    @PutMapping("/bed/booking/cancel")
    @ResponseBody
    public ResponseEntity<ObjectNode> cancelBedBooking(
            @RequestParam("hospital") Integer hospitalId) {

        // Select hospital
        Optional<Hospital> selectedHospital = this.hospitalService.getHospitalById(hospitalId);

        //if (selectedHospital.isEmpty() || selectedHospital.get().getId() == null) {
        if (selectedHospital.isEmpty()) {
            Hospital cancelBookedHospital = new Hospital();

            cancelBookedHospital.setContextMessage(String.format("Hospital %d not found", hospitalId));
            ObjectNode cancelBookedHospitalJson = getHospitalJson(cancelBookedHospital);
            return new ResponseEntity<>(cancelBookedHospitalJson,HttpStatus.NOT_FOUND);
        }

        // Get free beds number
        int freeBeds = selectedHospital.get().getFreeBeds();

        // Cancel booking
        Hospital cancelBookedHospital = hospitalService.cancelBedBooking(selectedHospital.get());

        if ((cancelBookedHospital.getFreeBeds() - 1) == freeBeds) {
            try {
                cancelBookedHospital.setContextMessage(String.format("Booking cancelled successfully in hospital %d", hospitalId));
                ObjectNode cancelBookedHospitalJson = getHospitalJson(cancelBookedHospital);
                return new ResponseEntity<>(cancelBookedHospitalJson, HttpStatus.OK);
            } catch (Exception e) {
                cancelBookedHospital.setContextMessage(e.getMessage() + " - " + e.getClass());
                ObjectNode cancelBookedHospitalJson = getHospitalJson(cancelBookedHospital);
                return new ResponseEntity<>(cancelBookedHospitalJson, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            cancelBookedHospital.setContextMessage(String.format("Unable to cancel booking in Hospital %d", hospitalId));
            ObjectNode cancelBookedHospitalJson = getHospitalJson(cancelBookedHospital);
            return new ResponseEntity<>(cancelBookedHospitalJson,HttpStatus.OK);
        }
    }

    @GetMapping("/search/hospital")
    @ResponseBody
    public ResponseEntity<ObjectNode> getHospital(@RequestParam("id") Integer hospitalId) {

        // Select hospital
        Optional<Hospital> selectedHospital = this.hospitalService.getHospitalById(hospitalId);

        if (selectedHospital.isEmpty()) {
            Hospital searchHospital = new Hospital();
            searchHospital.setContextMessage(String.format("Hospital %d not found", hospitalId));
            ObjectNode searchHospitalJson = getHospitalJson(searchHospital);
            return new ResponseEntity<>(searchHospitalJson, HttpStatus.NOT_FOUND);
        } else {
            selectedHospital.get().setContextMessage(String.format("Hospital %d found successfully", hospitalId));
            ObjectNode searchHospitalJson = getHospitalJson(selectedHospital.get());
            return new ResponseEntity<>(searchHospitalJson, HttpStatus.OK);
        }
    }
}

