package com.kronos.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.kronos.model.Accord;
import com.kronos.model.Department;
import com.kronos.model.User;
import com.kronos.model.NotificationDTO;

@Repository
public class NotificationService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void insertNotification(Accord acc, String us) throws Exception {

			
			Connection connection = jdbcTemplate.getDataSource().getConnection();
			CallableStatement statement = connection.prepareCall("{call  insertNotification(?, ?) }");
			statement.setString(1, acc.getAccNumber());
			statement.setString(2, us);
			
			
			// statement.registerOutParameter(1, new );
			int aux = statement.executeUpdate();
			statement.close();
			connection.close();


	}
	
	public void deleteNotification(Accord acc, String us) throws Exception {

		
		Connection connection = jdbcTemplate.getDataSource().getConnection();
		CallableStatement statement = connection.prepareCall("{call  deleteNotification(?, ?) }");
		statement.setString(1, acc.getAccNumber());
		statement.setString(2, us);
		
		
		// statement.registerOutParameter(1, new );
		int aux = statement.executeUpdate();
		statement.close();
		connection.close();


}
	
	
	public boolean isInNotification(Accord acc, String us) throws Exception {
		Connection connection = jdbcTemplate.getDataSource().getConnection();
		CallableStatement statement = connection.prepareCall("{call  isInNotification(?, ?) }");
		statement.setString(1, acc.getAccNumber());
		statement.setString(2, us);
		
		
		// statement.registerOutParameter(1, new );
		ResultSet rs = statement.executeQuery();
		
		statement.close();
		connection.close();
		return rs.next();
	}
	
	public List<NotificationDTO> resposableUsers(Accord acc) throws Exception{
		Connection connection = jdbcTemplate.getDataSource().getConnection();
		CallableStatement statement = connection.prepareCall("{call  resposableUsers(?) }");
		statement.setString(1, acc.getAccNumber());
		
		
		// statement.registerOutParameter(1, new );
		List<NotificationDTO> result= new ArrayList<>();
		ResultSet rs = statement.executeQuery();
		
		while(rs.next()) {
			NotificationDTO not = new NotificationDTO(rs.getString("ACCORD"),
			rs.getString("USER"),new Department(rs.getInt("DEP_ID"),rs.getString("DEP_NAME")));
			result.add(not);
		}
		
		rs.close();
		statement.close();
		connection.close();
		return result;
	}
	
	
	public List<Department> resposableDepartments(Accord acc) throws Exception{
		Connection connection = jdbcTemplate.getDataSource().getConnection();
		CallableStatement statement = connection.prepareCall("{call  resposableDepartments(?) }");
		statement.setString(1, acc.getAccNumber());
		
		
		// statement.registerOutParameter(1, new );
		List<Department> result= new ArrayList<>();
		ResultSet rs = statement.executeQuery();
		
		while(rs.next()) {
		
			Department dep= new Department(rs.getInt("DEP_ID"),rs.getString("DEP_NAME"));
			result.add(dep);
		}
		
		rs.close();
		statement.close();
		connection.close();
		return result;
	}
	
	
	
	public List<String> getResponsablesUserName(String accNumber) throws Exception{
		Connection connection = jdbcTemplate.getDataSource().getConnection();
		CallableStatement statement = connection.prepareCall("{call  getResponsablesUserName(?) }");
		statement.setString(1, accNumber);
		
		
		// statement.registerOutParameter(1, new );
		List<String> result= new ArrayList<>();
		ResultSet rs = statement.executeQuery();
		
		while(rs.next()) {
			result.add(rs.getString("USER"));
		}
		
		rs.close();
		statement.close();
		connection.close();
		return result;
	}

}
