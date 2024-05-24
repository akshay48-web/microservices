package com.emailservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.emailservice.service.EmailService;

@RestController
public class EmailController {
	
	EmailService email;
		
	public EmailController(EmailService email) {
		super();
		this.email = email;
	}


	//E-mail service SMTP
	
		@RequestMapping(value= "/sendEmail" ,method = RequestMethod.POST, produces = "application/json;charset=utf-8" )
	    public ResponseEntity<Object> sendEmail(@RequestParam("to") String to,@RequestParam("cc") String cc,@RequestParam("subject") String subject,@RequestParam("message") String message,@RequestParam("files") MultipartFile[] files) {
	        return email.sendEmail(to,cc,subject,message,files);
	    }

}
