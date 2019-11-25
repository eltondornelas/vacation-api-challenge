package com.esd.vacationapi.resources;

import java.net.URI;
import java.util.List;

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
import com.esd.vacationapi.services.FeriasService;

@RestController
@RequestMapping(value="/ferias")
public class FeriasResource {
	
	@Autowired
	private FeriasService feriasService;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Ferias> find(@PathVariable Integer id) {
		
		Ferias obj = feriasService.find(id);
		
		return ResponseEntity.ok().body(obj);
			
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody FeriasNewDTO objDto) {		
		/*
		 * inserindo pelo postman:
		 * 
		 * "matricula" : "1",
		  "inicioFerias" : "15/01/2020",
		  "finalFerias" : "15/02/2020"
		 * */
		
		Ferias obj = feriasService.fromDTO(objDto);
		
		feriasService.aprovaSolicitacaoFerias(obj);
		
		obj = feriasService.insert(obj);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(obj.getId()).toUri();
		
		
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
