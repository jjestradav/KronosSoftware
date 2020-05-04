package com.kronos.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.kronos.model.State;
import com.kronos.model.Type;


@Repository
public class StateService {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;

	public List<State> findAll(){
		try {
			Connection connection = jdbcTemplate.getDataSource().getConnection();
			CallableStatement statement = connection.prepareCall("{call searchAllStates() }");

			// statement.registerOutParameter(1, new );
			ResultSet rs = statement.executeQuery();

			List<State> result = this.mapRowList(rs);

			statement.close();
			connection.close();
			return result;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private List<State> mapRowList(ResultSet rs) throws SQLException {

		List<State> result = new ArrayList<>();
		while (rs.next()) {
			State t = new State();
			t.setDescription(rs.getString("DESCRIPTION"));
			t.setId(Character.getNumericValue(rs.getString("ID").charAt(0)));
			result.add(t);
		}

		return result;

	}

}
