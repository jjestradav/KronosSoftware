package com.kronos.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.kronos.model.Department;
import com.kronos.model.User;
import com.kronos.model.UserRole;
import com.kronos.service.AccordService;
import com.kronos.service.UserRoleService;
import com.kronos.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UsersRestController {
	
	@Autowired
	private UserRoleService userRoleRepo;
	
	
	@Autowired
	private UserService userRepo;
	
	
	@GetMapping("/getUsersByDepartment/{id}")
	public List<User> getUsersByDepartment(@PathVariable("id") int id){
		try {
			Department dep= new Department(id);
			return this.userRepo.getUsersByDepartment(dep);

		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			throw new ResponseStatusException(HttpStatus.valueOf(500), "Ocurrio un Error");
		}
	
	}
	
	
	@GetMapping("/getUsersRoleByDepartment/{id}")
	public List<UserRole> getUsersRoleByDepartment(@PathVariable("id") int id){
		try {
			return this.userRoleRepo.getUserRolesByDepartment(id);

		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			throw new ResponseStatusException(HttpStatus.valueOf(500), "Ocurrio un Error");
		}
	
	}

}
