package com.esd.vacationapi.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.esd.vacationapi.domain.Endereco;
import com.esd.vacationapi.domain.Equipe;
import com.esd.vacationapi.domain.Funcionario;
import com.esd.vacationapi.dto.FuncionarioNewDTO;
import com.esd.vacationapi.repositories.EquipeRepository;
import com.esd.vacationapi.repositories.FuncionarioRepository;
import com.esd.vacationapi.services.exceptions.DataIntegrityException;
import com.esd.vacationapi.services.exceptions.ObjectNotFoundException;
import com.esd.vacationapi.services.exceptions.VacationException;

@Service
public class FuncionarioService {

	@Autowired 
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private EquipeService equipeService;
	
	
	/*
	 * quando declaramos uma dependência dentro de uma classe
	 * ao colocar o @Autowired, essa dependência vai ser automaticamente
	 * instanciada pelo Spring pelo mecanismo de injeção de dependência ou
	 * inversão de controle
	 * */

	public Funcionario find(Integer matricula) {
		//precisamos acessar dados, consequentemente temos que acessar o repository.
		
		Optional<Funcionario> obj = funcionarioRepository.findById(matricula);
		/*
		 * Optional é um objeto container (objeto que contém outro objeto) mas ele vai
		 * encapsular a questão desse objeto estar instanciado ou não, isso elimina
		 * um problema de retornar um objeto null e lançando um NullPointerException
		 * por isso no retorno, precisa colocar o obj.orElse(null) porque se ele foi
		 * instanciado, vai retornar o objeto, se não, retorna null.
		 * */

		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + matricula + ", Tipo: " + Funcionario.class.getName()));
		/* lançando exceção caso o id não exista. Quem tem que captar é a camada
		 * de recurso/REST. O ResponseEntity tem condições de receber a exceção e
		 * enviar um json apropriado como resposta http. Porém, controlador REST
		 * é para ser blocos pequenos de código e não fica legível colocar try catch.
		 * vamos utilizar um Handler que é um objeto especial que intercepta caso ocorra
		 * uma exceção e ele que vai lançar uma resposta http adequada, no caso uma 404.
		 * 
		 * */
	}
	
	
	public Funcionario insert(Funcionario obj) {
		obj.setMatricula(null); 
		/* 
		 * por desencargo de consciência, pois se ele não for null
		 *  o método save vai achar que é uma atualização e não uma inserção
		 * */
		
		return funcionarioRepository.save(obj);
		/* 
		 * Código http 201 = Created 
		 * necessário retornar a URI da nova inserção
		 * */
	}
	
	public Funcionario update(Funcionario obj) {
		
		find(obj.getMatricula());
		/* com o find, caso o id não exista ele já lança a exceção e não salva*/
		return funcionarioRepository.save(obj);
	}
	
	public void delete(Integer id) {
		
		find(id);
		try {
			funcionarioRepository.deleteById(id);
		
		}catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir funcionário que possui equipe!");
			/* não vamos permitir deleção de um funcionário, pois vinculado a ele tem Equipe */
		}
		
	}
	
	public List<Funcionario> findAll() {
		return funcionarioRepository.findAll();
		/*
		 * retorna todas as informações dos funcionários, mas nessa listagem 
		 * não quero que mostre o endereço dos funcionários, apenas quero mostrar 
		 * quando especificar na busca.
		 * Para controlar isso utilizamos o padrão DTO -> Data Transfer Object que é
		 * um Objeto de Transferência de Dados que tem apenas os dados que precise
		 * para alguma operação que eu fizer.
		 * */
	}
	
	public Funcionario fromDTO(FuncionarioNewDTO objDto) {
		
		Equipe eq = new Equipe();
		eq = equipeService.findByName(objDto.getEquipe());
		
		if(eq == null)
			throw new VacationException("Nome da Equipe NÃO encontrado!");
		
		Endereco end = new Endereco(null, objDto.getRua(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCidade(), objDto.getEstado());
		
		Funcionario func = new Funcionario(null, objDto.getNome(), objDto.getDataNascimento(), objDto.getDataContratacao(), end, eq);	
		
		end.setFuncionario(func);
		
		equipeService.update(eq);
		
		/* 
		 * enderecoRepository.save(end);
		 * não é necessário, pois ele é vinculado diretamente ao id do funcionário
		 * se tentar dar um .saveAll(end) vai dar erro
		 *  */
		
		return func; 
	}
	
}
