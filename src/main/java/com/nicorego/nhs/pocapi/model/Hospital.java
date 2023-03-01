package com.nicorego.nhs.pocapi.model;
/*
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;*/
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ToString
@NoArgsConstructor
@Entity
// @JsonInclude
@DynamicUpdate
// @JsonIgnoreProperties("hospital_specialty")
@Table(name = "hospital")
public class Hospital implements Serializable {

	// Attributes
	
	@Id
	@Getter
	@Setter
	// @JsonProperty("idhospital")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idhospital;

	@Getter
	@Setter
	// @JsonProperty("hospital_name")
	@Column(name = "hosp_name")
	private String hospital_name;

	@Getter
	@Setter
	// @JsonProperty("latitude")
	@Column(name = "latitude")
	private Double latitude;

	@Getter
	@Setter
	// @JsonProperty("longitude")
	@Column(name = "longitude")
	private Double longitude;

	@Getter
	@Setter
	// @JsonProperty("free_beds")
	@Column(name = "free_beds")
	private Integer freeBeds;

	// Jointure
	// @JsonManagedReference
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

	// Convert hospital object to JSON
	/*public String toJson() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
	 	return mapper.writeValueAsString(this);
	}*/
}
