package com.nicorego.nhs.pocapi.service;

import com.nicorego.nhs.pocapi.model.Hospital;
import com.nicorego.nhs.pocapi.model.HospitalSpecialty;
import com.nicorego.nhs.pocapi.model.Specialty;
import com.nicorego.nhs.pocapi.repository.HospitalRepository;
import com.nicorego.nhs.pocapi.repository.HospitalSpecialtyRepository;
import com.nicorego.nhs.pocapi.repository.SpecialtyRepository;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@NoArgsConstructor
public class HospitalServiceIntegrationTest {

    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private HospitalRepository hospitalRepository;
    @Autowired
    private SpecialtyRepository specialtyRepository;
    @Autowired
    private HospitalSpecialtyRepository hospitalSpecialtyRepository;

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
    public void testGetNearestAvailableHospital() {

        // Given
        Optional<Hospital> searchHospital = hospitalRepository.findById(3);

        // When
        Hospital nearestHospital = hospitalService.getNearestAvailableHospital(50.616312, 3.051438, 3);

        // Then
        if (searchHospital.isPresent()) {
            assertEquals(nearestHospital.getId(), searchHospital.get().getId());
            assertEquals(nearestHospital.getName(), searchHospital.get().getName());
        } else {
            fail("Given condition not met : searchHospital is not present");
        }

    }
}
