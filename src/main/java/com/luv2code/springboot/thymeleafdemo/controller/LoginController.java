package com.luv2code.springboot.thymeleafdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
	
	@GetMapping("/errorAuth")
	public String showErrorAuthorization() {
		return "errorAuth";
	}
	
	@GetMapping("showMyLoginPage")
	public String showMyLogin() {
//		return "plain-login";
		return "bootstrap-login";
	}

}
