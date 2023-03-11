package com.nicorego.nhs.pocapi.controller;

import com.nicorego.nhs.pocapi.model.Hospital;
import com.nicorego.nhs.pocapi.repository.HospitalRepository;
import com.nicorego.nhs.pocapi.service.HospitalService;
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
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
public class HospitalControllerTestGET {

    private final MockMvc mvc;

    @MockBean
    private final HospitalService hospitalService;

    @MockBean
    private HospitalRepository hospitalRepository = null;

    @Autowired
    public HospitalControllerTestGET(MockMvc mvc, HospitalService hospitalService, HospitalRepository hospitalRepository) {
        this.mvc = mvc;
        this.hospitalService = hospitalService;
        this.hospitalRepository = hospitalRepository;
    }

    // *****************************************************************************
    // **                                                                         **
    // **                    getNearestAvailableHospital tests                    **
    // **                                                                         **
    // *****************************************************************************

    @Test
    public void getNearestAvailableHospital_WillReturnOKWithId1() throws Exception {

        // Set request attributes
        String searchLatitude = "50.616312";
        String searchLongitude = "3.051438";
        Integer searchSpecialtyId = 1;

        // Given
        Hospital givenHospital = new Hospital();

        givenHospital.setId(1);
        givenHospital.setName("Hopital Saint Vincent de Paul");
        givenHospital.setLatitude(50.620312);
        givenHospital.setLongitude(3.077438);
        givenHospital.setFreeBeds(2);
        givenHospital.setContextMessage("");

        // Given
        given(hospitalService.getNearestAvailableHospital(Double.parseDouble(searchLatitude), Double.parseDouble(searchLongitude), searchSpecialtyId))
                .willReturn(givenHospital);

        // Set expected json object
        String responseHospitalJsonString = getHospitalJson(givenHospital).toString();

        // When
        String url = String.format("/search/nearest?latitude=%S&longitude=%s&specialty=%d", searchLatitude, searchLongitude, searchSpecialtyId);

        MockHttpServletRequestBuilder requestBuilder = get(url);

        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(responseHospitalJsonString))
                .andReturn();

        MockHttpServletResponse response = result.getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(responseHospitalJsonString);

    }

    @Test
    public void getNearestAvailableHospital_WillReturnNotFound() throws Exception {

        // Set request attributes
        String searchLatitude = "50.616312";
        String searchLongitude = "3.051438";
        Integer searchSpecialtyId = 4;

        // Set response attributes
        Hospital notFoundHospital = new Hospital();
        notFoundHospital.setContextMessage(String.format("No nearest/available hospital found for specialty '%d' @ %s,%s", searchSpecialtyId, searchLatitude, searchLongitude));
        String responseHospitalJsonString = getHospitalJson(notFoundHospital).toString();

        // Given
        given(hospitalService.getNearestAvailableHospital(Double.parseDouble(searchLatitude), Double.parseDouble(searchLongitude), searchSpecialtyId))
                .willReturn(null);

        // When
        String url = String.format("/search/nearest?latitude=%s&longitude=%s&specialty=%d", searchLatitude, searchLongitude, searchSpecialtyId);

        MockHttpServletRequestBuilder requestBuilder = get(url);

        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(content().json(responseHospitalJsonString))
                .andReturn();

        // Then
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(result.getResponse().getContentAsString()).isEqualTo(responseHospitalJsonString);

    }

    @Test
    public void getNearestAvailableHospital_WillReturnOKWithId3() throws Exception {

        // Set request attributes
        String searchLatitude = "50.616312";
        String searchLongitude = "3.051438";
        Integer searchSpecialtyId = 3;

        // Set expected json object
        Hospital responseHospital = new Hospital();

        responseHospital.setId(3);
        responseHospital.setName("Hopital prive La Louviere");
        responseHospital.setLatitude(50.646438);
        responseHospital.setLongitude(3.083563);
        responseHospital.setFreeBeds(5);
        responseHospital.setContextMessage("");

        String responseHospitalJsonString = getHospitalJson(responseHospital).toString();

        given(hospitalService.getNearestAvailableHospital(Double.parseDouble(searchLatitude), Double.parseDouble(searchLongitude), searchSpecialtyId))
                .willReturn(responseHospital);

        String url = String.format("/search/nearest?latitude=%s&longitude=%s&specialty=%d", searchLatitude, searchLongitude, searchSpecialtyId);

        MockHttpServletRequestBuilder requestBuilder = get(url);

        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json(responseHospitalJsonString))
                .andReturn();

        MockHttpServletResponse response = result.getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(responseHospitalJsonString);

    }

    @Test
    public void getNearestAvailableHospital_WillReturnBadRequest() throws Exception {

        // Set request attributes
        String searchLatitude = "50.616312";
        String searchLongitude = "3.051438";

        // Set expected json object
        Hospital responseHospital = new Hospital();

        // Given
        given(this.hospitalService.getNearestAvailableHospital(Double.parseDouble(searchLatitude), Double.parseDouble(searchLongitude),null))
                .willReturn(responseHospital);

        // When
        String url = String.format("/search/nearest?latitude=.%s&longitude=%s&specialty=", searchLatitude, searchLongitude);

        MockHttpServletRequestBuilder requestBuilder = get(url);

        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andReturn();

        // Then
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    // *****************************************************************************
    // **                                                                         **
    // **                            getHospital tests                            **
    // **                                                                         **
    // *****************************************************************************

    @Test
    void getHospital_WillReturnOK() throws Exception {

        // Given
        Hospital givenHospital = new Hospital();

        givenHospital.setId(3);
        givenHospital.setName("Hopital prive La Louviere");
        givenHospital.setLatitude(50.646438);
        givenHospital.setLongitude(3.083563);
        givenHospital.setFreeBeds(5);
        givenHospital.setContextMessage("Hospital 3 found successfully");

        String responseHospitalJsonString = getHospitalJson(givenHospital).toString();

        given(hospitalService.getHospitalById(3)).willReturn(Optional.of(givenHospital));

        // When
        String url = "/search/hospital?id=3";

        MockHttpServletRequestBuilder requestBuilder = get(url);

        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        // Then
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getResponse().getContentAsString()).isEqualTo(responseHospitalJsonString);

    }

    @Test
    void getHospital_WillReturnNotFound() throws Exception {

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
        String url = "/search/hospital?id=4";

        MockHttpServletRequestBuilder requestBuilder = get(url);

        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andReturn();

        // Then
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(result.getResponse().getContentAsString()).isEqualTo(responseHospitalJsonString);

    }
}