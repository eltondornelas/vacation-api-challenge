package com.esd.vacationapi.services;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class SmtpEmailService extends AbstractEmailService {
	
	@Autowired
	private MailSender mailSender;
	/*
	 * MailSender é uma instância do framework, que quando instanciada, automaticamente
	 * vai pegar todos os dados que foram estabelecidos lá no app.properties que tenha
	 * "spring.mail" e instancia um objeto com esses dados
	 * */
	
	@Autowired
	private JavaMailSender javaMailSender;

	private static final Logger LOG = LoggerFactory.getLogger(SmtpEmailService.class);

	@Override
	public void sendEmail(SimpleMailMessage msg) {

		LOG.info("Enviando email...");
		mailSender.send(msg);  // enviando a mensagem
		LOG.info("Email enviado");
	}

	@Override
	public void sendHtmlEmail(MimeMessage msg) {
		
		LOG.info("Enviando email HTML...");
		javaMailSender.send(msg);
		LOG.info("Email enviado");
	}

	
}