package com.esd.vacationapi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class VacationapiApplication implements CommandLineRunner {
	/* ao implementar a interface CommandLineRunner, ele permite
	 * criar métodos auxiliares aqui no "main" para executar alguma
	 * ação quando a aplicação iniciar. */
		
	public static void main(String[] args) {
		SpringApplication.run(VacationapiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}

}
