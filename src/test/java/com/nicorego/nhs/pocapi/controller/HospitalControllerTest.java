package com.nicorego.nhs.pocapi.controller;

import com.nicorego.nhs.pocapi.model.Hospital;
import com.nicorego.nhs.pocapi.repository.HospitalRepository;
import com.nicorego.nhs.pocapi.service.HospitalService;
import com.nicorego.nhs.pocapi.service.SpecialtyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest(HospitalController.class)
public class HospitalControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private HospitalService hospitalService;

    @MockBean
    private SpecialtyService specialtyService;

    // This object will be initialized thanks to @AutoConfigureJsonTesters
    @MockBean
    private HospitalRepository hospitalRepository;

    @Autowired
    private JacksonTester<Hospital> jsonHospital;

    @Test
    void contextLoads() {
    }

    @Test
    public void testGetNearestAvailableHospital_WillReturnOKWithId1() throws Exception {

        // Set request attributes
        Double searchLatitude = 50.616312;
        Double searchLongitude = 3.051438;
        Integer searchSpecialtyId = 1;

        // Set response attributes
        Long responseId = 1L;
        String responseName = "Hopital Saint Vincent de Paul";
        Double responseLatitude = 50.620312;
        Double responseLongitude = 3.077438;
        Integer responseFreeBeds = 2;

        // Given
        Hospital givenHospital = new Hospital();

        givenHospital.setId(responseId);
        givenHospital.setName(responseName);
        givenHospital.setLatitude(responseLatitude);
        givenHospital.setLongitude(responseLongitude);
        givenHospital.setFreeBeds(responseFreeBeds);

        given(hospitalService.getNearestAvailableHospital(searchLatitude, searchLongitude, searchSpecialtyId))
                .willReturn(givenHospital);

        // When
        MockHttpServletResponse response = mvc.perform(
                        get(String.format("/search/nearest?latitude=%f&longitude=%f&specialtyid=%d", searchLatitude, searchLongitude, searchSpecialtyId))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then
        Hospital thenHospital = new Hospital();

        thenHospital.setId(responseId);
        thenHospital.setName(responseName);
        thenHospital.setLatitude(responseLatitude);
        thenHospital.setLongitude(responseLongitude);
        thenHospital.setFreeBeds(responseFreeBeds);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonHospital.write(thenHospital).getJson()
        );

    }

    @Test
    public void testGetNearestAvailableHospital_WillReturnNotFound() throws Exception {

        // Set request attributes
        Double searchLatitude = 50.616312;
        Double searchLongitude = 3.051438;
        Integer searchSpecialtyId = 4;

        // Set response attributes
        String responseText = String.format("No nearest/available hospital found for specialty '%d' @ %f,%f",searchSpecialtyId,searchLatitude, searchLongitude );

        // Given
        Hospital givenHospital = new Hospital();

        given(hospitalService.getNearestAvailableHospital(searchLatitude, searchLongitude, searchSpecialtyId))
                .willReturn(givenHospital);

        // When
        MockHttpServletResponse response = mvc.perform(
                        get(String.format("/search/nearest?latitude=%f&longitude=%f&specialtyid=%d", searchLatitude, searchLongitude, searchSpecialtyId))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).isEqualTo(responseText);

    }

    @Test
    public void testGetNearestAvailableHospital_WillReturnBadRequest() throws Exception {

        // Set request attributes
        Double searchLatitude = 50.616312;
        Double searchLongitude = 3.051438;
        Integer searchSpecialtyId = null;
        // Integer searchSpecialtyId = Integer.valueOf("");

        // Given
        Hospital givenHospital = new Hospital();

        given(hospitalService.getNearestAvailableHospital(searchLatitude, searchLongitude, searchSpecialtyId))
                .willReturn(givenHospital);

        // When
        MockHttpServletResponse response = mvc.perform(
                        get(String.format("/search/nearest?latitude=%f&longitude=%f&specialtyid=%d", searchLatitude, searchLongitude, searchSpecialtyId))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    public void testGetNearestAvailableHospital_WillReturnOKWithId3() throws Exception {

        // Set request attributes
        Double searchLatitude = 50.616312;
        Double searchLongitude = 3.051438;
        Integer searchSpecialtyId = 3;

        // Set response attributes
        Long responseId = 3L;
        String responseName = "Hopital privé La Louvière";
        Double responseLatitude = 50.646438;
        Double responseLongitude = 3.083563;
        Integer responseFreeBeds = 5;

        // Given
        Hospital givenHospital = new Hospital();

        givenHospital.setId(responseId);
        givenHospital.setName(responseName);
        givenHospital.setLatitude(responseLatitude);
        givenHospital.setLongitude(responseLongitude);
        givenHospital.setFreeBeds(responseFreeBeds);

        given(hospitalService.getNearestAvailableHospital(searchLatitude, searchLongitude, searchSpecialtyId))
                .willReturn(givenHospital);

        // When
        MockHttpServletResponse response = mvc.perform(
                        get(String.format("/search/nearest?latitude=%f&longitude=%f&specialtyid=%d", searchLatitude, searchLongitude, searchSpecialtyId))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then
        Hospital thenHospital = new Hospital();

        thenHospital.setId(responseId);
        thenHospital.setName(responseName);
        thenHospital.setLatitude(responseLatitude);
        thenHospital.setLongitude(responseLongitude);
        thenHospital.setFreeBeds(responseFreeBeds);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonHospital.write(thenHospital).getJson()
        );

    }

    @Test
    public void testGetNearestAvailableHospital_invalidInput() throws Exception {
        mvc.perform(get("/search/nearest"))
                .andExpect(status().isBadRequest());
    }

     @Test
    void bedBooking() {
    }

    @Test
    void cancelBedBooking() {
    }

}