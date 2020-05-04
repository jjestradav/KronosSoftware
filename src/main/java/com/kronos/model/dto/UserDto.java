package com.kronos.model.dto;

public class UserDto {

	private String username;
	private String password;
	private String token;
	
	

	public String getToken() {
		return token;
	}



	public void setToken(String token) {
		this.token = token;
	}



	public UserDto() {
	}



	public UserDto(String username, String passw) {
		this.username = username;
		this.password = passw;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
	
	
	
}
