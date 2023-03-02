package com.nicorego.nhs.pocapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@ToString
@NoArgsConstructor
@Entity
@DynamicUpdate
@Table(name = "specialty")
public class Specialty implements Serializable {

    @Id
	@Getter
	@Setter
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

	@Getter
	@Setter
    @Column(name="name")
	private String name;

	// Getters and Setters

}