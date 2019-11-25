package com.esd.vacationapi.services;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.esd.vacationapi.domain.Ferias;
import com.esd.vacationapi.domain.Funcionario;
import com.esd.vacationapi.dto.FeriasNewDTO;
import com.esd.vacationapi.repositories.FeriasRepository;
import com.esd.vacationapi.services.exceptions.DataIntegrityException;
import com.esd.vacationapi.services.exceptions.ObjectNotFoundException;
import com.esd.vacationapi.services.exceptions.VacationException;

@Service
public class FeriasService {

	@Autowired 
	private FeriasRepository feriasRepository;
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	public Ferias find(Integer id) {		
		
		Optional<Ferias> obj = feriasRepository.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Ferias.class.getName()));
		
	}
	
	public Ferias insert(Ferias obj) {
		obj.setId(null); 
		
		return feriasRepository.save(obj);
	}
	
	public Ferias update(Ferias obj) {
		
		find(obj.getId());		
		return feriasRepository.save(obj);
	}
	
	public void delete(Integer id) {
		
		find(id);
		try {
			feriasRepository.deleteById(id);
		
		}catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir!");
		}
		
	}
	
	public List<Ferias> findAll() {
		return feriasRepository.findAll();

	}
	
	public Ferias fromDTO(FeriasNewDTO objDto) {
		
		Funcionario funcionario = funcionarioService.find(objDto.getMatricula());
		
		Ferias fer = new Ferias(null, funcionario, objDto.getInicioFerias(), objDto.getFinalFerias());
		
		return fer;
	}
	
	public boolean aprovaSolicitacaoFerias(Ferias obj) {
		
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		Calendar contratado = Calendar.getInstance();
		
		start.setTime(obj.getInicioFerias());
		end.setTime(obj.getFinalFerias());
		contratado.setTime(obj.getFuncionario().getDataContratacao());
		
		Long finalFerias = end.getTimeInMillis();
		Long inicioFerias = start.getTimeInMillis();		
		Integer conversao = 24*60*60*1000;
		
		if( (finalFerias - inicioFerias) / conversao < 15 || (finalFerias - inicioFerias) / conversao > 30) {
			throw new VacationException("Quantidade de tempo inválida");
		}		
		
		else if( (inicioFerias - contratado.getTimeInMillis()) / conversao < 365 ) {
			throw new VacationException("Não pode solicitar férias com menos de 1 ano de contratado");
		}
		
		return true;
				
	}
	
}
