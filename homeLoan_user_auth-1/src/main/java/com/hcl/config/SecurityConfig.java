package com.hcl.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.hcl.security.JwtAuthenticationFilter;



@Configuration
public class SecurityConfig {
	
	@Autowired
	private JwtAuthenticationFilter authenticationFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
				http.csrf(csrf -> csrf.disable())
						.authorizeHttpRequests(
								 (auth) -> {
									 auth.requestMatchers("/customer/all","/customer/{id}").hasRole("USER");
									 auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**","/auth/register","/auth/login").permitAll();
									 
					                 auth.anyRequest().authenticated();
								 })
						.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
				return http.build();
			}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
		return config.getAuthenticationManager();
	}
}
