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
@Table(name = "hospital")
public class Hospital implements Serializable {

	// Attributes
	
	@Id
	@JsonProperty("idhospital")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long idhospital;

	@JsonProperty("hospital_name")
	@Column(name = "hosp_name")
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
			joinColumns = @JoinColumn(name = "hospitalid"),
			inverseJoinColumns = @JoinColumn(name = "specialtyid")
			)
	@ToString.Exclude

	// Getters and setters
	  
    // Specialties
    
    public List<Specialty> specialties = new ArrayList<>();

    public List<Specialty> getSpecialties() {
		return specialties;
	}	

	public void setSpecialties(List<Specialty> specialties) {
		this.specialties = specialties;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Hospital hospital = (Hospital) o;
		return idhospital != null && Objects.equals(idhospital, hospital.idhospital);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
