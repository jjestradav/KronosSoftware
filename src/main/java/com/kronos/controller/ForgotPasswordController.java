package com.kronos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/forgotPassword")
public class ForgotPasswordController {
	
	@GetMapping("/reset")
	public String reset() {
		
		return "password/resetPassword";
	}

}
