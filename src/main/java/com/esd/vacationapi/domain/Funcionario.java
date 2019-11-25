package com.esd.vacationapi.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.esd.vacationapi.domain.enums.Perfil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Funcionario implements Serializable {
	// sem ter colocado a dependência do JPA ele não enxerga nem @Entity nem o @Id
	
	private static final long serialVersionUID = 1L;	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer matricula;
	// @GeneratedValue(strategy=GenerationType.IDENTITY): geração de chave primária
	
	@NotEmpty(message="Preenchimento obrigatório")
	@Length(min = 5, max = 80, message = "O tamanho deve ser entre 5 e 80 caracteres")
	private String nome;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date dataNascimento;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date dataContratacao;
	
	@Column(unique=true)  // dessa forma garante que não terá repeticão no banco de dados
	private String email; 
	
	@JsonIgnore  // para que não retorne a senha quando puxar o json
	private String senha;
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "funcionario")  // é necessário para não ocorrer entidade transiente quando salvar funcionario e o endereco  
	private Endereco endereco;	
	
	@JsonIgnore  // evitando uma relação cíclica
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "equipe_id")  // criando uma nova coluna com referência ao id da equipe; Chave Estrangeira 
	private Equipe equipe;	
	
	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "funcionario")
	private Ferias ferias;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name="PERFIS") //nome da tabela será PERFIS
	private Set<Integer> perfis = new HashSet<>();
	
	private String imageUrl;
	
	public Funcionario() {
		addPerfil(Perfil.FUNCIONARIO);
	}

	public Funcionario(Integer matricula, String nome, Date dataNascimento, Date dataContratacao,
			Endereco endereco, Equipe equipe, String email, String senha) {
		super();
		this.matricula = matricula;
		this.nome = nome;
		this.dataNascimento = dataNascimento;
		this.dataContratacao = dataContratacao;		
		this.endereco = endereco;
		this.equipe = equipe;
		this.email = email;
		this.senha = senha;
		addPerfil(Perfil.FUNCIONARIO);
	}

	public Integer getMatricula() {
		return matricula;
	}

	public void setMatricula(Integer matricula) {
		this.matricula = matricula;		
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
		
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}	
	
	public Set<Perfil> getPerfis() {
		return perfis.stream().map(x -> Perfil.toEnum(x)).collect(Collectors.toSet());
	}
	
	public void addPerfil(Perfil perfil) {
		perfis.add(perfil.getCod());
	}

	//hash e equals apenas pelo id
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((matricula == null) ? 0 : matricula.hashCode());
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
		if (matricula == null) {
			if (other.matricula != null)
				return false;
		} else if (!matricula.equals(matricula))
			return false;
		return true;
	}

	
	
}
