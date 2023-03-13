package com.nicorego.nhs.pocapi.controller;

import com.nicorego.nhs.pocapi.model.Hospital;
import com.nicorego.nhs.pocapi.model.HospitalSpecialty;
import com.nicorego.nhs.pocapi.model.Specialty;
import com.nicorego.nhs.pocapi.repository.HospitalRepository;
import com.nicorego.nhs.pocapi.repository.HospitalSpecialtyRepository;
import com.nicorego.nhs.pocapi.repository.SpecialtyRepository;
import com.nicorego.nhs.pocapi.service.HospitalService;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.nicorego.nhs.pocapi.utils.JsonMapper.getHospitalJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
public class HospitalControllerTest {

    private final MockMvc mvc;

    @MockBean
    private final HospitalService hospitalService;

    @MockBean
    private HospitalRepository hospitalRepository;

    private final SpecialtyRepository specialtyRepository;
    private final HospitalSpecialtyRepository hospitalSpecialtyRepository;

    @Autowired
    public HospitalControllerTest(MockMvc mvc, HospitalService hospitalService, HospitalRepository hospitalRepository, SpecialtyRepository specialtyRepository, HospitalSpecialtyRepository hospitalSpecialtyRepository) {
        this.mvc = mvc;
        this.hospitalService = hospitalService;
        this.hospitalRepository = hospitalRepository;
        this.specialtyRepository = specialtyRepository;
        this.hospitalSpecialtyRepository = hospitalSpecialtyRepository;
    }

    Hospital hospital1 = new Hospital();
    Hospital hospital2 = new Hospital();
    Hospital hospital3 = new Hospital();

    Specialty specialty1 = new Specialty();
    Specialty specialty2 = new Specialty();
    Specialty specialty3 = new Specialty();
    Specialty specialty4 = new Specialty();

    HospitalSpecialty hospitalSpecialty1 = new HospitalSpecialty();
    HospitalSpecialty hospitalSpecialty2 = new HospitalSpecialty();
    HospitalSpecialty hospitalSpecialty3 = new HospitalSpecialty();
    HospitalSpecialty hospitalSpecialty4 = new HospitalSpecialty();
    HospitalSpecialty hospitalSpecialty5 = new HospitalSpecialty();
    HospitalSpecialty hospitalSpecialty6 = new HospitalSpecialty();
    HospitalSpecialty hospitalSpecialty7 = new HospitalSpecialty();
    HospitalSpecialty hospitalSpecialty8 = new HospitalSpecialty();

    List<Hospital> hospitals = new ArrayList<>();
    List<Specialty> specialties = new ArrayList<>();
    List<HospitalSpecialty> hospitalsSpecialties = new ArrayList<>();

