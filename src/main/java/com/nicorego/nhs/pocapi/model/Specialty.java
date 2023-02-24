package com.nicorego.nhs.pocapi.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "specialty")
public class Specialty {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    private String name;
 	    
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
        return id;
    }

	public void setId(Long id) {
        this.id = id;
    }
	
	// Name
	
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}