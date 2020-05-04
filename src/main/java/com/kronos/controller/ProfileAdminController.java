package com.kronos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.cloud.storage.Acl.User;
import com.kronos.service.TempUserService;
import com.kronos.service.UserService;

@Controller
@RequestMapping(value= "/profileAdmin")
public class ProfileAdminController {
	
	@Autowired
	private TempUserService tmp;
	@Autowired 
	private UserService user;

	@GetMapping("/forgotPassword")
	public String changePassword( User user, Model model) {
		//model.addAttribute("user", user);
		return "password/forgotPassword";
		
	}
	  		
	@PostMapping("/savePassword")
	public String savePassword() {
		return "profile/changePassword";
		
	}
	
	
}