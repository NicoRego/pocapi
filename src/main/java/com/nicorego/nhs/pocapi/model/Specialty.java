package com.nicorego.nhs.pocapi.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ToString
@Getter
@Setter
@RequiredArgsConstructor
@Entity
@DynamicUpdate
@Table(name = "specialty")
public class Specialty implements Serializable {

    @Id
    @JsonProperty("idspecialty")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	public Long idspecialty;

    @JsonProperty("specialty_name")
    @Column(name="spec_name")
	public String specialty_name;
 	    
    // Jointures
    @JsonManagedReference
 	@ManyToMany(
 			mappedBy = "specialties",
 			cascade = {
 					CascadeType.PERSIST,
 					CascadeType.MERGE
 			} 
 	)
	@ToString.Exclude

	// Getters and Setters
   
 	// Hospitals

	public List<Hospital> hospitals = new ArrayList<>();
	 
 	public List<Hospital> getHospitals() {
		return hospitals;
	}
 	
 	public void setHospitals(List<Hospital> hospitals) {
		this.hospitals = hospitals;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Specialty specialty = (Specialty) o;
		return idspecialty != null && Objects.equals(idspecialty, specialty.idspecialty);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}