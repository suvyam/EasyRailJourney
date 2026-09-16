package com.easyrailjourney.EasyRailJourney;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity 
public class EasyRailJourneyApplication {

	public static void main(String[] args) {
		SpringApplication.run(EasyRailJourneyApplication.class, args);
	}

}
