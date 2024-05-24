package com.emailservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.emailservice.emailconfig.EmailConfiguration;

import java.util.HashMap;


@Service
public class EmailService {
	
	@Value("${from}")
	private String from;
	
	@Autowired
	Environment environment;
	
	public ResponseEntity<Object> sendEmail(String to, String cc, String subject, String message, MultipartFile[] files) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		String successMessage = "";
		
		String password = environment.getProperty("password");
		
		System.out.println("Email id -" + from);
		System.out.println("Password -" + password);

		if(!to.equals("") && cc.equals("")) {
		successMessage = EmailConfiguration.sendMail(from, password, to, subject, message);
		System.out.println(successMessage);
		System.out.println("sendMail");
		}else if(!cc.equals("") && (files == null || files.length == 0)) {
		successMessage = EmailConfiguration.sendMailWithCC(from, password, to, cc, subject, message);
		System.out.println(successMessage);
		System.out.println("sendMailWithCC");
		}else if(files != null && (files.length > 0)){  
		successMessage = EmailConfiguration.sendMailWithAttachment(from, password, to, cc, subject, message, files);
		System.out.println(successMessage);
	    System.out.println("sendMailWithAttachment");
		}
		map.put("from",from);
		map.put("message", successMessage);
		map.put("status", "200");

		return new ResponseEntity<Object>(map,HttpStatus.OK);
	}
	

}
