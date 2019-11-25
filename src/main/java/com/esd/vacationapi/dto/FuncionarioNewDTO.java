package com.esd.vacationapi.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class FuncionarioNewDTO {
	
	private String nome;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date dataNascimento;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date dataContratacao;
	
	private String rua;	
	private String numero;	
	private String complemento;
	private String bairro;
	private String cidade;
	private String estado;
	
	private String email;
	private String senha;
	
	private String equipe;
	
	public FuncionarioNewDTO() {
		
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Date getDataContratacao() {
		return dataContratacao;
	}

	public void setDataContratacao(Date dataContratacao) {
		this.dataContratacao = dataContratacao;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEquipe() {
		return equipe;
	}

	public void setEquipe(String equipe) {
		this.equipe = equipe;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
