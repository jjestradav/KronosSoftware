package com.kronos.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.kronos.model.User;
import com.kronos.model.dto.UserDto;
import com.kronos.schedule.EmailServiceImpl;
import com.kronos.service.ForgotPasswordService;
import com.kronos.service.UserService;
import com.kronos.util.Util;

@RestController
@RequestMapping("/api/password")
public class PasswordRestController {

	//private final String baseUrl = "http://localhost:8080/forgotPassword/reset?username=";
	@Autowired
	private UserService usersRepo;

	@Autowired
	private ForgotPasswordService passwordRepo;

	@Autowired
	private EmailServiceImpl email;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ServletContext servletContext;

	@GetMapping("/forgot/{username}")
	public ResponseEntity forgotPassword(@PathVariable("username") String user, HttpServletRequest request) {
		Optional<User> dbUser = null;
		try {

			dbUser = usersRepo.getUserByEmail(user);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.valueOf(500), "Internal Server Error");
		}

		if (dbUser == null)
			throw new ResponseStatusException(HttpStatus.valueOf(500), "Internal Server Error");

		if (!dbUser.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no Encontrado");

		try {
			String baseUrl = String.format("%s://%s:%d/forgotPassword/reset?username=",request.getScheme(), 
					 request.getServerName(), request.getServerPort());
			String token = Util.getRandomToken();
			LocalDateTime datetime = LocalDateTime.now();
			this.passwordRepo.insertForgotPassword(user, token, datetime);
			String url = baseUrl + user + "&token=" + token;
			String message = "<h2 style='color:black'>Hola " + user + "!</h2>"
					+ "<h3 style='color:black'>Hemos recibido una solicitud de restablecimiento de contraseña para el sistema Kronos. \n"
					+ "Por favor ingrese al siguiente <b><a href='"+url+"'>link</a></b></h3>";

			email.sendSimpleMail(user, "Restablecer Contraseña del Sistema Kronos", message);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.valueOf(500), "Internal Server Error");
		}

		return ResponseEntity.ok().build();
	}

	@GetMapping("/verify/{username}/{token}")
	public ResponseEntity verify(@PathVariable("username") String user, @PathVariable("token") String token) {
		Optional<String> dbToken = null;
		try {
			dbToken = this.passwordRepo.getToken(user);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.valueOf(500), "Internal Server Error");
		}
		if (dbToken == null) {
			try {

				this.passwordRepo.deleteForgotPassword(user, token);

			} catch (Exception e) {

				throw new ResponseStatusException(HttpStatus.valueOf(500), "Internal Server Error");
			}
			throw new ResponseStatusException(HttpStatus.valueOf(410), "Token ha expirado");
		}

		if (!dbToken.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no Encontrado");

		String strToken = dbToken.get();

		if (!strToken.equals(token))
			throw new ResponseStatusException(HttpStatus.valueOf(403), "Token no coincide");

		return ResponseEntity.ok().build();

	}

	@PostMapping("/reset")
	public ResponseEntity reset(@RequestBody UserDto userDto) {

		try {

			String encodedPassword = this.passwordEncoder.encode(userDto.getPassword());
			this.usersRepo.updatePassword(userDto.getUsername(), encodedPassword);
			this.passwordRepo.deleteForgotPassword(userDto.getUsername(), userDto.getToken());
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.valueOf(500), "Internal Server Error");
		}

	}

	/*
	 * insert into T_USER (TEMPUSER,PASSWORD,DEPARTMENT,STATUS) values
	 * ('jjestradav@gmail.com','$2a$10$iCDiliiLJjGNB93sNBc.be6suYV/B.
	 * 2KeklGnEnuRsDzKC2l79bV2',2,1); insert into T_TEMPUSER(NAME,EMAIL) values
	 * ('Secretaria de Alcaldia','alcaldia@sanpablo.go.cr');
	 */

}
