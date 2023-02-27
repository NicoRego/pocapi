package com.nicorego.nhs.pocapi.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
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
import lombok.Data;

@Data
@Entity
@Table(name = "hospital")
public class Hospital implements Serializable {

	// Attributes
	
	@Id
	@JsonProperty("id_hospital")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id_hospital;

	@JsonProperty("hospital_name")
	@Column(name = "hospital_name")
	public String hospital_name;

	@JsonProperty("latitude")
	@Column(name = "latitude")
	public Double latitude;

	@JsonProperty("longitude")
	@Column(name = "longitude")
	public Double longitude;

	@JsonProperty("free_beds")
	@Column(name = "free_beds")
	public Integer freeBeds;

	// Jointure
	@JsonManagedReference
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
    
    public List<Specialty> specialties = new ArrayList<>();

    public List<Specialty> getSpecialties() {
		return specialties;
	}	

	public void setSpecialties(List<Specialty> specialties) {
		this.specialties = specialties;
	}

}
