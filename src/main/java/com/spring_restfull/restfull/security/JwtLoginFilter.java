package com.spring_restfull.restfull.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring_restfull.restfull.model.Usuario;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**Estabelece nosso gerenciador de token*/
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {
    /**Configurando o gerenciador de autenticação*/
    protected JwtLoginFilter(String url, AuthenticationManager authenticationManager) {
        /**Obriga a autenticar a url*/
        super(new AntPathRequestMatcher(url));

        /**Gerenciador de autenticação*/
        setAuthenticationManager(authenticationManager); 
    }

    /**Retorna o usuário ao processar a autenticação*/
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        /**Está pegando o token para validar*/
        Usuario usuario = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);

        
        
        /**Retorna usuário, senha e acessos*/
        return getAuthenticationManager()
            .authenticate(new UsernamePasswordAuthenticationToken(usuario.getLogin(), usuario.getPassword()));
    }
    
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        
        new JwtTokenAutenticacaoService().addAuthentication(response, authResult.getName());
    }
}