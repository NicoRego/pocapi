package com.nicorego.nhs.pocapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "hospital_specialty")
public class HospitalSpecialty {
    @EmbeddedId
    private HospitalSpecialtyId id;

    @ManyToOne
    @JoinColumn(name = "idhospital", insertable = false, updatable = false)
    private Hospital hospital;

    @ManyToOne
    @JoinColumn(name = "idspecialty", insertable = false, updatable = false)
    private Specialty specialty;

    // Getters and setters
}

@Embeddable
class HospitalSpecialtyId implements Serializable {
    @Column(name = "hospitalId")
    private Long hospitalId;

    @Column(name = "specialtyId")
    private Long specialtyId;

    // Getters and setters
}
