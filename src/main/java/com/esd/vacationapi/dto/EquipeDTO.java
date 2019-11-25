package com.esd.vacationapi.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.esd.vacationapi.domain.Equipe;
import com.esd.vacationapi.domain.Funcionario;

public class EquipeDTO {

	private Integer id;
	
	@NotEmpty(message="Preenchimento obrigatório")
	@Length(min = 5, max = 80, message = "O tamanho deve ser entre 5 e 80 caracteres")
	private String nome;
	
	private List<Funcionario> funcionarios = new ArrayList<>();
	
	public EquipeDTO() {
		
	}
	
	public EquipeDTO(Equipe obj) {
		id = obj.getId();
		nome = obj.getNome();
		funcionarios = obj.getFuncionarios();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Funcionario> getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(List<Funcionario> funcionarios) {
		this.funcionarios = funcionarios;
	}
	
	
}
