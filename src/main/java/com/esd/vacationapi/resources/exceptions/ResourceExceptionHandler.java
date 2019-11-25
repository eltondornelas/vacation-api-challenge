package com.esd.vacationapi.resources.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.esd.vacationapi.services.exceptions.DataIntegrityException;
import com.esd.vacationapi.services.exceptions.ObjectNotFoundException;
import com.esd.vacationapi.services.exceptions.VacationException;

@ControllerAdvice
public class ResourceExceptionHandler {
	
	@ExceptionHandler(ObjectNotFoundException.class)  // explicitando que é um tratador de exceções do tipo ObjectNotFoundException
	public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request) {
		// ObjectNotFoundException que criamos
		
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.NOT_FOUND.value(), "Não encontrado", e.getMessage(), request.getRequestURI());  // horário local do sistema
		// o .value() é para retorna o valor inteiro do erro
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
		// body(err) é o corpo da mensagem
	}
	
	@ExceptionHandler(DataIntegrityException.class)
	public ResponseEntity<StandardError> dataIntegrity(DataIntegrityException e, HttpServletRequest request) {
				
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), "Integridade de dados", e.getMessage(), request.getRequestURI());				
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);		
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {
		ValidationError err = new ValidationError(System.currentTimeMillis(), HttpStatus.UNPROCESSABLE_ENTITY.value(), "Erro de validação", e.getMessage(), request.getRequestURI());

		for (FieldError x : e.getBindingResult().getFieldErrors()) {
			err.addError(x.getField(), x.getDefaultMessage());
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);		
	}
	
	@ExceptionHandler(VacationException.class)
	public ResponseEntity<StandardError> vacationError(VacationException e, HttpServletRequest request) {
				
		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), "Erro de validação", e.getMessage(), request.getRequestURI());				
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);		
	}
}
