package com.esd.vacationapi.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Ferias implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToOne
	@JoinColumn(name = "funcionario_id")
	private Funcionario funcionario;

	@JsonFormat(pattern = "dd/MM/yyyy")  // mascara padrão
	private Date inicioFerias;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date finalFerias;
	
	public Ferias() {
		
	}

	public Ferias(Integer id, Funcionario funcionario, Date inicioFerias, Date finalFerias) {
		super();
		this.id = id;
		this.funcionario = funcionario;
		this.inicioFerias = inicioFerias;
		this.finalFerias = finalFerias;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}	

	public Date getInicioFerias() {
		return inicioFerias;
	}

	public void setInicioFerias(Date inicioFerias) {
		this.inicioFerias = inicioFerias;
	}

	public Date getFinalFerias() {
		return finalFerias;
	}

	public void setFinalFerias(Date finalFerias) {
		this.finalFerias = finalFerias;
	}
	
		
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
		Ferias other = (Ferias) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy");

//		StringBuilder builder = new StringBuilder();
//		builder.append("nome : ");
//		builder.append(funcionario.getNome());
//		builder.append("matricula : ");
//		builder.append(funcionario.getMatricula());
//		builder.append("email : ");
//		builder.append(funcionario.getEmail());
//		builder.append("inicioFerias : ");
//		builder.append(sdf.format(inicioFerias));
//		builder.append("finalFerias : ");
//		builder.append(sdf.format(finalFerias));
//		builder.append("equipe : ");
//		builder.append(funcionario.getEquipe().getNome());
//		
//		return builder.toString();
	
		
		return "nome : " + funcionario.getNome()
				+ "\nmatricula : " + funcionario.getMatricula()
				+ "\nemail : " + funcionario.getEmail()
				+ "\ninicioFerias : " + sdf.format(inicioFerias)
				+ "\nfinalFerias : " + sdf.format(finalFerias)
				+ "\nequipe : " + funcionario.getEquipe().getNome();
		
	}

	
	
	
	

}
