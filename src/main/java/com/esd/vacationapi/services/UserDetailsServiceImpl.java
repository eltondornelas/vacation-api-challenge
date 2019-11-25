package com.esd.vacationapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.esd.vacationapi.domain.Funcionario;
import com.esd.vacationapi.repositories.FuncionarioRepository;
import com.esd.vacationapi.security.UserSS;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	/* impl = implementação da interface do Spring Security*/
	
	@Autowired
	private FuncionarioRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		/* recebe o usuário e retorna o UserDetails	que é o nosso UserSS */
		
		Funcionario func = repo.findByEmail(email);
		if(func == null) {
			throw new UsernameNotFoundException(email);
		}
		
		return new UserSS(func.getMatricula(), func.getEmail(), func.getSenha(), func.getPerfis());
	}
	
}
