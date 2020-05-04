package com.kronos.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.kronos.model.Accord;
import com.kronos.service.AccordService;
import com.kronos.service.StateService;

@Controller
@RequestMapping(value = "/users")
public class UserController {
	
	@Autowired
	private AccordService accordRepo;
	
	@Autowired
	private StateService statesRepo;
	

	
	//private Accord oldAccord=new Accord();
	
	
	@GetMapping("/home")
	public String userHome( @SessionAttribute("username") String username, Model model) {
		
		try {
			model.addAttribute("listAccords", this.accordRepo.searchByUser(username));
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return "users/usersHome";
	}
	
	@GetMapping("/edit/{accNumber}")
	public String goToEdit(@PathVariable("accNumber") String accNumber, Model model) {

		try {
			Optional<Accord> opt = this.accordRepo.getAccord(accNumber);
			if (!opt.isPresent())
				throw new Exception("No se encontro el acuerdo");

			Accord acc = opt.get();
			model.addAttribute("accord", acc);
			//this.oldAccord=acc;
			
			
		
		}

		catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		return "users/usersView";
	}
	
	@ModelAttribute
	public void setGenericos(Model model) {
	
		model.addAttribute("states", this.statesRepo.findAll());

	}
}
