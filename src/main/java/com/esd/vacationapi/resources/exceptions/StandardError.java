package com.esd.vacationapi.resources.exceptions;

import java.io.Serializable;

public class StandardError implements Serializable {
	/*
	 * Classe de Erro padrão, utilizamos como objeto auxiliar
	 * e é a partir dele consegue informar um json informando o código http
	 * com mensagem de erro e o instante do erro .
	 * */

	private static final long serialVersionUID = 1L;
	
	private Long timestamp; //instante
	private Integer status;
	private String error;
	private String message;
	private String path;
	
	public StandardError(Long timestamp, Integer status, String error, String message, String path) {
		super();
		this.timestamp = timestamp;
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}	

}
