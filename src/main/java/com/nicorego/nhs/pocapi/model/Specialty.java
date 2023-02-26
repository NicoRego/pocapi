package com.nicorego.nhs.pocapi.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "specialty")
public class Specialty {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long idspecialty;

    @Column(name="spec_name")
    private String specialty_name;
 	    
    // Joins
 	@ManyToMany(
 			mappedBy = "specialties",
 			cascade = {
 					CascadeType.PERSIST,
 					CascadeType.MERGE
 			} 
 	)
  	
    // Getters and Setters
   
 	// Hospitals
 	
	private List<Hospital> hospitals = new ArrayList<>();
	 
 	public List<Hospital> getHospitals() {
		return hospitals;
	}
 	
 	public void setHospitals(List<Hospital> hospitals) {
		this.hospitals = hospitals;
	}
 	
 	// Id
 	
	public Long getId() {
        return idspecialty;
    }

	public void setId(Long id) {
        this.idspecialty = id;
    }
	
	// Name
	
    public String getName() {
        return specialty_name;
    }

    public void setName(String name) {
        this.specialty_name = name;
    }

}