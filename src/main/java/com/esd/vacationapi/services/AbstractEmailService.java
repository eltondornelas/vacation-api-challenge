package com.esd.vacationapi.services;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

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
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
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
	
	protected String htmlFromTemplateFerias(Ferias obj) {
		Context context = new Context();  // precisa de um objeto desse tipo para acessar o template
		context.setVariable("ferias", obj);  // vai utilizar o obj com o apelido ferias
		return templateEngine.process("email/confirmacaoFerias", context);
		//é o caminho da subpasta que está em template, por padrão o tymeleaf busca já por /resources/templates
		
	}
	
	@Override
	public void sendConfirmationHtmlEmail(Ferias obj) {
		
		try {
			MimeMessage mm = prepareMimeMessageFromFerias(obj);
			sendHtmlEmail(mm);
			
		} catch (MessagingException e) {
			sendConfirmationEmail(obj);			
		}
		
	}

	protected MimeMessage prepareMimeMessageFromFerias(Ferias obj) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mmh = new MimeMessageHelper(mimeMessage, true);
		mmh.setTo(obj.getFuncionario().getEmail());
		mmh.setFrom(sender);
		mmh.setSubject("Registro de Férias Concluído");
		mmh.setSentDate(new Date(System.currentTimeMillis()));
		mmh.setText(htmlFromTemplateFerias(obj), true);
		
		return mimeMessage;
	}
	
}
