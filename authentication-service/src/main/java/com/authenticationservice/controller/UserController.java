package com.authenticationservice.controller;

import org.springframework.web.bind.annotation.RestController;

import com.authenticationservice.entity.AuthRequest;
import com.authenticationservice.entity.UserInfo;
import com.authenticationservice.service.JwtService;
import com.authenticationservice.service.UserInfoService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class UserController {

	    @Autowired
	    private UserInfoService userInfoService;
	    @Autowired
	    private AuthenticationManager authenticationManager;
	    @Autowired
	    private JwtService jwtService;

		/*
		 * @Autowired private BlackList blackList;
		 */

	    @GetMapping("/welcome")
	    public String welcome(){
	        return "Welcome to Spring Security !!";
	    }

	    @PostMapping("/addUser")
	    public String addUser(@RequestBody UserInfo userInfo){
	        return userInfoService.addUser(userInfo);
	    }
	    @PostMapping("/login")
		public ResponseEntity<Object> addUser(@RequestBody AuthRequest authRequest) {
	    	HashMap<String, String> map = null;
	    	try {
		    map = new HashMap<>();
			Authentication authenticate = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
			if (authenticate.isAuthenticated()) {
				String token = jwtService.generateToken(authRequest.getUserName());
				map.put("Token", token);
				map.put("message", "User Login Success");
				map.put("status", "200");
				if (!token.equals("")) {
					
					UserInfo info = new UserInfo();
					info.setUsername(authRequest.getUserName());
				    info.setToken(token);
					userInfoService.updateToken(info);
				}
				return new ResponseEntity<>(map,HttpStatus.OK);
			} else {
				map.put("Token", "");
				map.put("message", "User Login Failed");
				map.put("status", "500");
				return new ResponseEntity<>(map,HttpStatus.OK);

			}
	    	}catch (UsernameNotFoundException e) {
				e.printStackTrace();
			}
	    	return new ResponseEntity<>(map,HttpStatus.OK);
		}
	    
	    @PostMapping("/logout")
	    //@PreAuthorize("hasAuthority('USER_ROLES') or hasAuthority('ADMIN_ROLES')")
	    public String logoutUser(HttpServletRequest request){
	        String authHeader = request.getHeader("Authorization");
	        String token= null;
	        if(authHeader !=null && authHeader.startsWith("Bearer")){
	            token = authHeader.substring(7);
	        }
	       // blackList.blacKListToken(token);
	        return "You have successfully logged out !!";
	    }

	    @GetMapping("/getUsers")
	   // @PreAuthorize("hasAuthority('ADMIN_ROLES') or hasAuthority('USER_ROLES')")
	    public List<UserInfo> getAllUsers(){
	        return userInfoService.getAllUser();
	    }
	    
	    @GetMapping("/getUsers/{id}")
	   // @PreAuthorize("hasAuthority('USER_ROLES')")
	    public UserInfo getAllUsers(@PathVariable Integer id){
	        return userInfoService.getUser(id);
	    }
	    
	
}
