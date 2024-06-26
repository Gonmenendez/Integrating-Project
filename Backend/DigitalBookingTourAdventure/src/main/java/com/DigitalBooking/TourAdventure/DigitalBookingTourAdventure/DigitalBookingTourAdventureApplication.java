package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.Clock;

@Configuration
@EnableTransactionManagement
@SpringBootApplication
public class DigitalBookingTourAdventureApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalBookingTourAdventureApplication.class, args);
	}

	@Bean
	public Clock clock(){
		return Clock.systemDefaultZone();
	}
}
