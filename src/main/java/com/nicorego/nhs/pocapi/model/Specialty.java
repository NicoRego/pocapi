package com.nicorego.nhs.pocapi.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
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
  	
    // Getters and Setters
   
 	// Hospitals

	public List<Hospital> hospitals = new ArrayList<>();
	 
 	public List<Hospital> getHospitals() {
		return hospitals;
	}
 	
 	public void setHospitals(List<Hospital> hospitals) {
		this.hospitals = hospitals;
	}

}