package com.kronos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kronos.service.TypeService;
import com.kronos.model.Type;

@Controller
@RequestMapping(value="/commission")
public class CommissionController {
	
	@Autowired
	private TypeService typeServiceRepo;
	
	@GetMapping("/addCommission")
	public String addCommision( Type type, Model model) {
		model.addAttribute("type", type);
		return "commission/commissionForm";
	}
	
}
