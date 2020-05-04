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
import com.kronos.model.Role;

@Repository
public class RoleService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Role> findAll() {
		try {
			Connection connection = jdbcTemplate.getDataSource().getConnection();
			CallableStatement statement = connection.prepareCall("{call searchAllRoles() }");

			// statement.registerOutParameter(1, new );
			ResultSet rs = statement.executeQuery();

			List<Role> result = this.mapRowList(rs);

			statement.close();
			connection.close();
			return result;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private List<Role> mapRowList(ResultSet rs) throws SQLException {

		List<Role> result = new ArrayList<>();
		while (rs.next()) {
			Role t = new Role();
			t.setName(rs.getString("NAME"));
			t.setId(Character.getNumericValue(rs.getString("ID").charAt(0)));
			result.add(t);
		}

		return result;

	}

	public void insertRole(String name) throws Exception {
		Connection connection = jdbcTemplate.getDataSource().getConnection();
		CallableStatement statement = connection.prepareCall("{call insertRole(?)}");
		statement.setString(1, name);
		statement.executeUpdate();
		statement.close();
	}

	public Optional<Role> searchRole(String name) {
		try {
			Connection connection = jdbcTemplate.getDataSource().getConnection();
			CallableStatement statement = connection.prepareCall("{call searchRole(?)}");
			statement.setString(1, name);

			ResultSet rs = statement.executeQuery();
			Role rl = null;
			while (rs.next()) {
				rl = new Role(rs.getInt("ID"), rs.getString("NAME"));
			}
			statement.close();
			connection.close();

			return (rl != null) ? Optional.of(rl) : Optional.empty();
		} catch (Exception e) {
			System.out.println(e);
		}
		return Optional.empty();
	}

}
