package com.hcl.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

	@Autowired
	private EmailService emailService;
	
	private Map<String,OtpData> otpStore = new HashMap<>();
	
	public void generateAndSendOtp(String email) {
		String otp = generateOtp(6);
		otpStore.put(email, new OtpData(otp,LocalDateTime.now().plusMinutes(5)));
		
		String subject = "password update OTP";
		String body = "Your Otp for password change id :" +otp+"\n\n this OTP will expire in 5 minutes";
		
		emailService.sendMail(otp, subject, body);
	}
	
	public boolean validateOtp(String email, String otp) {
		OtpData data = otpStore.get(email);
		if(data == null) {
			return false;
		}
		if(data.expiry.isBefore(LocalDateTime.now())) {
			otpStore.remove(email);
			return false;
		}
		return data.otp.equals(otp);
	}
	
	private String generateOtp(int length) {
		Random random = new Random();
		StringBuilder builder = new StringBuilder();
		for(int i =0;i<length;i++) {
			builder.append(random.nextInt(10));
		}
		return builder.toString();
	}
	
	
	
	public static class OtpData{
		String otp;
		LocalDateTime expiry;
		
		OtpData(String otp, LocalDateTime expiry){
			this.otp = otp;
			this.expiry = expiry;
		}
	}
}
