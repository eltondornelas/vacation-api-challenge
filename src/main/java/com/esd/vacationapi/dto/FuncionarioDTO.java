package com.esd.vacationapi.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.esd.vacationapi.domain.Funcionario;
import com.fasterxml.jackson.annotation.JsonFormat;

public class FuncionarioDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer matricula;
	
	@NotEmpty(message="Preenchimento obrigatório")
	@Length(min = 5, max = 80, message = "O tamanho deve ser entre 5 e 80 caracteres")
	private String nome;
	/* tags de validações */

	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date dataContratacao;
	
	@Email(message = "Email inválido")
	private String email;
	
	public FuncionarioDTO() {
		
	}
	
	public FuncionarioDTO(Funcionario obj) {
		matricula = obj.getMatricula();
		nome = obj.getNome();
		email = obj.getEmail();
		dataContratacao = obj.getDataContratacao();		
	}

	public Integer getId() {
		return matricula;
	}

	public void setId(Integer id) {
		this.matricula = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataContratacao() {
		return dataContratacao;
	}

	public void setDataContratacao(Date dataContratacao) {
		this.dataContratacao = dataContratacao;
	}

	public Integer getMatricula() {
		return matricula;
	}

	public void setMatricula(Integer matricula) {
		this.matricula = matricula;
	}
	
	
}
