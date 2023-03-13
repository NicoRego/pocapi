package com.nicorego.nhs.pocapi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.nicorego.nhs.pocapi.model.Hospital;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HospitalRepository extends CrudRepository<Hospital, Integer>{

    // Main query for API - Get hospitals for a given specialty where beds are available
    @Query(value = "SELECT h.id, h.name, h.latitude, h.longitude, h.free_beds " +
            "FROM hospital h " +
            "INNER JOIN hospital_specialty hs ON hs.idhospital = h.id " +
            "INNER JOIN specialty s ON hs.idspecialty = s.id " +
            "WHERE h.free_beds > :minFreeBeds " +
            "AND s.id = :specialtyId", nativeQuery = true)
    List<Hospital> findBySpecialtyAndFreeBeds(@Param("specialtyId") int specialtyId, @Param("minFreeBeds") int minFreeBeds);

    // Sample query for API - Get hospitals for a given specialty
    @Query(value = "SELECT h.id, h.name, h.latitude, h.longitude, h.free_beds " +
            "FROM hospital h " +
            "INNER JOIN hospital_specialty hs ON hs.idhospital = h.id " +
            "INNER JOIN specialty s ON hs.idspecialty = s.id " +
            "WHERE s.id = ?1", nativeQuery = true)
    List<Hospital> findBySpecialty(@Param("specialtyId") int specialtyId);
}
