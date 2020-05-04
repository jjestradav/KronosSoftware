package com.kronos.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.kronos.model.Department;
import com.kronos.model.State;
import com.kronos.model.TempUser;

@Repository
public class DeparmentService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<Department> findAll(){
		try {
			Connection connection = jdbcTemplate.getDataSource().getConnection();
			CallableStatement statement = connection.prepareCall("{call searchAllDepartments() }");

			// statement.registerOutParameter(1, new );
			ResultSet rs = statement.executeQuery();

			List<Department> result = this.mapRowList(rs);

			statement.close();
			connection.close();
			return result;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private List<Department> mapRowList(ResultSet rs) throws Exception {

		List<Department> result = new ArrayList<>();
		while (rs.next()) {
			Department t = new Department();
			t.setName(rs.getString("NAME"));
			t.setId(rs.getInt("ID"));
			result.add(t);
		}

			
		return result;

	}
	
	public void insertDepartment(String name)throws Exception{
		Connection connection = jdbcTemplate.getDataSource().getConnection();
        CallableStatement statement = connection.prepareCall("{call insertDepartment(?)}");
        statement.setString(1, name);
        statement.executeUpdate();
        statement.close();
	}
	
	/*public Optional<Department> searchDepartment(String name){
		try {
		Connection connection = jdbcTemplate.getDataSource().getConnection();
        CallableStatement statement = connection.prepareCall("{call searchDepartment(?)}");
		statement.setString(1, name);
		
		ResultSet rs= statement.executeQuery();
		Department dp=null;
		//while(rs.next()) {
			dp= new Department(rs.getInt("ID"), rs.getString("NAME"));
	//	}
		statement.close();
		connection.close();
		
		return Optional.of(dp);
		
		}catch(Exception e) {
			System.out.println(e);
		}
		return Optional.empty();
	}*/
	
	public Optional<Department> searchDepartment(String name){
		try {
		Connection connection = jdbcTemplate.getDataSource().getConnection();
        CallableStatement statement = connection.prepareCall("{call searchDepartment(?)}");
		statement.setString(1, name);
		
		ResultSet rs= statement.executeQuery();
		Department dp=null;
		while(rs.next()) {
			dp= new Department(rs.getInt("ID"), rs.getString("NAME"));
		}
		statement.close();
		connection.close();
		
		return (dp != null) ? Optional.of(dp) : Optional.empty(); 
		}catch(Exception e) {
			System.out.println(e);
		}
		return Optional.empty();
	}
	
	public Optional<Department> getDepartmentByEmail(String email){
		try {
		Connection connection = jdbcTemplate.getDataSource().getConnection();
        CallableStatement statement = connection.prepareCall("{call getDepartmentByEmail(?)}");
		statement.setString(1, email);
		
		ResultSet rs= statement.executeQuery();
		Department dp=null;
		while(rs.next()) {
			dp= new Department(rs.getInt("ID"), rs.getString("NAME"));
		}
		statement.close();
		connection.close();
		
		return (dp != null) ? Optional.of(dp) : Optional.empty(); 
		}catch(Exception e) {
			System.out.println(e);
		}
		return Optional.empty();
	}
	
	
	
}
