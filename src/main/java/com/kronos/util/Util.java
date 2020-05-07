package com.kronos.util;

import java.time.LocalDateTime;
import java.util.Random;

public class Util {

	public static String getRandomToken() {
	    // It will generate 6 digit random Number.
	    // from 0 to 999999
	    Random rnd = new Random();
	    int number = rnd.nextInt(999999);

	    // this will convert any number sequence into 6 character.
	    	return String.format("%06d", number);
	    
	}
	
	
	public static boolean isBefore(LocalDateTime previous,LocalDateTime now, int minutes ) {
		
		LocalDateTime aux=previous.plusMinutes(minutes);
		
		if(now.isEqual(aux)) 
			return true;	
			
		else
			if(now.isBefore(aux)) 
				return true;
				
			
		return false;
	}
}
