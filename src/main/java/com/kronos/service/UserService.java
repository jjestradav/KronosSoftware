package com.kronos.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.kronos.model.Department;
import com.kronos.model.TempUser;
import com.kronos.model.User;

@Repository
public class UserService {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	public Optional<User> getUserByEmail(String email) throws Exception{
		Connection connection = jdbcTemplate.getDataSource().getConnection();
		  CallableStatement statement = connection.prepareCall("{call getUserByEmail(?) }");
		  statement.setString(1, email);
		  ResultSet rs=statement.executeQuery();
		  User user=null;
		  while(rs.next()) {
			  user = new User(
				new TempUser(rs.getString("NAME"),rs.getString("TEMPUSER")),
				rs.getString("PASSWORD"),
				new Department(Integer.parseInt(rs.getString("DEPARTMENT")),
						rs.getString("DEPARTMENT_NAME")));
					  
		  }
		  statement.close();
		  connection.close();
		  if(user==null)
			  return Optional.empty();
		  
		  return Optional.of(user);
	}
}
