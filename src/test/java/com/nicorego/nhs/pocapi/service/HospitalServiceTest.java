package com.nicorego.nhs.pocapi.service;

import com.nicorego.nhs.pocapi.model.Hospital;
import com.nicorego.nhs.pocapi.model.HospitalSpecialty;
import com.nicorego.nhs.pocapi.model.Specialty;
import com.nicorego.nhs.pocapi.repository.HospitalRepository;
import com.nicorego.nhs.pocapi.repository.HospitalSpecialtyRepository;
import com.nicorego.nhs.pocapi.repository.SpecialtyRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class HospitalServiceTest {

    @MockBean
    private HospitalRepository hospitalRepository;
    private final SpecialtyRepository specialtyRepository;
    private final HospitalSpecialtyRepository hospitalSpecialtyRepository;

    private final HospitalService hospitalService;

    @Autowired
    public HospitalServiceTest(HospitalRepository hospitalRepository,
                               SpecialtyRepository specialtyRepository,
                               HospitalSpecialtyRepository hospitalSpecialtyRepository,
                               HospitalService hospitalService) {
        this.hospitalRepository = hospitalRepository;
        this.specialtyRepository = specialtyRepository;
        this.hospitalSpecialtyRepository = hospitalSpecialtyRepository;
        this.hospitalService = hospitalService;
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

    @Test
    void getHospitals() {

        // Given
        List<Hospital> expectedMockHospitals = new ArrayList<>();
        expectedMockHospitals.add(hospital1);
        expectedMockHospitals.add(hospital2);
        expectedMockHospitals.add(hospital3);

        Mockito.when(hospitalRepository.findAll()).thenReturn(expectedMockHospitals);

        // When
        Iterable<Hospital> hospitalIterable = hospitalService.getHospitals();

        // Then
        List<Hospital> hospitalList = new ArrayList<>();
        hospitalIterable.forEach(hospitalList::add);

        Assertions.assertEquals(hospitalList.size(), hospitals.size());
        Assertions.assertEquals(hospitalList.get(0).getName(), hospitals.get(0).getName());
        Assertions.assertEquals(hospitalList.get(1).getName(), hospitals.get(1).getName());
        Assertions.assertEquals(hospitalList.get(2).getName(), hospitals.get(2).getName());

    }

    @Test
    void getHospitalById() {

        // Given
        Mockito.when(hospitalRepository.findById(2)).thenReturn(Optional.of(hospital2));

        // When
        Optional<Hospital> searchHospital = hospitalService.getHospitalById(2);

        // Then
        if (searchHospital.isPresent()) {
            Assertions.assertEquals(searchHospital.get().getName(), hospital2.getName());
            Assertions.assertEquals(searchHospital.get().getLongitude(), hospital2.getLongitude());
            Assertions.assertEquals(searchHospital.get().getFreeBeds(), hospital2.getFreeBeds());
        } else {
            Assertions.fail("Unable to find hospital 2 using hospitalService.getHospitalById(2)");
        }

    }

    @Test
    void getHospitalsBySpecialtyAndFreeBeds() {

        // Given
        List<Hospital> expectedMockHospitals = new ArrayList<>();
        expectedMockHospitals.add(hospital1);
        expectedMockHospitals.add(hospital3);

        Mockito.when(hospitalRepository.findBySpecialtyAndFreeBeds(2,0)).thenReturn(expectedMockHospitals);

        // When
        Iterable<Hospital> hospitalIterable = hospitalService.getHospitalsBySpecialtyAndFreeBeds(2,0);

        // Then
        List<Hospital> hospitalList = new ArrayList<>();
        hospitalIterable.forEach(hospitalList::add);

        // Then
        Assertions.assertEquals(hospitalList.size(), 2);
        Assertions.assertEquals(hospitalList.get(0).getName(), hospital1.getName());
        Assertions.assertEquals(hospitalList.get(1).getName(), hospital3.getName());

    }

    @Test
    void getHospitalsBySpecialty() {

        // Given
        List<Hospital> expectedMockHospitals = new ArrayList<>();
        expectedMockHospitals.add(hospital2);
        expectedMockHospitals.add(hospital3);

        Mockito.when(hospitalRepository.findBySpecialty(3)).thenReturn(expectedMockHospitals);

        // When
        Iterable<Hospital> hospitalIterable = hospitalService.getHospitalsBySpecialty(3);

        // Then
        List<Hospital> hospitalList = new ArrayList<>();
        hospitalIterable.forEach(hospitalList::add);

        // Then
        Assertions.assertEquals(hospitalList.size(), 2);
        Assertions.assertEquals(hospitalList.get(0).getName(), hospital2.getName());
        Assertions.assertEquals(hospitalList.get(1).getName(), hospital3.getName());

    }

    @Test
    void bedBooking() {

        // Given
        Mockito.when(hospitalRepository.findById(3)).thenReturn(Optional.ofNullable(hospital3));

        // When
        Hospital bookedHospital = hospitalService.bedBooking(hospital3);
        int bookedHospitalFreeBeds = bookedHospital.getFreeBeds();

        // Then
        Assertions.assertEquals(4, bookedHospitalFreeBeds);

    }

    @Test
    void cancelBedBooking() {

        // Given
        Mockito.when(hospitalRepository.findById(1)).thenReturn(Optional.ofNullable(hospital1));

        // When
        Hospital cancelBookedHospital = hospitalService.cancelBedBooking(hospital1);
        int cancelBookedHospitalFreeBeds = cancelBookedHospital.getFreeBeds();

        // Then
        Assertions.assertEquals(3, cancelBookedHospitalFreeBeds);

    }

    @Test
    void saveHospital() {
        // Given
        Hospital hospital = new Hospital();
        hospital.setId(4);
        hospital.setName("Hopital Roger Salengro");
        hospital.setLatitude(50.607563);
        hospital.setLongitude(3.032188);
        hospital.setFreeBeds(10);

        // When
        when(hospitalRepository.save(hospital)).thenReturn(hospital);

        // Test hospital service method
        Hospital savedHospital = hospitalService.saveHospital(hospital);

        // Then
        verify(hospitalRepository).save(hospital);

        Assertions.assertEquals(hospital, savedHospital);
    }
}