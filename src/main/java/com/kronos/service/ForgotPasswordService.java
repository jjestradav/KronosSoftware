package com.kronos.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ForgotPasswordService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	
	public void insertForgotPassword(String username, String token, LocalDateTime datetime) throws Exception {
		
		Connection connection=null;
		CallableStatement statement=null;
		try {
		 connection = jdbcTemplate.getDataSource().getConnection();
		 statement = connection.prepareCall("{call insertForgotPassword(?,?,?) }");
			statement.setString(1, username);
			statement.setString(2, token);
			statement.setTimestamp(3, Timestamp.valueOf(datetime));
			statement.executeUpdate();
		}
		catch(Exception e) {
			throw e;
		}
		finally {
			
			if(statement != null)
				statement.close();
			
			if(connection != null) 
				connection.close();
			
		}
	}
	
		public Optional<String> getToken(String username) throws Exception {
			Connection connection=null;
			CallableStatement statement=null;
			try {
				connection = jdbcTemplate.getDataSource().getConnection();
				 statement = connection.prepareCall("{call getRecentToken(?) }");
				 statement.setString(1, username);
				 ResultSet rs= statement.executeQuery();
				 
				 if(rs.next())
					 return Optional.of(rs.getString("TOKEN"));
				 else
					 return Optional.empty();
					 
				 
				 
			}
			catch(Exception e) {
				throw e;
			}
			finally {
				if(statement != null && !statement.isClosed())
					statement.close();
				
				if(connection != null && !connection.isClosed())
					connection.close();
					
			}
			
			
		}
		
		
		public void deleteForgotPassword(String username, String token) throws Exception {
			
			Connection connection=null;
			CallableStatement statement=null;
			try {
			 connection = jdbcTemplate.getDataSource().getConnection();
			 statement = connection.prepareCall("{call deleteForgotPassword(?,?) }");
				statement.setString(1, username);
				statement.setString(2, token);
				statement.executeUpdate();
			}
			catch(Exception e) {
				throw e;
			}
			finally {
				
				if(statement != null)
					statement.close();
				
				if(connection != null) 
					connection.close();
				
			}
		}
	
}
