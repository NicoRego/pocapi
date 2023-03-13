package com.nicorego.nhs.pocapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class HospitalSpecialtyId implements Serializable {
    @Column(name = "idhospital")
    private Integer idhospital;

    @Column(name = "idspecialty")
    private Integer idspecialty;

    // Getters and setters
}
