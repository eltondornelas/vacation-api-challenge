package com.esd.vacationapi;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.esd.vacationapi.domain.Endereco;
import com.esd.vacationapi.domain.Equipe;
import com.esd.vacationapi.domain.Ferias;
import com.esd.vacationapi.domain.Funcionario;
import com.esd.vacationapi.repositories.EnderecoRepository;
import com.esd.vacationapi.repositories.EquipeRepository;
import com.esd.vacationapi.repositories.FeriasRepository;
import com.esd.vacationapi.repositories.FuncionarioRepository;


@SpringBootApplication
public class VacationapiApplication implements CommandLineRunner {
	/* ao implementar a interface CommandLineRunner, ele permite
	 * criar métodos auxiliares aqui no "main" para executar alguma
	 * ação quando a aplicação iniciar. */
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private EquipeRepository equipeRepository;
	
	@Autowired
	private FeriasRepository feriasRepository;
	
	
	
	public static void main(String[] args) {
		SpringApplication.run(VacationapiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");		
		
		Endereco end1 = new Endereco(null,  "Rua João Francisco Lisboa", "121", "BL 12 APT 101", "Várzea", "Recife", "PE", null);
		Endereco end2 = new Endereco(null, "Rua Alcino Ferreira da Paz", "200", "APT 302", "Pau Amarelo", "Paulista", "PE", null);
		Endereco end3 = new Endereco(null, "Rua da Unit", "101", "APT 101", "Boa Vista", "Recife", "PE", null);
		
		Equipe eq1 = new Equipe(null, "Bucha de Canhão");
		Equipe eq2 = new Equipe(null, "Só sei que Nada Sei");
		
		Funcionario func1 = new Funcionario(null, "Elton Dornelas", sdf.parse("01/11/1990"), sdf.parse("07/12/2018"), end1, eq1);
		Funcionario func2 = new Funcionario(null, "Amanda Santos", sdf.parse("14/08/1995"), sdf.parse("06/12/2016"), end2, eq2);
		Funcionario func3 = new Funcionario(null, "Ciço Perturbador", sdf.parse("15/06/1989"), sdf.parse("13/10/2015"), end3, eq1);
		
		end1.setFuncionario(func1);
		end2.setFuncionario(func2);
		end3.setFuncionario(func3);		
		
		funcionarioRepository.saveAll(Arrays.asList(func1, func2, func3));
		/* o saveAll aceita uma lista de objetos, o Arrays.asList cria uma 
		 * lista automática */
		
		enderecoRepository.saveAll(Arrays.asList(end1, end2, end3));
		
		eq1.getFuncionarios().addAll(Arrays.asList(func1, func3));
		eq2.getFuncionarios().addAll(Arrays.asList(func2));		
		
		equipeRepository.saveAll(Arrays.asList(eq1, eq2));
		
		Ferias fer1 = new Ferias(null, func2, sdf.parse("06/12/2018"), sdf.parse("06/01/2019"));
		
		feriasRepository.save(fer1);
	}

}
