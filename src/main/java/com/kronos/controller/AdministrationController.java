package com.kronos.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.kronos.service.DeparmentService;
import com.kronos.service.RoleService;
import com.kronos.service.TempUserService;
import com.kronos.service.UserRoleService;
import com.kronos.service.UserService;
import com.kronos.model.TempUser;
import com.kronos.model.User;
import com.kronos.model.Department;
import com.kronos.model.Role;

@Controller
@RequestMapping(value = "/administration")
public class AdministrationController {


	@Autowired
	private TempUserService tempUserRepo;
	@Autowired
	private UserService userRepo;
	@Autowired
	private DeparmentService departmentRepo;
	@Autowired
	private RoleService roleRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRoleService userRoleRepo;

	@GetMapping("/addUser")
	public String addUser(User user, Model model) {
		model.addAttribute("user",user);
		return "administration/userForm";
	}

	@GetMapping("/addUserRole")
	public String addUserRole(User user, Role role, Model model) {
		Map<String, Object> mapa = new HashMap();
		mapa.put("user", user);
		mapa.put("role", role);
		model.addAllAttributes(mapa);
		return "administration/useRole";
	}
	
	
	@GetMapping("/listUser")
	public String addUserRole(Model model) {

		try {
		model.addAttribute("listUsersModel", this.userRepo.getAllUsers());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return "administration/listUser";
	}
	
	@GetMapping("/editUser/{id}")
	public String addUserRole(Model model,@PathVariable("id") String email) {

		try {
			Optional<User> optUser=this.userRepo.getUserByEmail(email);
			
			if(!optUser.isPresent())
				throw new Exception();

			
		model.addAttribute("userModel", optUser.get());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return "administration/viewUser";
	}
	
	
	@GetMapping("/goToProfile")
	public String goToProfile( @SessionAttribute("username") String email, Model model) {

		try {
			Optional<User> optUser=this.userRepo.getUserByEmail(email);
			
			if(!optUser.isPresent())
				throw new Exception();

			
		model.addAttribute("userModel", optUser.get());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return "administration/viewUser";
	}

	@PostMapping("/saveUserRole")
	public String saveUserRole(User user, Role role,
			@RequestParam(value = "nameRole", required = false) String nameRole) {
		try {
			if (role.getId() == -2) {
				if(nameRole.isEmpty()) {throw new Exception();}
				roleRepo.insertRole(nameRole);
				Optional<Role> opt = this.roleRepo.searchRole(nameRole);
				if (opt.isPresent()) {
					role = opt.get();
				}
			}
			userRoleRepo.insertUserRole(user, role);
				
		} catch (Exception e) {
			return "/administration/error";
		}
		return "redirect:/accords/list";
	}

	@PostMapping("/saveUser")
	public String saveUser(User user, @RequestParam(value = "nameDeparment", required = false) String nameDepartment) {

		try {
			user.setStatus(true);
			if (user.getPassword().isEmpty()) {
				throw new Exception();
			} else {
				String plainPassword = user.getPassword();
				String encryptPassword = passwordEncoder.encode(plainPassword);
				user.setPassword(encryptPassword);
			}
			if (user.getTempUser().getEmail().isEmpty() || user.getTempUser().getName().isEmpty()) {
				throw new Exception();
			} else {
				this.tempUserRepo.insertTempUser(user.getTempUser());
			}
			if (user.getDepartment().getId() == -1) {
				if (nameDepartment.isEmpty()) {
					throw new Exception();
				} else {
					this.departmentRepo.insertDepartment(nameDepartment);
				}
			}
			Optional<Department> opt = this.departmentRepo.searchDepartment(nameDepartment);
			Department dp = null;
			if (opt.isPresent()) {
				dp = opt.get();
				user.setDepartment(dp);
			}
			userRepo.insertUser(user);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			return "/administration/error";
		}

		return "redirect:/administration/addUserRole";
	}

	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}

	@ModelAttribute
	public void setGenericos(Model model) {
		model.addAttribute("departments", this.departmentRepo.findAll());
		model.addAttribute("roles", this.roleRepo.findAll());
		model.addAttribute("users", this.userRepo.searchAllUsersWithoutRole());
	}
}
