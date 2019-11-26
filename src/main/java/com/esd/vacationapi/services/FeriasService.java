package com.esd.vacationapi.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.esd.vacationapi.domain.Equipe;
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
		
		Calendar inicioPeriodoFerias = Calendar.getInstance();
		Calendar fimPeriodoFerias = Calendar.getInstance();
		Calendar dataContratacao= Calendar.getInstance();
		
		inicioPeriodoFerias.setTime(obj.getInicioFerias());
		fimPeriodoFerias.setTime(obj.getFinalFerias());
		dataContratacao.setTime(obj.getFuncionario().getDataContratacao());
		
		Long finalFerias = fimPeriodoFerias.getTimeInMillis();
		Long inicioFerias = inicioPeriodoFerias.getTimeInMillis();		
		Integer conversao = 24*60*60*1000;
		
		Equipe equipe = obj.getFuncionario().getEquipe();		
		
		
		
		if( (finalFerias - inicioFerias) / conversao < 15 || (finalFerias - inicioFerias) / conversao > 31) {  // as vezes essa conversão come 1 dia
			throw new VacationException("Quantidade de tempo inválida");
		}		
		
		else if( (inicioFerias - dataContratacao.getTimeInMillis()) / conversao < 365 ) {
			throw new VacationException("Não pode solicitar férias com menos de 1 ano de contratado");
		}
		
		else if(equipe.getFuncionarios().size() <= 4) {
			coincideDataFeriasFuncionarios(obj.getInicioFerias(), obj.getFinalFerias(), equipe, obj.getFuncionario());
		}
		
		
		return true;
				
	}
	
	public boolean coincideDataFeriasFuncionarios(Date inicioPeriodoFerias, Date fimPeriodoFerias, Equipe equipe, Funcionario funcionario) {
		
		for(Funcionario func : equipe.getFuncionarios()) {
			/* confirmando se um funcionário da mesma equipe tem férias */
			if(func.getFerias() != null) {				
				if(funcionario.equals(func) && funcionario.getFerias() != null) {
					throw new VacationException("Você já possui férias cadastrada, necessário excluir para fazer novo cadastro.");

				}
				
				else if (inicioPeriodoFerias.after(func.getFerias().getInicioFerias()) && inicioPeriodoFerias.before(func.getFerias().getFinalFerias())
						|| fimPeriodoFerias.after(func.getFerias().getInicioFerias()) && fimPeriodoFerias.before(func.getFerias().getFinalFerias()) 
						|| inicioPeriodoFerias.equals(func.getFerias().getInicioFerias()) || fimPeriodoFerias.equals(func.getFerias().getFinalFerias())) {
					/* 
					 * ele pega a da inicial do período de férias que estamos cadastrando e verifica se ele está dentro
					 *  do período de outro funcionário da mesma equipe e faz o mesmo passo para a data final
					 *  se entrar nesse bloco é porque tem alguém com férias coincidindo.
					 *  se a data início e fim forem exatamente iguais, acabava passando por essa condição, por isso
					 *  fiz uma garantia utilizando o equals.
					 *  */
					throw new VacationException("Data coincide com outra pessoa da mesma equipe!");
					
				}
			}
		}
			
		
		return true;
	}
	
}
