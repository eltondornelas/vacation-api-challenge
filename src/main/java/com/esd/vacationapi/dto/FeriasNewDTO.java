package com.esd.vacationapi.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class FeriasNewDTO {

	//@NotEmpty(message="Preenchimento obrigatório")  // não roda
	private Integer matricula;
			
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date inicioFerias;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date finalFerias;	
	
	public FeriasNewDTO() {
		
	}

	public Integer getMatricula() {
		return matricula;
	}

	public void setMatricula(Integer matricula) {
		this.matricula = matricula;
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
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy");
		
		return "matricula : " + matricula
				+ "\ninicioFerias : " + sdf.format(inicioFerias)
				+ "\nfinalFerias : " + sdf.format(finalFerias);
	}
	
	
}
