package com.nicorego.nhs.pocapi.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

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
	private Integer id;

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

	@Getter
	@Setter
	@Transient
	private String contextMessage;

}
