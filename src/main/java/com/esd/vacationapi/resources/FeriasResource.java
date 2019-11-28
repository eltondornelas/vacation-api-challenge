package com.esd.vacationapi.resources;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.esd.vacationapi.domain.Ferias;
import com.esd.vacationapi.dto.FeriasNewDTO;
import com.esd.vacationapi.services.EmailService;
import com.esd.vacationapi.services.FeriasService;
import com.esd.vacationapi.services.QRCodeService;
import com.google.zxing.WriterException;

@RestController
@RequestMapping(value="/ferias")
public class FeriasResource {
	
	@Autowired
	private FeriasService feriasService;
	
	@Autowired
	private QRCodeService qrService;
	
	@Autowired
	private EmailService emailService;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Ferias> find(@PathVariable Integer id) {
		
		Ferias obj = feriasService.find(id);
		
		return ResponseEntity.ok().body(obj);
			
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody FeriasNewDTO objDto, HttpServletResponse response) {		
		/*
		 * inserindo pelo postman:
		 * 
		 * "matricula" : "1",
		  "inicioFerias" : "15/01/2020",
		  "finalFerias" : "15/02/2020"
		 * */
		
		Ferias obj = feriasService.fromDTO(objDto);
		
		feriasService.aprovaSolicitacaoFerias(obj);
		// se não for aprovado, vai ser lançado exceção lá do service.
		
		obj = feriasService.insert(obj);		
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(obj.getId()).toUri();
		
		try {			
			
			qrService.generateQRCodeImage(obj.toString(), 350, 350, QRCodeService.QR_CODE_IMAGE_PATH);  // testando armazenamento e funcionamento do QR Code
						
			byte[] qrCode = qrService.getQRCodeImage(obj.toString(), 350, 350);  // recebendo o QR Code em bytes para impressão na response
			
			response.addHeader("Operation Response", "Registro Concluído com Sucesso!");
			response.addHeader("QRCode", qrCode.toString());
			
			emailService.sendConfirmationEmail(obj);  // iria colocar no service, mas deixei em resource para aproveitar o QR Code
			
		} catch (WriterException | IOException e) {
			 System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
		}		
		
		return ResponseEntity.created(uri).build();		
	}
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody Ferias obj, @PathVariable Integer id) {
		
		obj.setId(id);
		obj = feriasService.update(obj);
		
		return ResponseEntity.noContent().build();
		
	}
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
			
		feriasService.delete(id);
		
		return ResponseEntity.noContent().build();
	}
		
		
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Ferias>> findAll() {
	
		List<Ferias> list = feriasService.findAll();		 
		
		return ResponseEntity.ok().body(list);
	
	}
}
