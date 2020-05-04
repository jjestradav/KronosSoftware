package com.kronos.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import com.kronos.service.StateService;
import com.kronos.service.TypeService;
import com.kronos.service.UserService;
import com.kronos.model.Accord;
import com.kronos.model.State;
import com.kronos.model.Type;
import com.kronos.model.User;
import com.kronos.service.AccordService;;

@Controller
public class HomeController {
//
//	 @Autowired
//	    JdbcTemplate jdbcTemplate;
//	 
	@Autowired
	private TypeService typeRepo;

	@Autowired
	private StateService stateRepo;

	@Autowired
	private UserService userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	
	
	//redirect the user to the index page
	@GetMapping("/")
	public String mostrarHome( HttpSession session) {

			return "redirect:/index";
		
			
		
	}

	@GetMapping("/forbidden")
	public String forbidden() {
		return "accessDenied";
	}

	@GetMapping("/login")
	public String login() {
		
		return "login";
	}
	
	//every role has a own index page
	//this method gets the login credentials and store it in the session
	@GetMapping("/index")
	public String showIndex(Authentication auth, HttpSession session) {

		try {
			String username = auth.getName();
			Optional<User> opt = this.userRepo.getUserByEmail(username);
			if (opt.isPresent()) {
				User user = opt.get();
				user.setPassword(null);
				
					session.setAttribute("user", user);

				String role = "";
				for (GrantedAuthority rol : auth.getAuthorities()) {
					role = rol.getAuthority();
				}
				session.setAttribute("roleName", role);
				session.setAttribute("username", user.getTempUser().getEmail());
				session.setAttribute("userDepartment", user.getDepartment().getId());

				//PdfMaker.generatePdf("", new Accord());

				
				if(user.getIsBoss())
					return "redirect:/boss/home";
				
				
				switch(role) {
				
				case "Concejo Municipal":
					return "redirect:/accords/list";
				case "Secretaria de Alcaldia":
					return "redirect:/townHall/homeTownHall";
				default: 
					return "redirect:/users/home";
				
				}
				
				
				
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return "index";
	}
	
	//logout action
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(request, null, null);
		return "redirect:/";
	}
	
	//custom login error message
	@GetMapping("/login-error")
    public String login(HttpServletRequest request, Model model) {
		
		
        HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                errorMessage = "Usuario o contraseña incorrectos";
            }
        }
        model.addAttribute("errorMessage", errorMessage);
        return "login";
    }
}
