package com.kronos.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ActService {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	public boolean isActInDB(Date date) throws Exception {
		
		
			Connection connection = jdbcTemplate.getDataSource().getConnection();
			CallableStatement statement = connection.prepareCall("{call isActInDB(?) }");
			statement.setDate(1, new java.sql.Date(date.getTime()));
			
			// statement.registerOutParameter(1, new );
			ResultSet rs = statement.executeQuery();
			boolean isInDb=rs.next();
			statement.close();
			connection.close();
			return isInDb;
		
		
		
	}
	
	
	public boolean insertAct(Date date) throws Exception {
	
			Connection connection = jdbcTemplate.getDataSource().getConnection();
			CallableStatement statement = connection.prepareCall("{call  insertTempAct(?) }");
			statement.setDate(1, new java.sql.Date(date.getTime()));
			
			// statement.registerOutParameter(1, new );
			int aux = statement.executeUpdate();
			statement.close();
			connection.close();
			System.out.println(aux);
			return true;
			
	
	}
	
	
}
