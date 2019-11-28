package com.esd.vacationapi.services;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import com.esd.vacationapi.domain.Ferias;


public interface EmailService {
	/* 
	 * criado como interface porque pode ser utilizado para
	 * email mock ou email smtp, causando flexibilidade na utilização
	 * de ambas classes  
	 * */
	
	void sendConfirmationEmail(Ferias obj);
	
	void sendEmail(SimpleMailMessage msg);  // enviando email plano
	
	void sendConfirmationHtmlEmail(Ferias obj);
	
	void sendHtmlEmail(MimeMessage msg);  // MimeMessage para enviar um email html
}
