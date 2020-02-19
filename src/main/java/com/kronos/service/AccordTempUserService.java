package com.kronos.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.kronos.model.Accord;
import com.kronos.model.TempUser;

@Repository
public class AccordTempUserService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void insertAccord_TempUser(Accord acc, TempUser tmp) throws Exception {
		
			Connection connection = jdbcTemplate.getDataSource().getConnection();
			CallableStatement statement = connection.prepareCall("{call insertUserAcc(?, ?)}");
			statement.setString(1, tmp.getEmail());
			statement.setString(2, acc.getAccNumber());
			statement.executeUpdate();
			statement.close();
			connection.close();
		

		

	}
	
	

	
}
