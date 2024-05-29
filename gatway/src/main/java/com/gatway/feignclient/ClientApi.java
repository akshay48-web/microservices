package com.gatway.feignclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


//@FeignClient(url = "http://localhost:9096" , value = "Authentication-Service-Client")
//below line work with same service but different ports. / Load balancing chapter
@FeignClient(name = "AUTHENTICATION-SERVICE")
public interface ClientApi {
	
	 @GetMapping(value = "/auth/getUsers")
	 public List<UserInfo> getAllUsers();
	 
	 @GetMapping("/auth/welcome")
	 public String welcome();
	

}
