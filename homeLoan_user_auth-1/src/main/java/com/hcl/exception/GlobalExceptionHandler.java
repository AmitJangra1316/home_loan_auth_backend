package com.hcl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.swagger.v3.oas.annotations.Hidden;

@RestControllerAdvice
@Hidden
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException msg){
		return new ResponseEntity<>(msg.getMessage(),HttpStatus.NOT_FOUND);//404
	}
	
	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<String> handleDuplicateException(DuplicateResourceException msg){
		return new ResponseEntity<>(msg.getMessage(),HttpStatus.CONFLICT);//409
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGlobalExcpetion(Exception ex){
		return new ResponseEntity<>("Internal server error" +ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);//500
	}
}
