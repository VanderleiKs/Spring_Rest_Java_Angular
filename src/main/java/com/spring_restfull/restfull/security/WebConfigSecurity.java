package com.spring_restfull.restfull.security;

import com.spring_restfull.restfull.services.ImplUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private ImplUserDetailsService implUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /** Ativando proteção para usuários que não estão validados por token */
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())

                /** Ativando acesso à página inicial do sistema */
                .disable().authorizeRequests().antMatchers("/").permitAll().antMatchers("/index").permitAll()

                .and().cors().and().authorizeRequests()

                .antMatchers(HttpMethod.POST, "/spring/login").permitAll()

                .antMatchers(HttpMethod.OPTIONS, "/spring/login").permitAll()

                /** Redireciona após o usuário se deslogar do sistema */
                .anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")

                /** Mapeia a URL e invalida o usuário deslogado */
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))

                /** Filtra requisições de login para autenticação */
                .and()
                .addFilterBefore(new JwtLoginFilter("/login", authenticationManager()),
                        UsernamePasswordAuthenticationFilter.class)

                /**
                 * Filtra demais requisições para verificar a presença do token JWT no Header
                 * http
                 */
                .addFilterBefore(new JwtApiAutenticacaoFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /* service que irá consultar o usuário no banco de dados */
        auth.userDetailsService(implUserDetailsService)
                /* Padrao para codificação de senha */
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}