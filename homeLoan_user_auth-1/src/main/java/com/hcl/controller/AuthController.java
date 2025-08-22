package com.hcl.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.Dto.CustomerDto;
import com.hcl.entity.Customer;
import com.hcl.entity.Role;
import com.hcl.exception.DuplicateResourceException;
import com.hcl.exception.ResourceNotFoundException;
import com.hcl.repository.CustomerRepository;
import com.hcl.repository.RoleRepository;
import com.hcl.service.JwtService;
import com.hcl.snowflake.EightDigitIdGenerator;


@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private  CustomerRepository customerRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	
	
	@PostMapping("/register")
	public Customer register(@RequestBody Customer customer ) {
		Customer isEmail = customerRepository.findByEmail(customer.getEmail()).orElse(null);
		
		if(isEmail != null) {
			throw new DuplicateResourceException("customer already exists with the email :"+customer.getEmail());
		}
			customer.setPassword(passwordEncoder.encode(customer.getPassword()));
			Long id = EightDigitIdGenerator.nextId();
			customer.setId(id);
			customer.setCreated(new Date());
			
			Role role = new Role();
			role.setName("USER_ROLE");
			Role roleUser = roleRepository.findByName("ROLE_USER").orElseGet(()-> roleRepository.save(role));
			
			customer.getRole().add(roleUser);
			customerRepository.save(customer);
			System.out.println("user created succesfully");
			return customer;
	
	
	}
	
	@PostMapping("/login")
	public String login(@RequestBody CustomerDto customer) {
		Customer customer2 = customerRepository.findByFirstName(customer.getFirstName()).orElseThrow(()-> new ResourceNotFoundException("Customer not found"));
		if(passwordEncoder.matches(customer.getPassword(), customer2.getPassword())) {
			return jwtService.genrateToken(customer2);
		}else {
			throw new RuntimeException("invalid credentials");
		}
	}
	
	

	
}
