package com.kronos.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.kronos.model.Accord;
import com.kronos.model.Pdf;
import com.kronos.model.State;
import com.kronos.model.Type;

@Repository
public class AccordService {

	@Autowired
    private JdbcTemplate jdbcTemplate;
	
    public void insertAccord(Accord acc) throws Exception {

    	Connection connection = jdbcTemplate.getDataSource().getConnection();
        CallableStatement statement = connection.prepareCall("{call insertAccord(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
;
        statement.setString(1, acc.getAccNumber());
        statement.setDate(2, new java.sql.Date(acc.getIncorporatedDate().getTime()));
        statement.setTime(3, java.sql.Time.valueOf(acc.getIncorporatedTime()));
        statement.setDate(4, new java.sql.Date(acc.getDeadline().getTime()));
        statement.setDate(5, new java.sql.Date(acc.getSessionDate().getTime()));
        statement.setString(6, String.valueOf(acc.getType().getId()));
        statement.setString(7, acc.getObservations());
        statement.setBoolean(8, acc.isPublished());
        statement.setBoolean(9, acc.isNotified());
        statement.setInt(10, acc.getState().getId());
        statement.setString(11, acc.getUser().getTempUser().getEmail());
        statement.executeUpdate();
        statement.close();

        for (Pdf item : acc.getURL()) {
            CallableStatement statement2 = connection.prepareCall("{call insertAccPdf(?, ?, ?)}");
            statement2.setString(1, acc.getAccNumber());
            statement2.setString(2, item.getURL());
            statement2.setBoolean(3, item.isFinalResponse());
            statement2.executeUpdate();

            statement2.close();
        }

    }
    
    
    
    public List<Accord> searchAllAccords() throws Exception{
    	Connection connection = jdbcTemplate.getDataSource().getConnection();
       CallableStatement statement = connection.prepareCall("{call searchAllAccords()}");
       ResultSet rs = statement.executeQuery();
       Map<String, Accord> map = new HashMap();

       while (rs.next()) {
      
           String accNumber = rs.getString("ACCNUMBER");
           if (map.isEmpty() || ! map.containsKey(accNumber)) { //if the map is empty or the result isn't
               Accord a = new Accord();                                                 //in the map
               a.setAccNumber(accNumber);
               a.setIncorporatedDate(rs.getDate("INCORDATE"));
               a.setDeadline(rs.getDate("DEADLINE"));
               a.setSessionDate(rs.getDate("SESSIONDATE")); 
               a.setType(new Type(rs.getString("TYPE_ID").charAt(0), rs.getString("TYPE_DESC")));
               a.setObservations(rs.getString("OBSERVATIONS"));
               a.setNotified(rs.getBoolean("NOTIFIED"));
               a.setPublished(rs.getBoolean("PUBLIC"));
               a.setState(new State(rs.getInt("STATE"),rs.getString("STATE_DESC")));
               a.getURL().add(new Pdf(rs.getString("URL")));
               map.put(accNumber, a);
           }
           else {
                   //if the result isn't  in the map or the map isn't empty, just add the URL into result
                   map.get(accNumber).getURL().add(new Pdf(rs.getString("URL")));
               
           }
       }
      
       statement.close();
       connection.close();
      return new ArrayList<>(map.values());
   }
    
    
    public List<Accord> searchBySessionDate(Date date) throws Exception {
    	Connection connection = jdbcTemplate.getDataSource().getConnection();
       CallableStatement statement = connection.prepareCall("{call searchAccordsessionDate(?)}");
       statement.setDate(1, new java.sql.Date(date.getTime()));
       ResultSet rs = statement.executeQuery();
       Map<String, Accord> map = new HashMap();

       while (rs.next()) {
      
           String accNumber = rs.getString("ACCNUMBER");
           if (map.isEmpty() || ! map.containsKey(accNumber)) { //if the map is empty or the result isn't
               Accord a = new Accord();                                                 //in the map
               a.setAccNumber(accNumber);
               a.setIncorporatedDate(rs.getDate("INCORDATE"));
               a.setDeadline(rs.getDate("DEADLINE"));
               a.setSessionDate(rs.getDate("SESSIONDATE")); 
               a.setType(new Type(rs.getString("TYPE_ID").charAt(0), rs.getString("TYPE_DESC")));
               a.setObservations(rs.getString("OBSERVATIONS"));
               a.setNotified(rs.getBoolean("NOTIFIED"));
               a.setPublished(rs.getBoolean("PUBLIC"));
               a.setState(new State(rs.getInt("STATE"),rs.getString("STATE_DESC")));
               a.getURL().add(new Pdf(rs.getString("URL")));
               map.put(accNumber, a);
           }
           else {
                   //if the result isn't  in the map or the map isn't empty, just add the URL into result
                   map.get(accNumber).getURL().add(new Pdf(rs.getString("URL")));
               
           }
       }
      
       statement.close();
       connection.close();
      return new ArrayList<>(map.values());
   }
	
	
    
    
    public List<Accord> searchByIncorDate(Date date) throws Exception {
    	Connection connection = jdbcTemplate.getDataSource().getConnection();
       CallableStatement statement = connection.prepareCall("{call searchAccordIncorDate(?)}");
       statement.setDate(1, new java.sql.Date(date.getTime()));
       ResultSet rs = statement.executeQuery();
       Map<String, Accord> map = new HashMap();

       while (rs.next()) {
      
           String accNumber = rs.getString("ACCNUMBER");
           if (map.isEmpty() || ! map.containsKey(accNumber)) { //if the map is empty or the result isn't
               Accord a = new Accord();                                                 //in the map
               a.setAccNumber(accNumber);
               a.setIncorporatedDate(rs.getDate("INCORDATE"));
               a.setDeadline(rs.getDate("DEADLINE"));
               a.setSessionDate(rs.getDate("SESSIONDATE")); 
               a.setType(new Type(rs.getString("TYPE_ID").charAt(0), rs.getString("TYPE_DESC")));
               a.setObservations(rs.getString("OBSERVATIONS"));
               a.setNotified(rs.getBoolean("NOTIFIED"));
               a.setPublished(rs.getBoolean("PUBLIC"));
               a.setState(new State(rs.getInt("STATE"),rs.getString("STATE_DESC")));
               a.getURL().add(new Pdf(rs.getString("URL")));
               map.put(accNumber, a);
           }
           else {
                   //if the result isn't  in the map or the map isn't empty, just add the URL into result
                   map.get(accNumber).getURL().add(new Pdf(rs.getString("URL")));
               
           }
       }
      
       statement.close();
       connection.close();
      return new ArrayList<>(map.values());
   }
    
    
    public List<Accord> searchByType(char type) throws Exception {
    	Connection connection = jdbcTemplate.getDataSource().getConnection();
       CallableStatement statement = connection.prepareCall("{call searchAccordType(?)}");
       statement.setString(1,String.valueOf(type) );
       ResultSet rs = statement.executeQuery();
       Map<String, Accord> map = new HashMap();

       while (rs.next()) {
      
           String accNumber = rs.getString("ACCNUMBER");
           if (map.isEmpty() || ! map.containsKey(accNumber)) { //if the map is empty or the result isn't
               Accord a = new Accord();                                                 //in the map
               a.setAccNumber(accNumber);
               a.setIncorporatedDate(rs.getDate("INCORDATE"));
               a.setDeadline(rs.getDate("DEADLINE"));
               a.setSessionDate(rs.getDate("SESSIONDATE")); 
               a.setType(new Type(rs.getString("TYPE_ID").charAt(0), rs.getString("TYPE_DESC")));
               a.setObservations(rs.getString("OBSERVATIONS"));
               a.setNotified(rs.getBoolean("NOTIFIED"));
               a.setPublished(rs.getBoolean("PUBLIC"));
               a.setState(new State(rs.getInt("STATE"),rs.getString("STATE_DESC")));
               a.getURL().add(new Pdf(rs.getString("URL")));
               map.put(accNumber, a);
           }
           else {
                   //if the result isn't  in the map or the map isn't empty, just add the URL into result
                   map.get(accNumber).getURL().add(new Pdf(rs.getString("URL")));
               
           }
       }
      
       statement.close();
       connection.close();
      return new ArrayList<>(map.values());
   }
    
    public List<Accord> searchByAccNumber(String accordNumber) throws Exception {
    	Connection connection = jdbcTemplate.getDataSource().getConnection();
       CallableStatement statement = connection.prepareCall("{call searchAccordNumber(?)}");
       statement.setString(1,accordNumber);
       ResultSet rs = statement.executeQuery();
       Map<String, Accord> map = new HashMap();

       while (rs.next()) {
       
           String accNumber = rs.getString("ACCNUMBER");
           if (map.isEmpty() || ! map.containsKey(accNumber)) { //if the map is empty or the result isn't
               Accord a = new Accord();                                                 //in the map
               a.setAccNumber(accNumber);
               a.setIncorporatedDate(rs.getDate("INCORDATE"));
               a.setDeadline(rs.getDate("DEADLINE"));
               a.setSessionDate(rs.getDate("SESSIONDATE")); 
               a.setType(new Type(rs.getString("TYPE_ID").charAt(0), rs.getString("TYPE_DESC")));
               a.setObservations(rs.getString("OBSERVATIONS"));
               a.setNotified(rs.getBoolean("NOTIFIED"));
               a.setPublished(rs.getBoolean("PUBLIC"));
               a.setState(new State(rs.getInt("STATE"),rs.getString("STATE_DESC")));
               a.getURL().add(new Pdf(rs.getString("URL")));
               map.put(accNumber, a);
           }
           else {
                   //if the result isn't  in the map or the map isn't empty, just add the URL into result
                   map.get(accNumber).getURL().add(new Pdf(rs.getString("URL")));
               
           }
       }
      
       statement.close();
       connection.close();
      return new ArrayList<>(map.values());
   }
    
    
    public Optional<Accord> getAccord(String accordNumber) throws Exception {
    	Connection connection = jdbcTemplate.getDataSource().getConnection();
        CallableStatement statement = connection.prepareCall("{call getAccord(?)}");
        statement.setString(1,accordNumber);
        ResultSet rs = statement.executeQuery();
        Map<String, Accord> map = new HashMap();

        while (rs.next()) {
        
            String accNumber = rs.getString("ACCNUMBER");
            if (map.isEmpty() || ! map.containsKey(accNumber)) { //if the map is empty or the result isn't
                Accord a = new Accord();                                                 //in the map
                a.setAccNumber(accNumber);
                a.setIncorporatedDate(rs.getDate("INCORDATE"));
                a.setDeadline(rs.getDate("DEADLINE"));
                a.setSessionDate(rs.getDate("SESSIONDATE")); 
                a.setType(new Type(rs.getString("TYPE_ID").charAt(0), rs.getString("TYPE_DESC")));
                a.setObservations(rs.getString("OBSERVATIONS"));
                a.setNotified(rs.getBoolean("NOTIFIED"));
                a.setPublished(rs.getBoolean("PUBLIC"));
                a.setState(new State(rs.getInt("STATE"),rs.getString("STATE_DESC")));
                a.getURL().add(new Pdf(rs.getString("URL")));
                map.put(accNumber, a);
            }
            else {
                    //if the result isn't  in the map or the map isn't empty, just add the URL into result
                    map.get(accNumber).getURL().add(new Pdf(rs.getString("URL")));
                
            }
        }
       
        statement.close();
        connection.close();
       List<Accord> list= new ArrayList<Accord>(map.values());
       if(list.size() > 1 || list.isEmpty())
    	   return Optional.empty();
       else
    	   return Optional.of(list.get(0));
    }
    
    public void insertActPdf(String accNumber, Pdf pdf) throws Exception {
    	Connection connection = jdbcTemplate.getDataSource().getConnection();
        CallableStatement statement = connection.prepareCall("{call insertAccPdf(?,?,?)}");
        statement.setString(1, accNumber);
        statement.setString(2, pdf.getURL());
        statement.setBoolean(3, pdf.isFinalResponse());
        statement.executeUpdate();
        statement.close();
        connection.close();
    }
    
    public void deleteAccPdf(String accNumber, String url) throws Exception {
    	Connection connection = jdbcTemplate.getDataSource().getConnection();
        CallableStatement statement = connection.prepareCall("{call deletePdf(?,?)}");
        statement.setString(1, accNumber);
        statement.setString(2, url);
        statement.executeUpdate();
        statement.close();
        connection.close();
    }
    
    public void deleteAccord(String accNumber, String user) throws Exception {
    	Connection connection = jdbcTemplate.getDataSource().getConnection();
        CallableStatement statement = connection.prepareCall("{call deleteAccord(?,?)}");
        statement.setString(1, accNumber);
        statement.setString(2, user);
        statement.executeUpdate();
        statement.close();
        connection.close();
    }
    
    
    
    
    
    public void updateAccord(Accord acc) throws Exception {

    	Connection connection = jdbcTemplate.getDataSource().getConnection();
        CallableStatement statement = connection.prepareCall("{call updateAccord(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
        
        
        statement.setString(1, acc.getAccNumber());
        statement.setDate(2, new java.sql.Date(acc.getDeadline().getTime()));
        statement.setDate(3, new java.sql.Date(acc.getSessionDate().getTime()));
        statement.setString(4, acc.getObservations());
        statement.setBoolean(5, acc.isPublished());
        statement.setBoolean(6, acc.isNotified());
        statement.setInt(7, acc.getState().getId());
        statement.setString(8, String.valueOf(acc.getType().getId()));
        statement.setString(9, acc.getUser().getTempUser().getEmail());
        statement.executeUpdate();
        statement.close();
        connection.close();
    

    }
    
    
    public List<Accord> emailInfo(Date today, Date limit) throws Exception{ 
    	Connection connection = jdbcTemplate.getDataSource().getConnection(); 
    	CallableStatement statement = connection.prepareCall("{call emailInfo(?,?)}"); 
    	statement.setDate(1, new java.sql.Date(today.getTime())); 
    	statement.setDate(2, new java.sql.Date(limit.getTime())); 
    	ResultSet rs= statement.executeQuery();
    	Map<String, Accord> map = new HashMap();
    		while(rs.next()) {
    			String accNumber = rs.getString("ACCNUMBER");
                if (map.isEmpty() || ! map.containsKey(accNumber)) { //if the map is empty or the result isn't
                    Accord a = new Accord();                                                 //in the map
                    a.setAccNumber(accNumber);
                    a.setIncorporatedDate(rs.getDate("INCORDATE"));
                    a.setDeadline(rs.getDate("DEADLINE"));
                    a.setSessionDate(rs.getDate("SESSIONDATE")); 
                    a.setType(new Type(rs.getString("TYPE_ID").charAt(0), rs.getString("TYPE_DESC")));
                    a.setObservations(rs.getString("OBSERVATIONS"));
                    a.setNotified(rs.getBoolean("NOTIFIED"));
                    a.setPublished(rs.getBoolean("PUBLIC"));
                    a.setState(new State(rs.getInt("STATE"),rs.getString("STATE_DESC")));
                    a.getURL().add(new Pdf(rs.getString("URL")));
                    map.put(accNumber, a);
                }
                else {
                        //if the result isn't  in the map or the map isn't empty, just add the URL into result
                        map.get(accNumber).getURL().add(new Pdf(rs.getString("URL")));
                    
                }
    		}
    	
    		statement.close();
    		connection.close();
    		  
    	      return new ArrayList<>(map.values());
    }
    
    
}
