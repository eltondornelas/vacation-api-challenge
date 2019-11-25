package com.esd.vacationapi.services.exceptions;

public class VacationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public VacationException(String msg) {
		super(msg);
	}
	
	public VacationException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
