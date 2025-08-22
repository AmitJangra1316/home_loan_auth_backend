package com.hcl.service;

import java.util.Date;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.hcl.entity.Customer;
import java.security.Key;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private Long expriration;


	private Key getSignKey() {
		return Keys.hmacShaKeyFor(secret.getBytes());
	}

	public String genrateToken(Customer customer) {
		return Jwts.builder().setSubject(customer.getFirstName()).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expriration))
				.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	}

	public String extractUsername(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSignKey())
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}

	public boolean validateToken(String token, Customer customer) {
		String username = extractUsername(token);
		System.out.println("validate token for user " + customer.getFirstName());
		return username.equals(customer.getFirstName()) && !isTokenExpiry(token);
	}

	private boolean isTokenExpiry(String token) {
		Date expiration = Jwts.parserBuilder()
				.setSigningKey(getSignKey())
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getExpiration();
		return expiration.before(new Date());
	}

}
