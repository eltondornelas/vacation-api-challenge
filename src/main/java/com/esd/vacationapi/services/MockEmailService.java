package com.esd.vacationapi.services;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

public class MockEmailService extends AbstractEmailService {

	private static final Logger LOG = LoggerFactory.getLogger(MockEmailService.class);  // passa a classe que está
	// Logger é para mostrar o email no log do servidor
	// static para ficar apenas um para todas as instancias
	
	@Override
	public void sendEmail(SimpleMailMessage msg) {

		LOG.info("Simulando envio de email...");
		LOG.info(msg.toString());
		LOG.info("Email enviado");
		
	}
//
//	@Override
//	public void sendHtmlEmail(MimeMessage msg) {
//		LOG.info("Simulando envio de email HTML...");
//		LOG.info(msg.toString());
//		LOG.info("Email enviado");
//		
//	}
}