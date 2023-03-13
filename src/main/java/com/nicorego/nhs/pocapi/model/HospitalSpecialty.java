package com.nicorego.nhs.pocapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "hospital_specialty")
public class HospitalSpecialty {

    @EmbeddedId
    private HospitalSpecialtyId id;

    // Join with Hospital
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("id")
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Hospital hospital;

    // Join with Specialty
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("id")
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Specialty specialty;

}

