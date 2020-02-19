package com.kronos.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.kronos.model.TempUser;

@Repository
public class TempUserService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Optional<TempUser> findByEmail(String email) {

		try {

			Connection connection = jdbcTemplate.getDataSource().getConnection();
			CallableStatement statement = connection.prepareCall("{call searchTempUser(?) }");
			statement.setString(1, email);

			ResultSet rs = statement.executeQuery();
			TempUser us=new TempUser(rs.getString("NAME"), rs.getString("EMAIL"));
			statement.close();
			connection.close();
			return Optional.of(us);

		}

		catch (Exception e) {

		}
		return Optional.empty();
	}

	public void insertTempUser(TempUser tmp) throws Exception {
		
			Connection connection = jdbcTemplate.getDataSource().getConnection();
			CallableStatement statement = connection.prepareCall("{call insertTempUser(?, ?)}");
			statement.setString(1, tmp.getName());
			statement.setString(2, tmp.getEmail());
			statement.executeUpdate();
			statement.close();
			connection.close();

	
	}

}
