package com.esd.vacationapi.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.esd.vacationapi.domain.Funcionario;
import com.esd.vacationapi.services.FuncionarioService;

@RestController
@RequestMapping(value="/funcionarios")
public class FuncionarioResource {
	/*
	 * Pacote resource fica as classes que são controladores REST 
	 * 
	 * */
	
	@Autowired
	private FuncionarioService funcionarioService;
	//respeitando as camadas, o rest chama o service que busca no repository
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Funcionario> find(@PathVariable Integer id) {
		/* Com o RequestMapping ele entende que além do endpoint /funcionario vai
		 * receber também o id do funcionãrio: /funcionarios/id;
		 * 
		 * Para que o Spring saiba que o {id} tem que ir para o id do parâmetro
		 * precisamos incluir a a anotação PathVariable
		 * 
		 * Trocamos de List para ResponseEntity, que é um objeto tipo especial que
		 * já encapsula/armazena várias informações de uma respostas http para um
		 * serviço REST
		 * */
		
		Funcionario obj = funcionarioService.find(id);
		
		return ResponseEntity.ok().body(obj);
		/* retornando um objeto do tipo ResponseEntity que terá os códigos http de
		 * respostas e informações do protocolo http. Para colocar que resposta
		 * deu sucesso, é colocando o ".ok" e que terá como corpo (body) o 
		 * objeto que buscou que é o Funcionario  */
		
//		Funcionario f1 = new Funcionario(1,  "01/11/1990", "20/11/2017", "1234");
//		Funcionario f2 = new Funcionario(2,  "14/08/1995", "10/11/2018", "abcd");
//		
//		List<Funcionario> lista = new ArrayList<>();
//				
//		lista.add(f1);
//		lista.add(f2);
//		
//		return lista;
				
	}
}
