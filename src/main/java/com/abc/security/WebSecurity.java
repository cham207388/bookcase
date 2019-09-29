package com.abc.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static com.abc.security.SecurityConstants.*;

import com.abc.service.AuthorService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter{
	
	private final AuthorService userDetailsService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
		
	
	@Override
	public void configure(HttpSecurity http) throws Exception{
		http.csrf().disable();
		http.authorizeRequests()
			.antMatchers(HttpMethod.POST, SAVE_AUTHOR).permitAll()
			.antMatchers(HttpMethod.POST, SAVE_AUTHORS).permitAll()
			.antMatchers(HttpMethod.POST, SHUTDOWN).permitAll()
			.antMatchers(HttpMethod.GET, SWAGGER_UI).permitAll()
			.antMatchers(HttpMethod.GET, SWAGGER_API).permitAll()
			.antMatchers(HttpMethod.GET, GET_AUTHORS).permitAll()
			.and()
			.addFilter(new AuthenticationFilter(authenticationManager()))
			.addFilter(new AuthorizationFilter(authenticationManager()))
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.headers().frameOptions().disable();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}
}
