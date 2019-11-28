package com.esd.vacationapi.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.esd.vacationapi.domain.Endereco;
import com.esd.vacationapi.domain.Equipe;
import com.esd.vacationapi.domain.Ferias;
import com.esd.vacationapi.domain.Funcionario;
import com.esd.vacationapi.domain.enums.Perfil;
import com.esd.vacationapi.repositories.EnderecoRepository;
import com.esd.vacationapi.repositories.EquipeRepository;
import com.esd.vacationapi.repositories.FeriasRepository;
import com.esd.vacationapi.repositories.FuncionarioRepository;

@Service 
public class DBService {
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private EquipeRepository equipeRepository;
	
	@Autowired
	private FeriasRepository feriasRepository;

	public void instantiateTestDatabase() throws ParseException {
				

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");		
		
		Endereco end1 = new Endereco(null, "Rua João Francisco Lisboa", "121", "BL 12 APT 101", "Várzea", "Recife", "PE");
		Endereco end2 = new Endereco(null, "Rua Alcino Ferreira da Paz", "200", "APT 302", "Pau Amarelo", "Paulista", "PE");
		Endereco end3 = new Endereco(null, "Rua da Conceição", "121", "APT 101", "Rio Doce", "Olinda", "PE");
		Endereco end4 = new Endereco(null, "Av. Barão de Lucena", "1005", "APT 401", "Maranguape", "Paulista", "PE");
		Endereco end5 = new Endereco(null, "Av. Manoel Borba", "1035", "Nº 505", "Piedade", "Jaboatão dos Guararapes", "PE");
		Endereco end6 = new Endereco(null, "Avenida Abdias de Carvalho", "444", "APT 104", "Tiúma", "São Lourenço da Mata", "PE");
		Endereco end7 = new Endereco(null, "Av. Conselheiro Aguiar", "1935", "APT 601", "Boa Viagem", "Recife", "PE");
		Endereco end8 = new Endereco(null, "Av. Afonso Olindense", "745", "APT 703", "Boa Vista", "Recife", "PE");
		Endereco end9 = new Endereco(null, "Av. General Polidoro", "169", "APT 304", "Timbi", "Camaragibe", "PE");
		Endereco end10 = new Endereco(null, "Av. Caxangá", "901", "APT 002", "Janga", "Paulista", "PE");
		
		
		Equipe eq1 = new Equipe(null, "Bucha de Canhão");
		Equipe eq2 = new Equipe(null, "Só sei que Nada Sei");
		Equipe eq3 = new Equipe(null, "Eu chego lá");
		
		Funcionario func1 = new Funcionario(null, "Elton Dornelas", sdf.parse("01/11/1990"), sdf.parse("01/01/2018"), end1, eq1, "eltondornelas@gmail.com", pe.encode("123"));
		func1.addPerfil(Perfil.ADMIN);
		Funcionario func2 = new Funcionario(null, "Amanda Santos", sdf.parse("14/08/1995"), sdf.parse("06/12/2017"), end2, eq2, "amanda@teste.com",  pe.encode("456"));
		Funcionario func3 = new Funcionario(null, "Cicinho Presidente Sindicato", sdf.parse("21/03/1993"), sdf.parse("27/01/2018"), end3, eq1, "cicinho@teste.com",  pe.encode("789"));
		Funcionario func4 = new Funcionario(null, "Everton Dornelas", sdf.parse("06/08/1988"), sdf.parse("30/03/2018"), end7, eq2, "everton@teste.com", pe.encode("987"));
		Funcionario func5 = new Funcionario(null, "Thiago Jacinto", sdf.parse("24/06/1997"), sdf.parse("05/05/2018"), end8, eq1, "thiago@teste.com", pe.encode("654"));
		Funcionario func6 = new Funcionario(null, "Thiago Albertins", sdf.parse("09/07/1998"), sdf.parse("04/08/2018"), end4, eq1, "thiago.albertins@teste.com",  pe.encode("321"));
		Funcionario func7 = new Funcionario(null, "Deyvson da Silva", sdf.parse("08/05/1986"), sdf.parse("29/09/2019"), end10, eq2, "deyvson@teste.com", pe.encode("111"));
		Funcionario func8 = new Funcionario(null, "Luana Vilela", sdf.parse("07/11/1984"), sdf.parse("06/01/2019"), end9, eq3, "luana@teste.com",  pe.encode("222"));
		Funcionario func9 = new Funcionario(null, "Paulo Alcantara", sdf.parse("16/08/1985"), sdf.parse("14/04/2019"), end5, eq2, "paulo@teste.com", pe.encode("333"));
		Funcionario func10 = new Funcionario(null, "Sergio Lins", sdf.parse("16/01/1981"), sdf.parse("17/12/2017"), end6, eq2, "sergio@teste.com", pe.encode("444"));
		Funcionario func11 = new Funcionario(null, "ADMIN CAST GROUP", sdf.parse("01/01/1970"), sdf.parse("22/11/2019"), null, eq3, "administrador.ferias@castgroup.com.br", pe.encode("123a"));
		func11.addPerfil(Perfil.ADMIN);
		
		end1.setFuncionario(func1);
		end2.setFuncionario(func2);
		end3.setFuncionario(func3);
		end4.setFuncionario(func6);
		end5.setFuncionario(func9);
		end6.setFuncionario(func10);
		end7.setFuncionario(func4);
		end8.setFuncionario(func5);
		end9.setFuncionario(func8);
		end10.setFuncionario(func7);
		
		funcionarioRepository.saveAll(Arrays.asList(func1, func2, func3, func4, func5, func6, func7, func8, func9, func10, func11));
		/* o saveAll aceita uma lista de objetos, o Arrays.asList cria uma 
		 * lista automática */
		
		enderecoRepository.saveAll(Arrays.asList(end1, end2, end3, end4, end5, end6, end7, end8, end9, end10));
		
		eq1.getFuncionarios().addAll(Arrays.asList(func1, func3, func5, func6));
		eq2.getFuncionarios().addAll(Arrays.asList(func4, func7, func9, func10));
		eq3.getFuncionarios().addAll(Arrays.asList(func2, func8));
		
		equipeRepository.saveAll(Arrays.asList(eq1, eq2, eq3));
		
		Ferias fer1 = new Ferias(null, func2, sdf.parse("06/12/2019"), sdf.parse("06/01/2020"));
		Ferias fer2 = new Ferias(null, func5, sdf.parse("02/02/2020"), sdf.parse("02/03/2020"));
		
		feriasRepository.saveAll(Arrays.asList(fer1, fer2));
		//feriasRepository.saveAll(Arrays.asList(fer1));
	}
}
