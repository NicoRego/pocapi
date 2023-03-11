package com.nicorego.nhs.pocapi.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nicorego.nhs.pocapi.model.Hospital;
import com.nicorego.nhs.pocapi.repository.HospitalRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Optional;

import static com.nicorego.nhs.pocapi.utils.JsonMapper.getHospitalJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
public class HospitalControllerTestPUT {

    private final MockMvc mvc;

    //@InjectMocks
    // private final HospitalService hospitalService;

    @MockBean
    private HospitalRepository hospitalRepository = null;

    @Autowired
    public HospitalControllerTestPUT(MockMvc mvc, HospitalRepository hospitalRepository) {
        this.mvc = mvc;
        //this.hospitalService = hospitalService;
        this.hospitalRepository = hospitalRepository;
    }

    // *****************************************************************************
    // **                                                                         **
    // **                            bedBooking  tests                            **
    // **                                                                         **
    // *****************************************************************************


    @Test
    public void bedBooking_WillReturnOk() throws Exception {

        // Given
        Hospital givenHospital = new Hospital();

        givenHospital.setId(3);
        givenHospital.setName("Hopital prive La Louviere");
        givenHospital.setLatitude(50.646438);
        givenHospital.setLongitude(3.083563);
        givenHospital.setFreeBeds(5);
        givenHospital.setContextMessage("");

        Mockito.when(this.hospitalRepository.findById(3)).thenReturn(Optional.of(givenHospital));

        // Set expected json object
        Hospital responseHospital = new Hospital();

        responseHospital.setId(3);
        responseHospital.setName("Hopital prive La Louviere");
        responseHospital.setLatitude(50.646438);
        responseHospital.setLongitude(3.083563);
        responseHospital.setFreeBeds(4);
        responseHospital.setContextMessage("Bed booked successfully in hospital 3");

        ObjectNode responseHospitalJson = getHospitalJson(responseHospital);
        String responseHospitalString = responseHospitalJson.toString();

        // When
        String url = "/bed/booking?hospital=3";

        MockHttpServletRequestBuilder requestBuilder = put(url);

        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(responseHospitalString))
                .andReturn();

        MockHttpServletResponse response = result.getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(responseHospitalString);

     }

    @Test
    public void bedBooking_NoBedAvailable() throws Exception {

        // Given
        Hospital givenHospital = new Hospital();

        givenHospital.setId(2);
        givenHospital.setName("Centre Hospitalier Universitaire de Lille");
        givenHospital.setLatitude(50.610937);
        givenHospital.setLongitude(3.034687);
        givenHospital.setFreeBeds(0);
        givenHospital.setContextMessage("");

        Mockito.when(this.hospitalRepository.findById(2)).thenReturn(Optional.of(givenHospital));

        // Set expected json object
        Hospital responseHospital = new Hospital();

        responseHospital.setId(2);
        responseHospital.setName("Centre Hospitalier Universitaire de Lille");
        responseHospital.setLatitude(50.610937);
        responseHospital.setLongitude(3.034687);
        responseHospital.setFreeBeds(0);
        responseHospital.setContextMessage("No bed available in Hospital 2");

        ObjectNode responseHospitalJson = getHospitalJson(responseHospital);
        String responseHospitalString = responseHospitalJson.toString();

        // When
        String url = "/bed/booking?hospital=2";

        MockHttpServletRequestBuilder requestBuilder = put(url);

        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(responseHospitalString))
                .andReturn();

        MockHttpServletResponse response = result.getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(responseHospitalString);

    }

    @Test
    public void bedBooking_invalidInput() throws Exception {
        mvc.perform(put("/bed/booking"))
                .andExpect(status().isBadRequest());
    }
    // *****************************************************************************
    // **                                                                         **
    // **                         cancelBedBooking  tests                         **
    // **                                                                         **
    // *****************************************************************************

    @Test
    public void cancelBedBooking_WillReturnOk() throws Exception {

        // Given
        Hospital givenHospital = new Hospital();

        givenHospital.setId(3);
        givenHospital.setName("Hopital prive La Louviere");
        givenHospital.setLatitude(50.646438);
        givenHospital.setLongitude(3.083563);
        givenHospital.setFreeBeds(4);
        givenHospital.setContextMessage("");

        Mockito.when(this.hospitalRepository.findById(3)).thenReturn(Optional.of(givenHospital));

        // Set expected json object
        Hospital responseHospital = new Hospital();

        responseHospital.setId(3);
        responseHospital.setName("Hopital prive La Louviere");
        responseHospital.setLatitude(50.646438);
        responseHospital.setLongitude(3.083563);
        responseHospital.setFreeBeds(5);
        responseHospital.setContextMessage("Booking cancelled successfully in hospital 3");

        ObjectNode responseHospitalJson = getHospitalJson(responseHospital);
        String responseHospitalString = responseHospitalJson.toString();

        // When
        String url = "/bed/booking/cancel?hospital=3";

        MockHttpServletRequestBuilder requestBuilder = put(url);

        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(responseHospitalString))
                .andReturn();

        MockHttpServletResponse response = result.getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(responseHospitalString);

    }

    @Test
    public void cancelBedBooking_WillReturnNotFound() throws Exception {

        // Given
        Hospital givenHospital = new Hospital();

        // Set expected json object
        Hospital responseHospital = new Hospital();

        responseHospital.setId(0);
        responseHospital.setName("");
        responseHospital.setLatitude(0.0);
        responseHospital.setLongitude(0.0);
        responseHospital.setFreeBeds(0);
        responseHospital.setContextMessage("Hospital 4 not found");

        String responseHospitalJsonString = getHospitalJson(responseHospital).toString();

        Mockito.when(this.hospitalRepository.findById(4)).thenReturn(Optional.of(givenHospital));

        // When
        String url = "/bed/booking/cancel?hospital=4";

        MockHttpServletRequestBuilder requestBuilder = put(url);

        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(content().json(responseHospitalJsonString))
                .andReturn();

        MockHttpServletResponse response = result.getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).isEqualTo(responseHospitalJsonString);

    }
}