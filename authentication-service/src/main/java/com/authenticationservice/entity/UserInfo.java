package com.authenticationservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;
	    private String username;
	    private String email;
	    private String roles;
	   //@JsonIgnore
	    private String password;
	    private String token;
	    
	    
		public String getToken() {
			return token;
		}
		public void setToken(String token) {
			this.token = token;
		}
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getRoles() {
			return roles;
		}
		public void setRoles(String roles) {
			this.roles = roles;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}

	    
	    
}
