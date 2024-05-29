package com.gatway.feignclient;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeignController {
	
private final ClientApi clientApi;
    
    @Autowired
    public FeignController(ClientApi clientApi) {
        this.clientApi = clientApi;
    }
	
    @GetMapping("/getUserDetails")
    public List<UserInfo> getUserDetails() {
        return clientApi.getAllUsers();
    }
    
    @GetMapping("/welcomePage")
    public String welcome() {
    	return clientApi.welcome();
    }

}
