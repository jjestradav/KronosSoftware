package com.kronos.model;

import java.time.LocalDateTime;

public class ForgotPassword {
	
	
	private String user;
	private String token;
	private LocalDateTime datetime;
	

	public ForgotPassword() {
		
	}
	public ForgotPassword(String user, int String, LocalDateTime datetime) {
		this.user = user;
		this.token = token;
		this.datetime = datetime;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public LocalDateTime getDatetime() {
		return datetime;
	}
	public void setDatetime(LocalDateTime datetime) {
		this.datetime = datetime;
	}
	
	
	
	
	
	

}
