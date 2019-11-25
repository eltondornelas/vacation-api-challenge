package com.esd.vacationapi.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.esd.vacationapi.security.UserSS;


public class UserService {

	public static UserSS authenticated() {	
		// confirma usuário que está logado
		
		try {
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			//retorna o usuário logado no sistema
		}
		catch (Exception e) {
			return null;
			// pode dar uma exceção num caso de ninguém estar logado
		}
	}
}
