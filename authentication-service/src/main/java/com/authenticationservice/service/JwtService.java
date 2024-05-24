package com.authenticationservice.service;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;


import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtService {
	
	/*
	 * @Autowired private BlackList blackList;
	 */
	    private static final String SECERET = "!@#$FDGSDFGSGSGSGSHSHSHSSHGFFDSGSFGSSGHSDFSDFSFSFSFSDFSFSFSF";

	    
	    //Header payload signature = jwttoken
	    public String generateToken(String userName){
	        Map<String, Objects> claims = new HashMap<>();
	        return Jwts.builder()
	                .setClaims(claims)
	                .setSubject(userName)
	                .setIssuedAt(new Date(System.currentTimeMillis()))
	                .setExpiration(new Date(System.currentTimeMillis()+1000*60*30))//30 min
	                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	    }

	    private Key getSignKey() {
	        byte[] keyBytes = Decoders.BASE64.decode(SECERET);
	        return Keys.hmacShaKeyFor(keyBytes);
	    }
	    public String extractUserName(String token){
	        return extractClaim(token,Claims::getSubject);
	    }

	    public Date extractExpiration(String token){
	        return extractClaim(token,Claims::getExpiration);
	    }
	    private <T> T extractClaim(String token, Function<Claims,T> claimResolver) {
	        final Claims claims = extractAllClaims(token);
	        return claimResolver.apply(claims);
	    }

	    private Claims extractAllClaims(String token){
	        return Jwts.parserBuilder()
	                .setSigningKey(getSignKey())
	                .build()
	                .parseClaimsJws(token)
	                .getBody();
	    }
	    private Boolean isTokenExpired(String token){
	        return extractExpiration(token).before(new Date());
	    }
	    public Boolean validateToken(String token, UserDetails userDetails){
	        final String userName= extractUserName(token);
			return (userName.equals(userDetails.getUsername())
					&& !isTokenExpired(token) /* && !blackList.isBlackListed(token) */);
	    }
	
	
	  

}
