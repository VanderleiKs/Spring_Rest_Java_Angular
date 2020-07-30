package com.spring_restfull.restfull.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.spring_restfull.restfull.ApplicationContextLoad;
import com.spring_restfull.restfull.model.Usuario;
import com.spring_restfull.restfull.repositories.UsuarioRepository;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Component
public class JwtTokenAutenticacaoService {

    /** Validaade do token de 2 dias */
    private static final long EXPIRATION_TIME = 86400000;
    /** Uma senha única para compor a autenticação e ajudar na segurança */
    private static final String SECRET = "secretSenha";
    /** Prefixo padrão do token */
    private static final String TOKEN_PREFIX = "Bearer";
    private static final String HEADER_STRING = "Authorization";

    /**
     * Gerando token autenticado e adicionando ao cabeçalho HTTP
     * 
     * @throws IOException
     */

    public void addAuthentication(final HttpServletResponse response, final String username) throws IOException {

        /** Montagem do token */
        final String JWT = Jwts.builder() /** Chama gerador de token */
                .setSubject(username) /** Adiciona usuário */
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))/** Tempo para expiração */
                .signWith(SignatureAlgorithm.HS512, SECRET).compact(); /** Compactação e geração do algoritmo */

        /** Operação de cadastro de token no banco de dados */
        Usuario usuario = ApplicationContextLoad.getApplicationContext().getBean(UsuarioRepository.class)
                .findUserByLogin(username);

        usuario.setToken(JWT);
        ApplicationContextLoad.getApplicationContext().getBean(UsuarioRepository.class).save(usuario);

        /** Junta token com o prefixo */
        final String token = TOKEN_PREFIX + " " + JWT; /** fica algo assim: Bearer codigotoken */

        /** Adiciona cabeçalho */
        response.addHeader(HEADER_STRING, token); /** fica algo assim: Authorization: Bearer codigotoken */

        /** Escreve o token como resposta no corpo http */
        response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
    }

    public Authentication getAuthentication(final HttpServletRequest request, HttpServletResponse response) {
        /** Pega o token enviado no cabeçalho */
        final String token = request.getHeader(HEADER_STRING);

        try {
            /** Inicio Verificação */
            if (token != null) {

                String tokenSBeare = token.replace(TOKEN_PREFIX, "").trim();

                /** Faz a validação do token do usuário na requisição */
                final String user = Jwts.parser().setSigningKey(SECRET) /** vem algo assim: Bearer codigotoken */
                        .parseClaimsJws(tokenSBeare) /** Tira o Bearer, ficando só o codigotoken */
                        .getBody().getSubject();
                /** Pega o usuario */

                if (user != null) {
                    Usuario usuario = ApplicationContextLoad.getApplicationContext().getBean(UsuarioRepository.class)
                            .findUserByLogin(user);

                    if (usuario != null) {
                        if (tokenSBeare.equalsIgnoreCase(usuario.getToken())) {
                            return new UsernamePasswordAuthenticationToken(usuario.getLogin(), usuario.getPassword(),
                                    usuario.getAuthorities());
                        }
                    }
                }

            } /** Fim verificação token */
        } catch (ExpiredJwtException ex) {
            try {
                response.getOutputStream().println("Token Expirado, Faça novamente o Login!");
            } catch (IOException e) {
            }
        }

        return null; /** Não autorizdo */
    }
}