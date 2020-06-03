package com.kronos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.kronos.model.Accord;
import com.kronos.model.Department;
import com.kronos.model.NotificationDTO;
import com.kronos.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationRestController {
	
	
	@Autowired
	private NotificationService notRepo;
	
	
	@GetMapping("/getResponsables/{accNumber}")
	public List<NotificationDTO> getResponsables(@PathVariable("accNumber") String accNumber){
		
		try {
			Accord acc= new Accord();
			acc.setAccNumber(accNumber);
			return this.notRepo.resposableUsers(acc);
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
		}
	}
	
	@GetMapping("/getResponsablesDep/{accNumber}")
	public List<Department> getResponsablesDep(@PathVariable("accNumber") String accNumber){
		
		try {
			Accord acc= new Accord();
			acc.setAccNumber(accNumber);
			return this.notRepo.resposableDepartments(acc);
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
		}
	}

}
