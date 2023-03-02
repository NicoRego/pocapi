package com.nicorego.nhs.pocapi.controller;

import com.nicorego.nhs.pocapi.model.Hospital;
import com.nicorego.nhs.pocapi.repository.HospitalRepository;
import com.nicorego.nhs.pocapi.service.HospitalService;
import com.nicorego.nhs.pocapi.service.SpecialtyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(HospitalController.class)
public class HospitalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HospitalService hospitalService;

    @MockBean
    private SpecialtyService specialtyService;

    @MockBean
    private HospitalRepository hospitalRepository;

    @Test
    void contextLoads() {
    }

    @Test
    public void testGetNearestAvailableHospital() throws Exception {

        Double latitude = 50.616312;
        Double longitude = 3.051438;
        Integer specialtyId = 1;

        Hospital hospital = new Hospital();

        hospital.setIdhospital(1L);
        // hospital.setHospitalName("Hopital Saint Vincent de Paul");
        hospital.setHospitalName("Un nom");
        hospital.setLatitude(50.620312);
        hospital.setLongitude(3.077438);
        hospital.setFreeBeds(2);

        given(hospitalService.getNearestAvailableHospital(latitude, longitude, specialtyId))
                .willReturn(hospital);

        mockMvc.perform(get("/search/nearest")
                        .param("latitude", latitude.toString())
                        .param("longitude", longitude.toString())
                        .param("specialty", specialtyId.toString())
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("Hospital(idhospital=1, hospital_name=Un autre nom, latitude=50.620312, longitude=3.077438, freeBeds=2)"));
        // .andExpect(content().string("Hospital(idhospital=1, hospital_name=Hopital Saint Vincent de Paul, latitude=50.620312, longitude=3.077438, freeBeds=2)"));

    }

    @Test
    public void testGetNearestAvailableHospital2() throws Exception {
        Double latitude = 50.616312;
        Double longitude = 3.051438;
        Integer specialtyId = 4;

        Hospital hospital = new Hospital();

        given(hospitalService.getNearestAvailableHospital(latitude, longitude, specialtyId))
                .willReturn(hospital);


        mockMvc.perform(get("/search/nearest")
                        .param("latitude", latitude.toString())
                        .param("longitude", longitude.toString())
                        .param("specialty", specialtyId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("No nearest/available hospital found for specialty '4' @ 50,616312.3,051438"));
    }

    @Test
    public void testGetNearestAvailableHospital3() throws Exception {
        Double latitude = 50.616312;
        Double longitude = 3.051438;
        String specialtyId = "";

        Hospital hospital = new Hospital();

        given(hospitalService.getNearestAvailableHospital(latitude, longitude, Integer.valueOf(specialtyId)))
                .willReturn(hospital);


        mockMvc.perform(get("/search/nearest")
                        .param("latitude", latitude.toString())
                        .param("longitude", longitude.toString())
                        .param("specialty", specialtyId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(String.format("No nearest/available hospital found for specialty '%s' @ %f.%f", specialtyId, latitude, longitude)));
        }

    @Test
    public void testGetNearestAvailableHospital4() throws Exception {
        Double latitude = 50.616312;
        Double longitude = 3.051438;
        Integer specialtyId = 3;

        Hospital hospital = new Hospital();

        given(hospitalService.getNearestAvailableHospital(latitude, longitude, specialtyId))
                .willReturn(hospital);


        mockMvc.perform(get("/search/nearest")
                        .param("latitude", latitude.toString())
                        .param("longitude", longitude.toString())
                        .param("specialty", specialtyId.toString())
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("Hospital(idhospital=3, hospital_name=Hôpital privé La Louvière, latitude=50.646438, longitude=3.083563, freeBeds=5)"));
    }

    @Test
    public void testGetNearestAvailableHospital_invalidInput() throws Exception {
        mockMvc.perform(get("/search/nearest"))
                .andExpect(status().isBadRequest());
    }

     @Test
    void bedBooking() {
    }

    @Test
    void cancelBedBooking() {
    }

}