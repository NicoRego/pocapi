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
	private Long idspecialty;

	@Getter
	@Setter
    @Column(name="spec_name")
	private String specialtyName;
 	    

	// Getters and Setters

}