package com.nicorego.nhs.pocapi.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "hospital")
public class Hospital {

	// Attributes
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idhospital;

	@Column(name = "hosp_name")
	private String hospital_name;

	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "longitude")
	private Double longitude;
	
	@Column(name = "free_beds")
	private Integer freeBeds;

	// Join
    @ManyToMany(
			fetch = FetchType.LAZY,
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE
					}
			)
	@JoinTable(
			name = "hospital_specialty",
			joinColumns = @JoinColumn(name = "hospital_id"),
			inverseJoinColumns = @JoinColumn(name = "specialty_id")
			)
		
	// Getters and setters
	  
    // Specialties
    
    private List<Specialty> specialties = new ArrayList<>();	
	
	public List<Specialty> getSpecialties() {
		return specialties;
	}	

	public void setSpecialties(List<Specialty> specialties) {
		this.specialties = specialties;
	}

	// Id
	
	public Long getId() {
		return idhospital;
	}

	public void setId(Long id) {
		this.idhospital = id;
	}

	// Name
	
	public String getName() {
		return hospital_name;
	}

	public void setName(String name) {
		this.hospital_name = name;
	}

	// Latitude
	
	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	// Longitude
	
	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	// Free Beds
	
	public Integer getFreeBeds() {
		return freeBeds;
	}

	public void setFreeBeds(Integer freeBeds) {
		this.freeBeds = freeBeds;
	}

}
