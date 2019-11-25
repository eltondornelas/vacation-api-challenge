package com.esd.vacationapi.resources;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.esd.vacationapi.domain.Equipe;
import com.esd.vacationapi.services.EquipeService;

@RestController
@RequestMapping(value="/equipes")
public class EquipeResource {
	
	@Autowired
	private EquipeService equipeService;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Equipe> find(@PathVariable Integer id) {
		
		Equipe obj = equipeService.find(id);
		
		return ResponseEntity.ok().body(obj);
			
	}	
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody Equipe obj) {
		
		obj = equipeService.insert(obj); 
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(obj.getId()).toUri();
		
		
		return ResponseEntity.created(uri).build();		
	}
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody Equipe obj, @PathVariable Integer id) {
		
		obj.setId(id);
		obj = equipeService.update(obj);
		
		return ResponseEntity.noContent().build();
		
	}
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
			
		equipeService.delete(id);
		
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Equipe>> findAll() {
	
		List<Equipe> list = equipeService.findAll();		 
		
		return ResponseEntity.ok().body(list);
	
	}
}
