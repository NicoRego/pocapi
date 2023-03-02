package com.nicorego.nhs.pocapi.model;

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
@DynamicUpdate
@Table(name = "hospital")
public class Hospital implements Serializable {

	// Attributes
	
	@Id
	@Getter
	@Setter
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idhospital;

	@Getter
	@Setter
	@Column(name = "hosp_name")
	private String hospitalName;

	@Getter
	@Setter
	@Column(name = "latitude")
	private Double latitude;

	@Getter
	@Setter
	@Column(name = "longitude")
	private Double longitude;

	@Getter
	@Setter
	@Column(name = "free_beds")
	private Integer freeBeds;

	// Jointure
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
