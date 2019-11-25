package com.esd.vacationapi.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

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

import com.esd.vacationapi.domain.Funcionario;
import com.esd.vacationapi.dto.FuncionarioDTO;
import com.esd.vacationapi.dto.FuncionarioNewDTO;
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
			
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody FuncionarioNewDTO objDto) {
		/* Inserindo um novo Funcionário no banco de dados com o POST
		 * recebendo em formato json
		 * 
		 * para que o parâmetro obj seja construído a partir dos dados json que for enviado,
		 * é necessário a anotação @RequestBody convertendo o json em objeto java automaticamente
		 * 
		 * Void porque será uma resposta com body vazio, sem retorno de obj
		 * 		 * 
		 * @Valid para validar as tags do javax validation na classe domain
		 * 
		 * */
		
		Funcionario obj = funcionarioService.fromDTO(objDto);
		
		/*
		 * Exemplo json: 
		  "nome" : "Everton Dornelas",
		  "dataNascimento" : "03/12/1984",
		  "dataContratacao" : "17/02/2012",
		  "rua" : "Rua de Olinda",
		  "numero" : "321",
		  "complemento" : "Torre C APT 701",
		  "bairro" : "Rio Doce",
		  "cidade" : "Olinda",
		  "estado" : "PE",
		  "equope" : "Bucha de Canhão",
		  "email" : "everton@gmail.com",
		  "senha" : "123"
		 * */
		
		obj = funcionarioService.insert(obj);  // operação save retorna o objeto
				
		/* 
		 * vamos inserir no bd e o próprio bd atribui um novo id e com esse retorno 
		 * vamos fornecer como argumento da URI 
		 * */
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(obj.getMatricula()).toUri();
		/* 
		 * o FromCurrentRequest pega a URL que utilizou para inserir
		 * ex: localhost:8080/funcionarios e vamos acrescentar o path /{id} que foi criado
		 * o buildAndExpand atribui o valor ao /{id} através do id adicionado ao objeto
		 * e convertemos para URI com o toUri.
		 *  */
		
		return ResponseEntity.created(uri).build();
		/*
		 * o método created já gera o código 201 e recebe a uri gerada como argumento
		 * e o build() gera essa resposta.
		 * */
		
		
		/*
		 * Testando no Postman: deixa na URI: http://localhost:8080/funcionarios
		 * muda a requisição para o método POST; vai em body -> clica em raw -> JSON
		 * basta digitar { "nome" : "Fulado Beltrano"} e enviar ele retornará um 201 created
		 * e em Headers vai ter o location com a URI e o novo id
		 * */
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody Funcionario obj, @PathVariable Integer id) {
		/*
		 * Mistura entre o GET e o POST, pois vai receber o objeto através do json e precisa
		 * do parâmetro da URL que tem o id
		 * */
		
		obj.setMatricula(id);
		obj = funcionarioService.update(obj);
		
		return ResponseEntity.noContent().build();
		/* retorna um conteúdo vazio */
		
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
			
		funcionarioService.delete(id);
		
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<FuncionarioDTO>> findAll() {
		/* 
		 * como irá mostrar todos os funcionários, não precisa do /id
		 * por isso não precisa de parâmetros
		 * ao digitar /funcioarios retorna todos os funcionários
		 *  */
		

		List<Funcionario> list = funcionarioService.findAll();
		/* 
		 * percorre toda a lista armazenando com objeto domain
		 * */
		
		List<FuncionarioDTO> listDto = list.stream().map(obj -> new FuncionarioDTO(obj)).collect(Collectors.toList()); 
		/*
		 * vamos percorrer essa a list utilizando o stream(), o map() efetua uma operação
		 * para cada elemento da list que chamei de obj e para cada obj, utilizando
		 * arrow function, instancia um objeto do tipo DTO e retorna para esse obj um
		 * elemento DTO e o stream converte os elementos para tipo lista com o .collectors
		 * atribuindo para listDto  
		 * */
		
		return ResponseEntity.ok().body(listDto);
		/* retornando uma lista de funcionários */
		
	}
}
