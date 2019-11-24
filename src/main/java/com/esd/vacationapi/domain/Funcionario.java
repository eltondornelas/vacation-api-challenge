package com.esd.vacationapi.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Funcionario implements Serializable {
	// sem ter colocado a dependência do JPA ele não enxerga nem @Entity nem o @Id
	
	private static final long serialVersionUID = 1L;
	
	private static int cod = 10;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)  
	private Integer id;
	// @GeneratedValue(strategy=GenerationType.IDENTITY): geração de chave primária
	
	private String nome;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date dataNascimento;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date dataContratacao;	
		
	// @GeneratedValue(strategy=GenerationType.AUTO)
	private String matricula;  // gerada automaticamente pelo sistema
	
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "funcionario")  // é necessário para não ocorrer entidade transiente quando salvar funcionario e o endereco  
	private Endereco endereco;	
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "equipe_id")  // criando uma nova coluna com referência ao id da equipe 
	private Equipe equipe;	
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "funcionario")
	private Ferias ferias;

	
	public Funcionario() {
		
	}

	public Funcionario(Integer id, String nome, Date dataNascimento, Date dataContratacao,
			Endereco endereco, Equipe equipe) {
		super();
		this.id = id;
		this.nome = nome;
		this.dataNascimento = dataNascimento;
		this.dataContratacao = dataContratacao;
		this.matricula = nome.substring(0, 3).toLowerCase() + cod;
		this.endereco = endereco;
		this.equipe = equipe;
		cod++;
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

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
		cod++;
	}	

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}	

	public Equipe getEquipe() {
		return equipe;
	}

	public void setEquipe(Equipe equipe) {
		this.equipe = equipe;
	}

	public Ferias getFerias() {
		return ferias;
	}

	public void setFerias(Ferias ferias) {
		this.ferias = ferias;
	}

	//hash e equals apenas pelo id
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Funcionario other = (Funcionario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
