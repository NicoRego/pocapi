package com.nicorego.nhs.pocapi.utils;

import com.nicorego.nhs.pocapi.model.Hospital;
import com.nicorego.nhs.pocapi.model.Specialty;
import com.nicorego.nhs.pocapi.repository.HospitalRepository;
import com.nicorego.nhs.pocapi.service.HospitalService;
import com.nicorego.nhs.pocapi.service.SpecialtyService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static java.lang.Math.round;

@Transactional
public class PersonalTests {

    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private SpecialtyService specialtyService;
    @Autowired
    private HospitalRepository hospitalRepository;

    public PersonalTests() {}

    @Transactional
    public void main() {

        // Get all hospitals *******************************************************************************************

        System.out.println();
        System.out.println("====================");
        System.out.println("Test - ALL HOSPITALS");
        System.out.println("====================");
        System.out.println();
        Iterable<Hospital> iterableHospitals = hospitalService.getHospitals();
        iterableHospitals.forEach(hospital -> System.out.println(hospital.getIdhospital() + "-" + hospital.getHospitalName() + "-" + hospital.getFreeBeds()));

        // Get one hospital ********************************************************************************************

        System.out.println();
        System.out.println("===================");
        System.out.println("Test - ONE HOSPITAL");
        System.out.println("===================");
        System.out.println();

        Optional<Hospital> optionalHospital = hospitalService.getHospitalById(1);
        if (optionalHospital.isPresent()) {
            Hospital oneHospital = optionalHospital.get();
            System.out.println(oneHospital.getIdhospital() + "-" + oneHospital.getHospitalName());
        }

        // Get one specialty and related hospitals *********************************************************************

        System.out.println();
        System.out.println("======================================");
        System.out.println("Test - ONE SPECIALTY AND ITS HOSPITALS");
        System.out.println("======================================");
        System.out.println();

        Optional<Specialty> optSpecialtyForHospitals = specialtyService.getSpecialtyById(2);
        if (optSpecialtyForHospitals.isPresent()) {
            Specialty specialtyForHospitals = optSpecialtyForHospitals.get();


            System.out.println("Spécialité : " + specialtyForHospitals.getSpecialtyName());
            System.out.println();
            System.out.println("Hôpitaux associés :");

            specialtyForHospitals.getHospitals().forEach(
                    specialty -> System.out.println("- " + specialty.getHospitalName()));
        }

        // Get one hospital and related specialties ********************************************************************

        System.out.println();
        System.out.println("===========================================");
        System.out.println("Test - ALL HOSPITALS AND THEIRS SPECIALTIES");
        System.out.println("===========================================");

        long maxHospitals = hospitalRepository.count();

        System.out.println("Nb d'entités dans hospitalRepository = " + maxHospitals);
        Optional<Hospital> optHospitalForSpecialties;
        for (
                int hospId = 1;
                hospId <= maxHospitals; ++hospId) {
            optHospitalForSpecialties = hospitalService.getHospitalById(hospId);
            if (optHospitalForSpecialties.isPresent()) {
                Hospital hospitalForSpecialties = optHospitalForSpecialties.get();

                System.out.println();
                System.out.println(String.format("Hôpital n° %d : %s ", hospId, hospitalForSpecialties.getHospitalName()));
                System.out.println("Nombre de lits disponibles : " + hospitalForSpecialties.getFreeBeds());
                System.out.println("Spécialités associées :");

                hospitalForSpecialties.getSpecialties().forEach(
                        specialtiesForHopsital -> System.out.println(specialtiesForHopsital.getIdspecialty() + " - " + specialtiesForHopsital.getSpecialtyName()));
            } else {
                System.out.println(String.format("Hôpital %d introuvable", hospId));
            }
        }

        // Get Hospitals by specialty and free beds ********************************************************************

        System.out.println();
        System.out.println("===========================================================================");
        System.out.println("Test - FIND HOSPITALS FOR A GIVEN SPECIALTY AND WITH A MINIMUM OF FREE BEDS");
        System.out.println("===========================================================================");
        System.out.println();

        int givenSpecialtyId = 1;
        int minFreeBeds = 0;

        // Get specialty
        Optional<Specialty> optGivenSpecialty = specialtyService.getSpecialtyById(givenSpecialtyId);
        if (optGivenSpecialty.isPresent()) {
            Specialty givenSpecialty = optGivenSpecialty.get();

            // Resolve query
            Iterable<Hospital> iterableHospitalsSpecBeds = hospitalService.getHospitalsBySpecialtyAndFreeBeds(givenSpecialtyId, minFreeBeds);

            // Print out result
            System.out.println("Spécialité : " + givenSpecialty.getSpecialtyName());
            System.out.println();

            System.out.println("Hôpitaux associés :");
            iterableHospitalsSpecBeds.forEach(hospitalSpecBeds -> System.out.println(hospitalSpecBeds.getIdhospital() + "-" + hospitalSpecBeds.getHospitalName() + "-" + hospitalSpecBeds.getFreeBeds()));
        }

        // Get Hospitals by specialty and free beds ********************************************************************

        System.out.println();
        System.out.println("===========================================");
        System.out.println("Test - FIND HOSPITALS FOR A GIVEN SPECIALTY");
        System.out.println("===========================================");
        System.out.println();

        int given2ndSpecialtyId = 1;

        // Get specialty
        Optional<Specialty> opt2ndGivenSpecialty = specialtyService.getSpecialtyById(given2ndSpecialtyId);
        if (opt2ndGivenSpecialty.isPresent()) {
            Specialty given2ndSpecialty = opt2ndGivenSpecialty.get();

            // Resolve query
            Iterable<Hospital> iterableHospitalsSpec = hospitalService.getHospitalsBySpecialty(given2ndSpecialtyId);

            // Print out result
            System.out.println("Spécialité : " + given2ndSpecialty.getSpecialtyName());
            System.out.println();

            System.out.println("Hôpitaux associés :");
            iterableHospitalsSpec.forEach(hospitalSpec -> System.out.println(hospitalSpec.getIdhospital() + "-" + hospitalSpec.getHospitalName() + "-" + hospitalSpec.getFreeBeds()));
        }

        // Get nearest hospitals for a given specialty and with free beds **********************************************

        System.out.println();
        System.out.println("======================================================================");
        System.out.println("Test - FIND NEAREST HOSPITALS FOR A GIVEN SPECIALTY AND WITH FREE BEDS");
        System.out.println("======================================================================");
        System.out.println();

        int given3rdSpecialtyId = 1;
        // Near Hôpital Saint Vincent de Paul
        // double latitude = 50.637062;
        // double longitude = 3.064312;
        // Near Centre Hospitalier Universitaire de Lille
        double latitude = 50.616312;
        double longitude = 3.051438;

        System.out.println();

        Optional<Specialty> optNearestSpecialty = specialtyService.getSpecialtyById(given3rdSpecialtyId);

        if (optNearestSpecialty.isPresent()) {
            Hospital nearestAvailableHospital = hospitalService.getNearestAvailableHospital(latitude, longitude, given3rdSpecialtyId);
            Specialty given3rdSpecialty = optNearestSpecialty.get();

            // Print out result
            System.out.println("Spécialité : " + given3rdSpecialty.getSpecialtyName());
            System.out.println();

            System.out.println(String.format("Hôpital disponible : %s", nearestAvailableHospital.getHospitalName()));
            System.out.println();
            System.out.println("Distance du point : " + round(Distance.distanceHaversine(nearestAvailableHospital.getLatitude(), nearestAvailableHospital.getLongitude(), latitude, longitude)) + " km(s)");
        }

        // Book a bed in nearest available hospital *******************************************************************

        System.out.println();
        System.out.println("===============================================");
        System.out.println("Test - BOOK A BED IN NEAREST AVAILABLE HOSPITAL");
        System.out.println("===============================================");
        System.out.println();

        Hospital nearestAvailableHospital2nd = hospitalService.getNearestAvailableHospital(latitude, longitude, given3rdSpecialtyId);

        System.out.println(String.format("Hôpital réservé : %s", nearestAvailableHospital2nd.getHospitalName()));
        System.out.println(String.format("Nombre de lits disponibles après réservation : %d", nearestAvailableHospital2nd.getFreeBeds()));
        System.out.println();

        if (hospitalService.bedBooking(nearestAvailableHospital2nd)) {
            System.out.println(String.format("Nombre de lits disponibles après réservation : %d", nearestAvailableHospital2nd.getFreeBeds()));

        } else {
            System.out.println(String.format("Réservation non faite. Nombre de lits disponibles après réservation : %d", nearestAvailableHospital2nd.getFreeBeds()));
        }
        System.out.println();

        // Cancel booking

        System.out.println();
        System.out.println("====================================");
        System.out.println("TEST - CANCEL BOOKING IN AN HOSPITAL");
        System.out.println("====================================");
        System.out.println();

        System.out.println(String.format("Hôpital réservé : %s", nearestAvailableHospital2nd.getHospitalName()));
        System.out.println(String.format("Nombre de lits disponibles avant annulation : %d", nearestAvailableHospital2nd.getFreeBeds()));
        System.out.println();

        hospitalService.cancelBedBooking(nearestAvailableHospital2nd);

        System.out.println(String.format("Nombre de lits disponibles après annulation : %d", nearestAvailableHospital2nd.getFreeBeds()));

    }
}
