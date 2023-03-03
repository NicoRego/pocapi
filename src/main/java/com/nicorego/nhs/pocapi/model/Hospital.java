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
	private Long id;

	@Getter
	@Setter
	@Column(name = "name")
	private String name;

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

	// Join with HospitalSpecialty
	// @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true)
	// @JoinColumn(name = "idhospital")
	// private List<HospitalSpecialty> hospitalSpecialties = new ArrayList<>();

}
