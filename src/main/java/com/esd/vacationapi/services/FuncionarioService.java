package com.esd.vacationapi.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esd.vacationapi.domain.Funcionario;
import com.esd.vacationapi.repositories.FuncionarioRepository;

@Service
public class FuncionarioService {

	@Autowired 
	private FuncionarioRepository funcionarioRepository;
	/*
	 * quando declaramos uma dependência dentro de uma classe
	 * ao colocar o @Autowired, essa dependência vai ser automaticamente
	 * instanciada pelo Spring pelo mecanismo de injeção de dependência ou
	 * inversão de controle
	 * */ 
	
	public Funcionario find(Integer id) {
		//precisamos acessar dados, consequentemente temos que acessar o repository.
		
		Optional<Funcionario> obj = funcionarioRepository.findById(id);
		/*
		 * Optional é um objeto container (objeto que contém outro objeto) mas ele vai
		 * encapsular a questão desse objeto estar instanciado ou não, isso elimina
		 * um problema de retornar um objeto null e lançando um NullPointerException
		 * por isso no retorno, precisa colocar o obj.orElse(null) porque se ele foi
		 * instanciado, vai retornar o objeto, se não, retorna null.
		 * */
		
		return obj.orElse(null);
	}
	
}
