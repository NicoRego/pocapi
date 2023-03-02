package com.nicorego.nhs.pocapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ToString
@NoArgsConstructor
@Entity
@DynamicUpdate
@Table(name = "specialty")
public class Specialty implements Serializable {

    @Id
	@Getter
	@Setter
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long idspecialty;

	@Getter
	@Setter
    @Column(name="spec_name")
	private String specialtyName;
 	    
    // Jointures
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