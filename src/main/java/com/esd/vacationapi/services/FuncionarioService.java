package com.esd.vacationapi.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.esd.vacationapi.domain.Endereco;
import com.esd.vacationapi.domain.Equipe;
import com.esd.vacationapi.domain.Funcionario;
import com.esd.vacationapi.domain.enums.Perfil;
import com.esd.vacationapi.dto.FuncionarioNewDTO;
import com.esd.vacationapi.repositories.FuncionarioRepository;
import com.esd.vacationapi.security.UserSS;
import com.esd.vacationapi.services.exceptions.AuthorizationException;
import com.esd.vacationapi.services.exceptions.DataIntegrityException;
import com.esd.vacationapi.services.exceptions.ObjectNotFoundException;
import com.esd.vacationapi.services.exceptions.VacationException;

@Service
public class FuncionarioService {

	@Autowired
	private BCryptPasswordEncoder pe;  // password enconder
	
	@Autowired 
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private EquipeService equipeService;
	
	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private ImageService imageService;	
	
	@Value("${img.prefix.client.profile}")
	private String prefix;
	
	
	/*
	 * quando declaramos uma dependência dentro de uma classe
	 * ao colocar o @Autowired, essa dependência vai ser automaticamente
	 * instanciada pelo Spring pelo mecanismo de injeção de dependência ou
	 * inversão de controle
	 * */

	public Funcionario find(Integer matricula) {
		//precisamos acessar dados, consequentemente temos que acessar o repository.
		
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN) && !matricula.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		/* 
		 * essa condição verifica se esse funcionário é o ADMIN, caso negativo
		 * verifica se ele é o mesmo do id solicitado e caso negativo, ele receberá
		 * uma mensagem com acesso negado.
		 *  */
		}
		
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
	
	public void delete(Integer matricula) {
		
		find(matricula);  // caso não exista já dispara a exceção
		
		try {
						
			funcionarioRepository.deleteById(matricula);
		
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
	
	public List<Funcionario> findAllMissing(Integer meses) {

		List<Funcionario> listAll = funcionarioRepository.findAll();
		// procurar buscar do banco de dados os funcionarios sem férias
		
		List<Funcionario> listaQuemFaltaCadastrarFerias = new ArrayList<>();
		
		for(Funcionario func : listAll)
			if(func.getFerias() == null) {  // buscando funcionários que não tem férias programadas
				listaQuemFaltaCadastrarFerias.add(func);
			}
		
		listaQuemFaltaCadastrarFerias = confirmaPendentes(listaQuemFaltaCadastrarFerias, meses);
		/* fazer exceção para o caso de todos serem null */
		
		return listaQuemFaltaCadastrarFerias;

	}
	
	public List<Funcionario> confirmaPendentes(List<Funcionario> listaQuemFaltaCadastrarFerias, Integer meses) {
		
		//final int MESES_LIMITE = 20;  // defini que 20 meses é o alerta.
		
		List<Funcionario> listaConfirmada = new ArrayList<>();
		
		Calendar calContratado = Calendar.getInstance();
		Calendar calAgora = Calendar.getInstance();
		
		Long conversaoMeses = 1000 * 60 * 60 * 24 * 30L;
		
		for(Funcionario func : listaQuemFaltaCadastrarFerias) {
			calContratado.setTime(func.getDataContratacao());
			if( (24 - (calAgora.getTimeInMillis() - calContratado.getTimeInMillis()) / conversaoMeses ) == meses ) {  // >= meses; na lógica anterior
				listaConfirmada.add(func);
			}
		}
		
		return listaConfirmada;
	}
	
	public Funcionario fromDTO(FuncionarioNewDTO objDto) {
		
		Equipe eq = new Equipe();
		eq = equipeService.findByName(objDto.getEquipe());
		
		if(eq == null)
			throw new VacationException("Nome da Equipe NÃO encontrado!");
		
		Endereco end = new Endereco(null, objDto.getRua(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCidade(), objDto.getEstado());
		
		Funcionario func = new Funcionario(null, objDto.getNome(), objDto.getDataNascimento(), objDto.getDataContratacao(), end, eq, objDto.getEmail(), pe.encode(objDto.getSenha()));
		
		end.setFuncionario(func);
		
		equipeService.update(eq);
		
		/* 
		 * enderecoRepository.save(end);
		 * não é necessário, pois ele é vinculado diretamente ao id do funcionário
		 * se tentar dar um .saveAll(end) vai dar erro
		 *  */
		
		return func; 
	}
	
	public URI uploadProfilePicture(MultipartFile multipartFile) {
				
		UserSS user = UserService.authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
			// não tem ninguém logado, por isso nega
		}
		
		//link exemplo: https://vacation-api.s3.sa-east-1.amazonaws.com/func8.jpg
		
		//URI uri = s3Service.uploadFile(multipartFile);
		
		Funcionario func = find(user.getId());
		//func.setImageUrl(uri.toString());
		//funcionarioRepository.save(func);
		
		//return uri;
		
		
		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
		
		//jpgImage = imageService.cropSquare(jpgImage);
		//jpgImage = imageService.resize(jpgImage, size);

		String fileName = prefix + user.getId() + ".jpg";
		// montando o nomne personalizado. esse prefix é o que colocamos la no
		// application properties: "func"
		// a partir de agora ficará salvo na nuvem desta forma e gerará a url com esse
		// nome no final
		
		func.setImageUrl(s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image").toString());
		update(func); // salvando no bd a url além de salvar na nuvem abaixo.
		
		return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
	}
	
}
