package com.spring_restfull.restfull;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan(basePackages = "com.spring_restfull.restfull.model")
@ComponentScan(basePackages = "com.spring_restfull.*")
@EnableJpaRepositories(basePackages = "com.spring_restfull.restfull.repositories")
@EnableTransactionManagement
@EnableWebMvc
@RestController
@EnableCaching
public class DemoApplication implements WebMvcConfigurer{

	public static void main(final String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		//System.out.println(new BCryptPasswordEncoder().encode("123"));
	}

	//Metodo de configuração global de acesso aos endpoints
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
					.allowedMethods("*")
					.allowedOrigins("*")
					.allowedHeaders("*")
					.allowCredentials(false)
					.maxAge(3600);
			}
		};

}
}
