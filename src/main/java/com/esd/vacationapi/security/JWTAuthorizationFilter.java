package com.esd.vacationapi.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
	/* 
	 * Esse filtro analisa o token pra ver se ele é válido e partir desse token ele extrai o usuário
	 * busca no bd pra verificar se ele existe mesmo, vamos fazer a verificação pelo email
	 *  */
		
	private JWTUtil jwtUtil;

	private UserDetailsService userDetailsService;

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil,
			UserDetailsService userDetailsService) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
		//ele vai validar o token (é o que vem depois do "Bearer") gerado no /login do usuário e por isso precisa extrair o usuário (UserDetailService) e verificar se ele existe
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
				HttpServletResponse response, FilterChain chain) 
					throws IOException, ServletException {
		//Ele executa antes de deixar a requisição continuar
		
		String header = request.getHeader("Authorization");
		//é o token gerado na chave Header(Cabeçalho)
		//Authorization é o nome da chave lá no Postman
		
		// esse if é o procedimento para liberar a autorização do usuário que está tentando acessar o endpoint
		if (header != null && header.startsWith("Bearer ")) {
			UsernamePasswordAuthenticationToken auth = getAuthentication(header.substring(7));
			//substring 7 -> significa que vai ignorar os 7 primeiros caracteres, que é o "Bearer "

			if (auth != null) {
				SecurityContextHolder.getContext().setAuthentication(auth);
				//se entrar no esta tudo certo. Libera o usuário
			}
		}
		chain.doFilter(request, response);
		// se tudo acima passar, o chain continua a passar a requisição após os testes
	}

	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		if (jwtUtil.tokenValido(token)) {
			String username = jwtUtil.getUsername(token);
			UserDetails user = userDetailsService.loadUserByUsername(username);
			//busca o usuário no DB;
			
			return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		}
		return null;
	}
}
