package com.esd.vacationapi.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.esd.vacationapi.domain.Equipe;
import com.esd.vacationapi.repositories.EquipeRepository;
import com.esd.vacationapi.services.exceptions.DataIntegrityException;
import com.esd.vacationapi.services.exceptions.ObjectNotFoundException;

@Service
public class EquipeService {

	@Autowired 
	private EquipeRepository equipeRepository;	
	
	public Equipe find(Integer id) {		
		
		Optional<Equipe> obj = equipeRepository.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Equipe.class.getName()));
		
	}
	
	public Equipe insert(Equipe obj) {
		obj.setId(null); 
		
		return equipeRepository.save(obj);
	}
	
	public Equipe update(Equipe obj) {
		
		find(obj.getId());		
		return equipeRepository.save(obj);
	}
	
	public void delete(Integer id) {
		
		find(id);
		try {
			equipeRepository.deleteById(id);
		
		}catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir!");
		}
		
	}
	
	public List<Equipe> findAll() {
		return equipeRepository.findAll();

	}
	
	public Equipe findByName(String nome) {
		
		for(Equipe e : equipeRepository.findAll()) {
			if(e.getNome().equals(nome))
				return e;
		}
		
		return null;
	}
	
}