    @BeforeEach
    public void setUp(){

        // Set hospitals
        hospital1.setId(1);
        hospital1.setName("Hopital Saint Vincent de Paul");
        hospital1.setLatitude(50.620312);
        hospital1.setLongitude(3.077438);
        hospital1.setFreeBeds(2);
        hospital1.setContextMessage(null);

        hospital2.setId(2);
        hospital2.setName("Centre Hospitalier Universitaire de Lille");
        hospital2.setLatitude(50.610937);
        hospital2.setLongitude(3.034687);
        hospital2.setFreeBeds(0);
        hospital2.setContextMessage(null);

        hospital3.setId(3);
        hospital3.setName("Hopital prive La Louviere");
        hospital3.setLatitude(50.646438);
        hospital3.setLongitude(3.083563);
        hospital3.setFreeBeds(5);
        hospital3.setContextMessage(null);

        hospitals.add(hospital1);
        hospitals.add(hospital2);
        hospitals.add(hospital3);
        hospitalRepository.saveAll(hospitals);

        // Set specialties
        specialty1.setId(1);
        specialty1.setName("Cardiologie");
        specialty2.setId(2);
        specialty2.setName("Immunologie");
        specialty3.setId(3);
        specialty3.setName("Neuropathologie diagnostique");
        specialty4.setId(4);
        specialty4.setName("Nephrologie");
        specialties.add(specialty1);
        specialties.add(specialty2);
        specialties.add(specialty3);
        specialties.add(specialty4);
        specialtyRepository.saveAll(specialties);

        // Set hospitals specialties
        hospitalSpecialty1.setHospital(hospital1);
        hospitalSpecialty1.setSpecialty(specialty1);
        hospitalSpecialty2.setHospital(hospital1);
        hospitalSpecialty2.setSpecialty(specialty2);
        hospitalSpecialty3.setHospital(hospital2);
        hospitalSpecialty3.setSpecialty(specialty1);
        hospitalSpecialty4.setHospital(hospital2);
        hospitalSpecialty4.setSpecialty(specialty2);
        hospitalSpecialty5.setHospital(hospital2);
        hospitalSpecialty5.setSpecialty(specialty3);
        hospitalSpecialty6.setHospital(hospital2);
        hospitalSpecialty6.setSpecialty(specialty4);
        hospitalSpecialty7.setHospital(hospital3);
        hospitalSpecialty7.setSpecialty(specialty2);
        hospitalSpecialty8.setHospital(hospital3);
        hospitalSpecialty8.setSpecialty(specialty3);
        hospitalSpecialtyRepository.saveAll(hospitalsSpecialties);
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
        given(hospitalService.getNearestAvailableHospital(Double.parseDouble(searchLatitude), Double.parseDouble(searchLongitude), searchSpecialtyId))
                .willReturn(hospital1);

        // Set expected json object
        String responseHospitalJsonString = getHospitalJson(hospital1).toString();

        // When
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
        String responseHospitalJsonString = getHospitalJson(hospital3).toString();

        // Given
        given(hospitalService.getNearestAvailableHospital(Double.parseDouble(searchLatitude), Double.parseDouble(searchLongitude), searchSpecialtyId))
                .willReturn(hospital3);

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
        given(hospitalService.getNearestAvailableHospital(Double.parseDouble(searchLatitude), Double.parseDouble(searchLongitude),null))
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

        // Set expected json object
        Hospital responseHospital = hospital3;
        responseHospital.setContextMessage("Hospital 3 found successfully");
        String responseHospitalJsonString = getHospitalJson(hospital3).toString();

        // Given
        given(hospitalService.getHospitalById(3)).willReturn(Optional.of(hospital3));

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

    // *****************************************************************************
    // **                                                                         **
    // **                            bedBooking  tests                            **
    // **                                                                         **
    // *****************************************************************************

    @Test
    public void bedBooking_WillReturnOk() throws Exception {

        // Given

        // Set expected json object
        Hospital responseHospital = new Hospital();

        responseHospital.setId(3);
        responseHospital.setName("Hopital prive La Louviere");
        responseHospital.setLatitude(50.646438);
        responseHospital.setLongitude(3.083563);
        responseHospital.setFreeBeds(4);
        responseHospital.setContextMessage("Bed booked successfully in hospital 3");

        String responseHospitalString = getHospitalJson(responseHospital).toString();

        Mockito.when(this.hospitalService.getHospitalById(3)).thenReturn(Optional.of(hospital3));
        Mockito.when(this.hospitalService.bedBooking(hospital3)).thenReturn(responseHospital);

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

        // Set expected json object
        Hospital responseHospital = new Hospital();

        responseHospital.setId(2);
        responseHospital.setName("Centre Hospitalier Universitaire de Lille");
        responseHospital.setLatitude(50.610937);
        responseHospital.setLongitude(3.034687);
        responseHospital.setFreeBeds(0);
        responseHospital.setContextMessage("No bed available in Hospital 2");

        String responseHospitalString = getHospitalJson(responseHospital).toString();

        Mockito.when(this.hospitalService.getHospitalById(2)).thenReturn(Optional.of(hospital2));
        Mockito.when(this.hospitalService.bedBooking(hospital2)).thenReturn(responseHospital);

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
    public void bedBooking_WillReturnNotFound() throws Exception {

        // Given
        Hospital givenHospital = new Hospital();

        // Set expected json object
        Hospital responseHospital = new Hospital();

        responseHospital.setId(0);
        responseHospital.setName("");
        responseHospital.setLatitude(0.0);
        responseHospital.setLongitude(0.0);
        responseHospital.setFreeBeds(0);
        responseHospital.setContextMessage("Hospital 5 not found");

        String responseHospitalJsonString = getHospitalJson(responseHospital).toString();

        Mockito.when(this.hospitalService.getHospitalById(5)).thenReturn(Optional.empty());
        Mockito.when(this.hospitalService.bedBooking(givenHospital)).thenReturn(responseHospital);

        // When
        String url = "/bed/booking?hospital=5";

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

        // Set expected json object
        Hospital responseHospital = new Hospital();

        responseHospital.setId(3);
        responseHospital.setName("Hopital prive La Louviere");
        responseHospital.setLatitude(50.646438);
        responseHospital.setLongitude(3.083563);
        responseHospital.setFreeBeds(6);
        responseHospital.setContextMessage("Booking cancelled successfully in hospital 3");

        String responseHospitalString = getHospitalJson(responseHospital).toString();

        Mockito.when(this.hospitalService.getHospitalById(3)).thenReturn(Optional.of(hospital3));
        Mockito.when(this.hospitalService.cancelBedBooking(hospital3)).thenReturn(responseHospital);

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
        responseHospital.setContextMessage("Hospital 5 not found");

        String responseHospitalJsonString = getHospitalJson(responseHospital).toString();

        Mockito.when(this.hospitalService.getHospitalById(5)).thenReturn(Optional.empty());
        Mockito.when(this.hospitalService.cancelBedBooking(givenHospital)).thenReturn(responseHospital);

        // When
        String url = "/bed/booking/cancel?hospital=5";

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
