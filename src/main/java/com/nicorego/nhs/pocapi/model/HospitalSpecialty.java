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
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private Hospital hospital;

    @ManyToOne
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private Specialty specialty;

    // Getters and setters
}

@Embeddable
class HospitalSpecialtyId implements Serializable {
    @Column(name = "idhospital")
    private Long idhospital;

    @Column(name = "idspecialty")
    private Long idspecialty;

    // Getters and setters
}
