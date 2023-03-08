package com.nicorego.nhs.pocapi.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nicorego.nhs.pocapi.model.Hospital;
import com.nicorego.nhs.pocapi.service.HospitalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Optional;

import static com.nicorego.nhs.pocapi.utils.JsonMapper.getHospitalJson;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.test.web.servlet.MvcResult;


@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
public class HospitalControllerTest {

    @Autowired
    private final MockMvc mvc;

    @Autowired
    @MockBean
    private final HospitalService hospitalService;

    @Autowired
    public HospitalControllerTest(MockMvc mvc, HospitalService hospitalService) {
        this.mvc = mvc;
        this.hospitalService = hospitalService;
    }

    @Test
    void contextLoads() {
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

        // Set expected json object
        Hospital responseHospital = new Hospital();

        responseHospital.setId(1);
        responseHospital.setName("Hopital Saint Vincent de Paul");
        responseHospital.setLatitude(50.620312);
        responseHospital.setLongitude(3.077438);
        responseHospital.setFreeBeds(2);
        responseHospital.setContextMessage("");

        ObjectNode responseHospitalJson = getHospitalJson(responseHospital);
        String responseHospitalString =responseHospitalJson.toString();

        // Given
        given(hospitalService.getNearestAvailableHospital(Double.parseDouble(searchLatitude), Double.parseDouble(searchLongitude), searchSpecialtyId))
                .willReturn(responseHospital);

        // When
        String url = String.format("/search/nearest?latitude=%S&longitude=%s&specialty=%d", searchLatitude, searchLongitude, searchSpecialtyId);

        MockHttpServletRequestBuilder requestBuilder = get(url);

        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andReturn();

        MockHttpServletResponse response = result.getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(responseHospitalString);

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
        ObjectNode notFoundHospitalJson = getHospitalJson(notFoundHospital);
        String responseHospitalString = notFoundHospitalJson.toString();

        // Given
        given(hospitalService.getNearestAvailableHospital(Double.parseDouble(searchLatitude), Double.parseDouble(searchLongitude), searchSpecialtyId))
                .willReturn(null);

        // When
        String url = String.format("/search/nearest?latitude=%s&longitude=%s&specialty=%d", searchLatitude, searchLongitude, searchSpecialtyId);

        MockHttpServletRequestBuilder requestBuilder = get(url);

        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.context_message").value(String.format("No nearest/available hospital found for specialty '%d' @ %s,%s", searchSpecialtyId, searchLatitude, searchLongitude)))
                .andReturn();

        // Then
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(result.getResponse().getContentAsString()).isEqualTo(responseHospitalString);

    }

    @Test
    public void getNearestAvailableHospital_WillReturnBadRequest() throws Exception {

        // Set request attributes
        String searchLatitude = "50.616312";
        String searchLongitude = "3.051438";
        Integer searchSpecialtyId = null;

        // Set expected object
        Hospital responseHospital = new Hospital();

        // Given
        given(hospitalService.getNearestAvailableHospital(Double.parseDouble(searchLatitude), Double.parseDouble(searchLongitude), searchSpecialtyId))
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

        ObjectNode responseHospitalJson = getHospitalJson(responseHospital);
        String responseHospitalString = responseHospitalJson.toString();

        given(hospitalService.getNearestAvailableHospital(Double.parseDouble(searchLatitude), Double.parseDouble(searchLongitude), searchSpecialtyId))
                .willReturn(responseHospital);

        String url = String.format("/search/nearest?latitude=%s&longitude=%s&specialty=%d", searchLatitude, searchLongitude, searchSpecialtyId);

        MockHttpServletRequestBuilder requestBuilder = get(url);

        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andReturn();

        MockHttpServletResponse response = result.getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(responseHospitalString);

    }

    @Test
    public void getNearestAvailableHospital_invalidInput() throws Exception {
        mvc.perform(get("/search/nearest"))
                .andExpect(status().isBadRequest());
    }

    // *****************************************************************************
    // **                                                                         **
    // **                            bedBooking  tests                            **
    // **                                                                         **
    // *****************************************************************************


    @Test
    public void bedBooking_WillReturnOk() throws Exception {

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

         // Given
         Hospital givenHospital = new Hospital();

         givenHospital.setId(3);
         givenHospital.setName("Hopital prive La Louviere");
         givenHospital.setLatitude(50.646438);
         givenHospital.setLongitude(3.083563);
         givenHospital.setFreeBeds(4);
         givenHospital.setContextMessage("");

         given(hospitalService.bedBooking(givenHospital)).willReturn(responseHospital);

         // When
         String url = "/bed/booking?hospital=3";

         MockHttpServletRequestBuilder requestBuilder = put(url);

         MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.free_beds").value(4))
                .andReturn();

         MockHttpServletResponse response = result.getResponse();

         // Then
         assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
         assertThat(response.getContentAsString()).isEqualTo(responseHospitalString);

     }

