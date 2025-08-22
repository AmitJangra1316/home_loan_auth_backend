package com.hcl.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.entity.Customer;
import com.hcl.exception.ResourceNotFoundException;
import com.hcl.repository.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private OtpService otpService;

	
	
	public Customer register(Customer customer) {
		customer.setCreated(new Date());
		return customerRepository.save(customer);
	}
	
	public List<Customer> getCustomer() {
		return customerRepository.findAll();
	}
	
	public Customer getById(Long id) {
		return customerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("customer not found with this id "+id));
	}
	
	public void DeleteCustomer(Long id) {
	    customerRepository.deleteById(id);
	}
	
	public Customer updateCustomer(Long id , Customer customer) {
		Customer customer2 = customerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("customer not found with this id "+id));
		if(customer.getFirstName()!=null) {
			customer2.setFirstName(customer.getFirstName());
		}
		if (customer.getLastName()!=null) {
			customer2.setLastName(customer.getLastName());
		}
		if(customer.getEmail() != null) {
			customer2.setEmail(customer.getEmail());
		}
		if(customer.getDob() != null) {
			customer2.setDob(customer.getDob());
		}
		if(customer.getAdharNo() != null) {
			customer2.setAdharNo(customer.getAdharNo());
		}
		if(customer.getPanNo() != null) {
			customer2.setPanNo(customer.getPanNo());
		}
		customer2.setUpdated(new Date());
	   customerRepository.save(customer2);
	   return customer2;
	}
	
	public Customer CustomerGenerateOtp(Long id ) {
		Customer customer = customerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("customer not found with this id "+id));
		otpService.generateAndSendOtp(customer.getEmail());
		return customer;
	}
	
	
	
	public void verifyOtpAndUpdatepassword(Long id, String otp,String newPassword) {
		Customer customer = customerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("customer not found with this id "+id));
		
		if(!otpService.validateOtp(customer.getEmail(), otp)) {
			throw new RuntimeException("invalid or expired otp");
		}
		customer.setPassword(newPassword);
		customerRepository.save(customer);
	}
}

