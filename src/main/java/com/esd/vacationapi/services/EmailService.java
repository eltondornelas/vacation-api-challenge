package com.esd.vacationapi.services;

import org.springframework.mail.SimpleMailMessage;

import com.esd.vacationapi.domain.Ferias;


public interface EmailService {
	/* 
	 * criado como interface porque pode ser utilizado para
	 * email mock ou email smtp, causando flexibilidade na utilização
	 * de ambas classes  
	 * */
	
	void sendConfirmationEmail(Ferias obj);
	
	void sendEmail(SimpleMailMessage msg);
}
