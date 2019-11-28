package com.esd.vacationapi.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.esd.vacationapi.services.DBService;
import com.esd.vacationapi.services.EmailService;
import com.esd.vacationapi.services.MockEmailService;

@Configuration
@Profile("test")
public class TestConfig {

	@Autowired
	private DBService dbService;
	
	@Bean
	public boolean instantiateDatabase() throws ParseException {
		// instanciando o bd no profile de test
		
		dbService.instantiateTestDatabase();
		
		return true;
	}
	
	@Bean
	public EmailService emailService() {
		return new MockEmailService();
		
		/*
		 * em FeriasService a injeção de dependência do EmailService é uma interface
		 * então para informar que essa interface será instanciada como
		 * MockEmailService é através daqui, ou seja, quem injetar a dependência
		 * para EmailService vai estar na verdade instanciando um MockEmailService
		 * seja em qualquer local.
		 * */
	}
}
