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
			  
				TempUser tmp = new TempUser(rs.getString("NAME"),rs.getString("EMAIL"));
				Department newDep= new Department(rs.getInt("DEP_ID"),rs.getString("DEP_NAME"));
				user= new User(tmp,newDep,rs.getBoolean("STATUS"));
				user.setIsBoss(rs.getBoolean("ISBOSS"));
					  
		  }
		  statement.close();
		  connection.close();
		  if(user==null)
			  return Optional.empty();
		  
		  return Optional.of(user);
	}
	
	public void insertUser(User user) throws Exception {
		Connection connection = jdbcTemplate.getDataSource().getConnection();
        CallableStatement statement = connection.prepareCall("{call insertUser(?, ?, ?, ?)}");
        statement.setInt(1, user.getDepartment().getId());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getTempUser().getEmail());
        statement.setBoolean(4, user.getStatus());
        statement.executeUpdate();
        statement.close();
	}
	
	
	public void updatePassword(String user, String password) throws Exception {
		Connection connection = null;
        CallableStatement statement =null;
        
        try {
        	 connection = jdbcTemplate.getDataSource().getConnection();
        	 statement = connection.prepareCall("{call updatePassword(?, ?)}");
        	 statement.setString(1, user);
        	 statement.setString(2, password);
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

	public List<User> searchAllUsersWithoutRole(){
		try {
			Connection connection = jdbcTemplate.getDataSource().getConnection();
			CallableStatement statement = connection.prepareCall("{call searchAllUsersWithoutRole() }");

			// statement.registerOutParameter(1, new );
			ResultSet rs = statement.executeQuery();

			List<User> result = this.mapRowList(rs);

			statement.close();
			connection.close();
			return result;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	private List<User> mapRowList(ResultSet rs) throws SQLException {

		List<User> result = new ArrayList<>();
		while (rs.next()) {
			TempUser u= new TempUser();
			User t = new User();
			u.setEmail(rs.getString("TEMPUSER"));
			t.setTempUser(u);
			result.add(t);
		}

		return result;

	}

	public List<User> getUsersByDepartment(Department dep) throws Exception{
		Connection connection=null;
		CallableStatement statement=null;
		ResultSet rs=null;
		try {
			 connection = jdbcTemplate.getDataSource().getConnection();
			 statement = connection.prepareCall("{call searchUserByDepartment(?) }");

			statement.setInt(1, dep.getId());
			 rs= statement.executeQuery();

			List<User> result = new ArrayList<>();
			while(rs.next()) {
				TempUser tmp = new TempUser(rs.getString("NAME"),rs.getString("EMAIL"));
				Department newDep= new Department(rs.getInt("DEP_ID"),rs.getString("DEP_NAME"));
				User user= new User(tmp,newDep,rs.getBoolean("STATUS"));
				result.add(user);				
				
			}

			
			return result;

		} catch (Exception e) {
			throw e;
		}
		finally {
			try {
			if(rs!=null)
				rs.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
			try {
			if(statement!= null)
				statement.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
			try {
			if(connection != null)
			connection.close();
			}
			catch(Exception e) {
				e.printStackTrace();
				
			}
		}
		
		}
		
		public List<User> getUsersByEmail(String email) throws Exception{
			Connection connection=null;
			CallableStatement statement=null;
			ResultSet rs=null;
			try {
				 connection = jdbcTemplate.getDataSource().getConnection();
				 statement = connection.prepareCall("{call searchUserByEmail(?) }");

				statement.setString(1, email);
				 rs= statement.executeQuery();

				List<User> result = new ArrayList<>();
				while(rs.next()) {
					TempUser tmp = new TempUser(rs.getString("NAME"),rs.getString("EMAIL"));
					Department newDep= new Department(rs.getInt("DEP_ID"),rs.getString("DEP_NAME"));
					User user= new User(tmp,newDep,rs.getBoolean("STATUS"));
					result.add(user);				
					
				}

				
				return result;

			} catch (Exception e) {
				throw e;
			}
			finally {
				try {
				if(rs!=null)
					rs.close();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				
				try {
				if(statement!= null)
					statement.close();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				
				try {
				if(connection != null)
				connection.close();
				}
				catch(Exception e) {
					e.printStackTrace();
					
				}
			}

		
	}
		
		
		public List<User> getUsersStatus(boolean status) throws Exception{
			Connection connection=null;
			CallableStatement statement=null;
			ResultSet rs=null;
			try {
				 connection = jdbcTemplate.getDataSource().getConnection();
				 statement = connection.prepareCall("{call searchUserByStatus(?) }");

				statement.setBoolean(1, status);
				 rs= statement.executeQuery();

				List<User> result = new ArrayList<>();
				while(rs.next()) {
					TempUser tmp = new TempUser(rs.getString("NAME"),rs.getString("EMAIL"));
					Department newDep= new Department(rs.getInt("DEP_ID"),rs.getString("DEP_NAME"));
					User user= new User(tmp,newDep,rs.getBoolean("STATUS"));
					result.add(user);				
					
				}

				
				return result;

			} catch (Exception e) {
				throw e;
			}
			finally {
				try {
				if(rs!=null)
					rs.close();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				
				try {
				if(statement!= null)
					statement.close();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				
				try {
				if(connection != null)
				connection.close();
				}
				catch(Exception e) {
					e.printStackTrace();
					
				}
			}

		
	}
		
		public List<User> getAllUsers() throws Exception{
			Connection connection=null;
			CallableStatement statement=null;
			ResultSet rs=null;
			try {
				 connection = jdbcTemplate.getDataSource().getConnection();
				 statement = connection.prepareCall("{call searchAllUsers() }");

				 rs= statement.executeQuery();

				List<User> result = new ArrayList<>();
				while(rs.next()) {
					TempUser tmp = new TempUser(rs.getString("NAME"),rs.getString("EMAIL"));
					Department newDep= new Department(rs.getInt("DEP_ID"),rs.getString("DEP_NAME"));
					User user= new User(tmp,newDep,rs.getBoolean("STATUS"));
					result.add(user);				
					
				}

				
				return result;

			} catch (Exception e) {
				throw e;
			}
			finally {
				try {
				if(rs!=null)
					rs.close();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				
				try {
				if(statement!= null)
					statement.close();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				
				try {
				if(connection != null)
				connection.close();
				}
				catch(Exception e) {
					e.printStackTrace();
					
				}
			}

		
	}
		
		public Optional<User> getBossByDepartment(Department dep) throws Exception{
			Connection connection = jdbcTemplate.getDataSource().getConnection();
			  CallableStatement statement = connection.prepareCall("{call getUserByEmail(?) }");
			  statement.setInt(1, dep.getId());
			  ResultSet rs=statement.executeQuery();
			  User user=null;
				while(rs.next()) {
					TempUser tmp = new TempUser(rs.getString("NAME"),rs.getString("EMAIL"));
					Department newDep= new Department(rs.getInt("DEP_ID"),rs.getString("DEP_NAME"));
					user= new User(tmp,newDep,rs.getBoolean("STATUS"));
					user.setIsBoss(rs.getBoolean("ISBOSS"));
					
				}
			  statement.close();
			  connection.close();
			  if(user==null)
				  return Optional.empty();
			  
			  return Optional.of(user);
		}
		
		public List<User> getUsersBoss(String userId, int dep) throws Exception{
			Connection connection=null;
			CallableStatement statement=null;
			ResultSet rs=null;
			try {
				 connection = jdbcTemplate.getDataSource().getConnection();
				 statement = connection.prepareCall("{call searchUsersBoss(?,?) }");
				 statement.setString(1, userId);
				statement.setInt(2, dep);
				 rs= statement.executeQuery();

				List<User> result = new ArrayList<>();
				while(rs.next()) {
					TempUser tmp = new TempUser(rs.getString("NAME"),rs.getString("EMAIL"));
					Department newDep= new Department(rs.getInt("DEP_ID"),rs.getString("DEP_NAME"));
					User user= new User(tmp,newDep,rs.getBoolean("STATUS"));
					result.add(user);				
					
				}

				
				return result;

			} catch (Exception e) {
				throw e;
			}
			finally {
				try {
				if(rs!=null)
					rs.close();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				
				try {
				if(statement!= null)
					statement.close();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				
				try {
				if(connection != null)
				connection.close();
				}
				catch(Exception e) {
					e.printStackTrace();
					
				}
			}
			
			}
		
		public Optional<User> getUserBossByDepartment(int id) throws Exception{
			Connection connection = jdbcTemplate.getDataSource().getConnection();
			  CallableStatement statement = connection.prepareCall("{call searchBossByDepartment(?) }");
			  statement.setInt(1, id);
			  ResultSet rs=statement.executeQuery();
			  User user=null;
			  while(rs.next()) {
				  
					TempUser tmp = new TempUser(rs.getString("NAME"),rs.getString("EMAIL"));
					Department newDep= new Department(rs.getInt("DEP_ID"),rs.getString("DEP_NAME"));
					user= new User(tmp,newDep,rs.getBoolean("STATUS"));
					user.setIsBoss(rs.getBoolean("ISBOSS"));
						  
			  }
			  statement.close();
			  connection.close();
			  if(user==null)
				  return Optional.empty();
			  
			  return Optional.of(user);
		}
				
}
