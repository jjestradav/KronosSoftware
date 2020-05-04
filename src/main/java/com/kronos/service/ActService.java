package com.kronos.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.kronos.model.Act;

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
		boolean isInDb = rs.next();
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
		return true;

	}

	public List<Act> findSessionDates() {
		try {
			Connection connection = jdbcTemplate.getDataSource().getConnection();
			CallableStatement statement = connection.prepareCall("{call sessionDates() }");
			ResultSet rs = statement.executeQuery();
			List<Act> result = this.mapRowList(rs);
			statement.close();
			connection.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return null;
	}

	private List<Act> mapRowList(ResultSet rs) throws SQLException {
		List<Act> result = new ArrayList<>();
		while (rs.next()) {
			Act t = new Act();
			t.setSessionDate(rs.getDate("SESSIONDATE"));
			result.add(t);
		}

		return result;
	}

	public void addAct(Act acta) throws Exception {
		Connection connection = jdbcTemplate.getDataSource().getConnection();
		CallableStatement statement = connection.prepareCall("{call addAct(?,?,?,?,?)}");
		statement.setString(1, acta.getSessionType());
		statement.setDate(2, new java.sql.Date(acta.getSessionDate().getTime()));
		statement.setString(3, acta.getUrl());
		statement.setBoolean(4, acta.getPublc());
		statement.setBoolean(5, acta.getActive());
		statement.executeUpdate();
		statement.close();
		connection.close();
	}

	public List<Act> findAll() {
		try {
			Connection connection = jdbcTemplate.getDataSource().getConnection();
			CallableStatement statement = connection.prepareCall("{call searchAllActs()}");
			ResultSet rs = statement.executeQuery();
			List<Act> result = this.mapRowList1(rs);
			statement.close();
			connection.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<Act> mapRowList1(ResultSet rs) throws SQLException {
		List<Act> result = new ArrayList<>();
		while (rs.next()) {
			Act act = new Act();
			act.setSessionType(rs.getString("SESSIONTYPE"));
			act.setSessionDate(rs.getDate("SESSIONDATE"));
			act.setUrl(rs.getString("URL"));
			act.setPublc(rs.getBoolean("PUBLIC"));
			act.setActive(rs.getBoolean("ACTIVE"));
			result.add(act);
		}
		return result;
	}

	public Optional<Act> getAct(Date sessionDate) throws Exception {
		Connection connection = jdbcTemplate.getDataSource().getConnection();
		CallableStatement statement = connection.prepareCall("{call getActByDate(?)}");
		statement.setDate(1, new java.sql.Date(sessionDate.getTime()));
		ResultSet rs = statement.executeQuery();
		Act act = null;
		while (rs.next()) {
			act = new Act(rs.getString("SESSIONTYPE"), rs.getDate("SESSIONDATE"), rs.getString("URL"),
					rs.getBoolean("PUBLIC"), rs.getBoolean("ACTIVE"));
	
		}
		statement.close();
		connection.close();
		if (act == null)
			return Optional.empty();

		return Optional.of(act);
	}

	public List<Act> searchBySessionDate(Date db2) {
		try {
			Connection connection= jdbcTemplate.getDataSource().getConnection();
			CallableStatement statement = connection.prepareCall("{call getActsByDate(?)}");
			statement.setDate(1, new java.sql.Date(db2.getTime()));
			ResultSet rs = statement.executeQuery();
			List<Act> result = this.mapRowList1(rs);
			statement.close();
			connection.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public List<Act> searchByType(String type) {
		try {
			Connection connection= jdbcTemplate.getDataSource().getConnection();
			CallableStatement statement = connection.prepareCall("{call getActByType(?)}");
			statement.setString(1, type);
			ResultSet rs = statement.executeQuery();
			List<Act> result = this.mapRowList1(rs);
			statement.close();
			connection.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public void updateActPdf(Date sessionDate, String url) throws Exception {
    	Connection connection = jdbcTemplate.getDataSource().getConnection();
        CallableStatement statement = connection.prepareCall("{call updatePdfAct(?,?)}");
        statement.setDate(1, new java.sql.Date(sessionDate.getTime()));
        statement.setString(2, url);
        statement.executeUpdate();
        statement.close();
        connection.close();
    }

	public void updateAct(Act act) throws SQLException {
		Connection connection = jdbcTemplate.getDataSource().getConnection();
		CallableStatement statement = connection.prepareCall("{call updateAct(?,?,?,?)}");
		statement.setString(1, act.getSessionType());
		statement.setDate(2, new java.sql.Date(act.getSessionDate().getTime()));
		statement.setBoolean(3, act.getPublc());
		statement.setBoolean(4, act.getActive());
		statement.executeUpdate();
		statement.close();
		connection.close();
	}

	public List<Act> deactivatedActs() {
		
		try {
			Connection connection = jdbcTemplate.getDataSource().getConnection();
			CallableStatement statement = connection.prepareCall("{call deactivatedActs()}");
			ResultSet rs = statement.executeQuery();
			List<Act> result = this.mapRowList1(rs);
			statement.close();
			connection.close();
			return result;
		} catch (Exception e) {
			System.out.println("\n\n\n\n\n\n\n\nERROR\n\n\n\n\n" + e.getMessage());
		}
		return null;
	}
	
}
