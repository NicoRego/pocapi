package com.nicorego.nhs.pocapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PocapiApplication implements CommandLineRunner {

	@Autowired
	public PocapiApplication() {
	}
	public static void main(String[] args) {
		SpringApplication.run(PocapiApplication.class, args);
	}

	@Override
	public void run(String... args) {

	}

}