    @Test
    public void bedBooking2_WillReturnOk() throws Exception {

        MockHttpServletRequestBuilder requestBuilder = put("/bed/booking2?hospital=3");

        mvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.free_beds").value(4))
                .andReturn();

    }

    @Test
    public void bedBooking_NoBedAvailable() throws Exception {

        // Set attributes
        Integer givenId = 2;
        String givenName = "Centre Hospitalier Universitaire de Lille";
        Double givenLatitude = 50.610937;
        Double givenLongitude = 3.034687;
        Integer givenFreeBeds = 0;
        String responseContextMessage ="No bed available in Hospital 2";

        // Given
        Hospital givenHospital = new Hospital();

        givenHospital.setId(givenId);
        givenHospital.setName(givenName);
        givenHospital.setLatitude(givenLatitude);
        givenHospital.setLongitude(givenLongitude);
        givenHospital.setFreeBeds(givenFreeBeds);
        givenHospital.setContextMessage(responseContextMessage);

        String responseText = getHospitalJson(givenHospital).toString();

        given(hospitalService.bedBooking(givenHospital))
                .willReturn(givenHospital);

        // When
        String url = String.format("/bed/booking?hospital=%d", givenId);

        MockHttpServletResponse response = mvc.perform(put(url)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // Then
        assertThat(response
                .getStatus())
                .isEqualTo(HttpStatus.OK.value());
        assertThat(response
                .getContentAsString())
                .isEqualTo(responseText);

    }

    // *****************************************************************************
    // **                                                                         **
    // **                         cancelBedBooking  tests                         **
    // **                                                                         **
    // *****************************************************************************

    @Test
    public void cancelBedBooking_WillReturnOk() throws Exception {

        // Set attributes
        Integer givenId = 3;
        String givenName = "Hopital prive La Louviere";
        Double givenLatitude = 50.646438;
        Double givenLongitude = 3.083563;
        Integer givenFreeBeds = 4;
        Integer responseFreeBeds = 5;
        String responseContextMessage ="Bed booked successfully in hospital 3";

        // Set json object for response
        Hospital responseHospital = new Hospital();

        responseHospital.setId(givenId);
        responseHospital.setName(givenName);
        responseHospital.setLatitude(givenLatitude);
        responseHospital.setLongitude(givenLongitude);
        responseHospital.setFreeBeds(responseFreeBeds);
        responseHospital.setContextMessage(responseContextMessage);

        responseHospital.setFreeBeds(responseFreeBeds);
        String responseText = getHospitalJson(responseHospital).toString();

        // Given
        Hospital givenHospital = new Hospital();

        givenHospital.setId(givenId);
        givenHospital.setName(givenName);
        givenHospital.setLatitude(givenLatitude);
        givenHospital.setLongitude(givenLongitude);
        givenHospital.setFreeBeds(givenFreeBeds);
        givenHospital.setContextMessage("");

        given(hospitalService.bedBooking(givenHospital))
                .willReturn(responseHospital);

        // When
        String url = String.format("/bed/booking?hospital=%d", givenId);

        MockHttpServletResponse response = mvc.perform(put(url)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // Then
        assertThat(response
                .getStatus())
                .isEqualTo(HttpStatus.OK.value());
        assertThat(response
                .getContentAsString())
                .isEqualTo(responseText);

    }

    @Test
    void getHospital_WillReturnOK() throws Exception {

        // Set attributes
        Integer givenId = 3;

        // Given
        Hospital givenHospital = new Hospital();

        givenHospital.setId(givenId);
        givenHospital.setName("Hopital prive La Louviere");
        givenHospital.setLatitude(50.646438);
        givenHospital.setLongitude(3.083563);
        givenHospital.setFreeBeds(5);
        givenHospital.setContextMessage(String.format("Hospital %d found successfully", givenId));

        String responseText = getHospitalJson(givenHospital).toString();

        given(hospitalService.getHospitalById(givenId))
                .willReturn(Optional.of(givenHospital));

        // When
        String url = "/search/hospital?id=3";

        MockHttpServletResponse response = mvc.perform(get(url)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        // Then
        assertThat(response
                .getStatus())
                .isEqualTo(HttpStatus.OK.value());
        assertThat(response
                .getContentAsString())
                .isEqualTo(responseText);

    }
}