package com.authenticationservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.authenticationservice.entity.AppConstant;
import com.authenticationservice.filter.JwtFilter;
import com.authenticationservice.service.UserInfoService;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	    @Autowired
	    @Lazy
	    private JwtFilter jwtFilter;
	    
	    @Bean
	    public UserDetailsService userDetailsService(){
	        return new UserInfoService();
	    }

	    @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
	        return httpSecurity.csrf(csrf->csrf.disable())
	        		.cors(cors->cors.disable())
					.authorizeHttpRequests(
							auth -> auth/* .requestMatchers("/myapp/**").authenticated() */
							.requestMatchers(AppConstant.URLS_TO_EXCLUDE)//NoAuth
	                        .permitAll()
	                        .requestMatchers("/addUser").hasAuthority("Admin")//Grant access to URLs starting with "/admin" only to users with the role "ADMIN"
	                        .anyRequest().authenticated())
	                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	                .authenticationProvider(authenticationProvider())
	                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
	                .build();
	    }

	    @Bean
	    public AuthenticationProvider authenticationProvider() {
	        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
	        authenticationProvider.setUserDetailsService(userDetailsService());
	        authenticationProvider.setPasswordEncoder(passwordEncoder());
	        return authenticationProvider;
	    }

	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	    @Bean
	    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
	        return config.getAuthenticationManager();
	    }
	
	

}
