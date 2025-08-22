package com.hcl.security;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hcl.entity.Customer;
import com.hcl.repository.CustomerRepository;
import com.hcl.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authHeader = request.getHeader("Authorization");
		
		if(authHeader == null || authHeader.startsWith("bearer")) {
			filterChain.doFilter(request, response);
			return ;
		}
		
		String jwtToken = authHeader.substring(7);
		
		String customerName = jwtService.extractUsername(jwtToken);
		
		if(customerName !=null && SecurityContextHolder.getContext().getAuthentication()==null) {
			Customer customer = customerRepository.findByFirstName(customerName).orElse(null);
			
			if(customer!=null && jwtService.validateToken(jwtToken,customer)) {
				UsernamePasswordAuthenticationToken authToken =  new UsernamePasswordAuthenticationToken(customer,null,
						customer.getRole().stream()
		                .map((role) -> new SimpleGrantedAuthority(role.getName()))
		                .collect(Collectors.toSet()));
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
		}
			filterChain.doFilter(request, response);
			
	
			
	}
	}

}
