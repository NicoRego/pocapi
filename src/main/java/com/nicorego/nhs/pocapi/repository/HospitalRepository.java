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
            "INNER JOIN hospital_specialty hs ON hs.hospital_id = h.hospId " +
            "INNER JOIN specialty s ON hs.specialty_id = s.specId " +
            "WHERE h.free_beds > ?2 " +
            "AND hs.specialty_id = ?1", nativeQuery = true)
    List<Hospital> findBySpecialtyAndFreeBeds(@Param("specialtyId") int specialtyId, @Param("minFreeBeds") int minFreeBeds);
}
