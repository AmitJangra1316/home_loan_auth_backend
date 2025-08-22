package com.hcl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.entity.Customer;
import com.hcl.service.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

//	@PostMapping("/register")
//	public ResponseEntity<Customer> register(@RequestBody Customer customer) {
//		return ResponseEntity.status(HttpStatus.CREATED).body(customerService.register(customer));
//	}
//	
	@GetMapping("/{id}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable Long id){
		return ResponseEntity.status(HttpStatus.OK).body(customerService.getById(id));
	}

	@GetMapping("/all")
	public ResponseEntity<List<Customer>> getAll() {
		return ResponseEntity.status(HttpStatus.OK).body(customerService.getCustomer());
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Customer> updateCustomer(@PathVariable Long id , @RequestBody Customer customer){
		return ResponseEntity.status(HttpStatus.OK).body(customerService.updateCustomer(id, customer));
	}
	
	@PostMapping("/{id}/requestOtp")
	public ResponseEntity<Customer> generateOtp(@PathVariable Long id){
	  Customer customer = 	customerService.CustomerGenerateOtp(id);
		return ResponseEntity.status(HttpStatus.OK).body(customer);
	}
	
	@PostMapping("/{id}/updatePassword")
	public String updatePassword(@PathVariable Long id,@RequestParam String otp,@RequestParam String newPassword) {
		customerService.verifyOtpAndUpdatepassword(id, otp, newPassword);
		return "password Updated succesfully";
	}
}
