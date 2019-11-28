package com.esd.vacationapi.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import com.esd.vacationapi.domain.Ferias;

public abstract class AbstractEmailService implements EmailService {
	/* 
	 * Abstrata porque a sendConfirmationEmail vai utilizar a 
	 * sendEmail, porém antes de enviar precisa preparar o objeto
	 * através do SimpleMailMessage e como é abstract não fica 
	 * obrigado a implementar as classes da interface
	 *  */
	
	@Value("${default.sender}")
	private String sender;
	
	@Override
	public void sendConfirmationEmail(Ferias obj) {
		SimpleMailMessage sm = prepareSimpleMailMessageFromFerias(obj);
		// função que vai instanciar um SMM a partir do obj de Ferias
		sendEmail(sm);
		// template method
	}

	protected SimpleMailMessage prepareSimpleMailMessageFromFerias(Ferias obj) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(obj.getFuncionario().getEmail());  // destinatário do email
		sm.setFrom(sender);  // email remetente é o email padrão da aplicação lá em app.properties
		sm.setSubject("Registro de Férias Concluído");  // assunto do email
		sm.setSentDate(new Date(System.currentTimeMillis()));  // para criar uma data com o horário do servidor
		sm.setText(obj.toString());
		
		return sm;
	}
	
}
