package com.nicorego.nhs.pocapi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.nicorego.nhs.pocapi.model.Hospital;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HospitalRepository extends CrudRepository<Hospital, Integer>{

    // Main query for API - Get hospitals for a given specialty where beds are available
    @Query(value = "SELECT * " +
            "FROM hospital h " +
            "INNER JOIN hospital_specialty hs ON hs.hospitalid = h.idhospital " +
            "INNER JOIN specialty s ON hs.specialtyid = s.idspecialty " +
            "WHERE h.free_beds > :minFreeBeds " +
            "AND s.idspecialty = :specialtyId", nativeQuery = true)
    List<Hospital> findBySpecialtyAndFreeBeds(@Param("specialtyId") int specialtyId, @Param("minFreeBeds") int minFreeBeds);

    // Sample query for API - Get hospitals for a given specialty
    @Query(value = "SELECT * " +
            "FROM hospital h " +
            "INNER JOIN hospital_specialty hs ON hs.hospitalid = h.idhospital " +
            "INNER JOIN specialty s ON hs.specialtyid = s.idspecialty " +
            "WHERE s.idspecialty = ?1", nativeQuery = true)
    List<Hospital> findBySpecialty(@Param("specialtyId") int specialtyId);
}
