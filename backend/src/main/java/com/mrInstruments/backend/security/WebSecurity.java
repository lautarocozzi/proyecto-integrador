package com.mrInstruments.backend.security;

import com.mrInstruments.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Map;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebMvc
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		prePostEnabled = false, securedEnabled = false, jsr250Enabled = true
)
public class WebSecurity extends WebSecurityConfigurerAdapter {


	@Autowired
	private UserService usuarioService;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider(){
		DaoAuthenticationProvider provider= new DaoAuthenticationProvider();
		provider.setPasswordEncoder(bCryptPasswordEncoder);
		provider.setUserDetailsService(usuarioService);
		return provider;
	}
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		/*
		 * 1. Se desactiva el uso de cookies
		 * 2. Se activa la configuración CORS con los valores por defecto
		 * 3. Se desactiva el filtro CSRF
		 * 4. Se indica que el login no requiere autenticación
		 * 5. Se indica que el resto de URLs esten securizadas
		 */
		httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().cors().and()
				.csrf().disable().authorizeRequests()
				.antMatchers(AUTH_LIST).permitAll()
				.antMatchers(Constants.LOGIN_URL).permitAll()
				.antMatchers("/stock/**").hasRole("ADMIN")
				.antMatchers("/pedidos/**").hasAnyRole("ADMIN","USER")
				.anyRequest().authenticated().and()
				//.httpBasic().authenticationEntryPoint(swaggerAuthenticationEntryPoint()).and()
				.addFilterBefore(new JWTAuthenticationFilter(Constants.LOGIN_URL, authenticationManager()), UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(new JWTAuthorizationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Se define la clase que recupera los usuarios y el algoritmo para procesar las
		// passwords
		auth.authenticationProvider(daoAuthenticationProvider());
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());// Por defecto solo permite los metodos GET, POST y HEAD
		Map<String, CorsConfiguration> corsConfiguration = source.getCorsConfigurations();
		for (Map.Entry<String,CorsConfiguration> entry : corsConfiguration.entrySet()){//Agrego los metodos DELETE y PUT al corsConfiguration
			entry.getValue().addAllowedMethod("DELETE");
			entry.getValue().addAllowedMethod("PUT");
		}
		return source;
	}

	private static final String[] AUTH_LIST = {
			// -- swagger ui
			"**/swagger-resources/**",
			"/swagger-resources/**",
			"/swagger-ui.html",
			"/v2/api-docs",
			"/webjars/**",
			"/swagger-ui/**",
			"/productos/**",
			"/categorias/**",
			"/caracteristicas/**",
			"/usuarios/**"
	};
}
