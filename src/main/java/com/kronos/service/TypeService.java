package com.kronos.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.kronos.model.Type;


@Repository
public class TypeService {

	@Autowired
    private JdbcTemplate jdbcTemplate;
	

	 class TypeRowMapper implements RowMapper < Type > {
	        @Override
	        public Type mapRow(ResultSet rs, int rowNum) throws SQLException {
	        	Type type = new Type();
	        	
	            type.setId(rs.getString("ID").charAt(0));
	            type.setDescription(rs.getString("DESCRIPTION"));
	         
	         
	            return type;
	        }
	    }
	
	
	
	public List<Type> findAll()  {
		
		try {
		Connection connection = jdbcTemplate.getDataSource().getConnection();
		  CallableStatement statement = connection.prepareCall("{call searchAllTypes() }");
		  
		//  statement.registerOutParameter(1, new );
		  ResultSet rs=statement.executeQuery();
		  
		 List<Type> result= this.mapRowList(rs);
		  
		 statement.close();
		 connection.close();
	         return result;
	         
	        
		}
		catch(Exception e){
			System.out.println("\n\n\n\n\n\n\n\nERROR\n\n\n\n\n"+e.getMessage());
		}
		
		
		
		return null;
	}
		

	private List<Type> mapRowList(ResultSet rs) throws SQLException{
		
		List<Type> result= new ArrayList<>();
		while(rs.next()) {
			  Type t=new Type();
	             t.setDescription(rs.getString("DESCRIPTION"));
	             t.setId(rs.getString("ID").charAt(0));
	             result.add(t);
		}
		
		return result;
		
	}
	
	
	
}
