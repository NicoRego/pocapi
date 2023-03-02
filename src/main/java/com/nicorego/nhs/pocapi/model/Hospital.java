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

}
