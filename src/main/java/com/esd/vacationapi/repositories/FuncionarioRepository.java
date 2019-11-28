package com.esd.vacationapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.esd.vacationapi.domain.Funcionario;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer> {
	//classe/interface capaz de acessar o db fazer consulta pra acessar os dados da tabela funcionário
	/*
	 * 1º Parâmetro => O objeto que deseja acessar os dados
	 * 2º Parâmetro => Tipo do atributo identificador desse objeto
	 * */
	
	Funcionario findByEmail(String email);
	// dessa forma o Spring entende que quer buscar por e-mail.	
	
}
