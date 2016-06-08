package com.lge.sureparksystem.parkserver.reservationmanager;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ReservationManager {
	public boolean isValid(String confirmationNumber) {
		boolean result = false;
		
		result = isValidConfirmationNumber_Temporary(confirmationNumber);
		
		return result;
	}
	
	boolean isValidConfirmationNumber_Temporary(String confirmationNumber) {
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = null;
		try {
			jsonObject = (JSONObject) jsonParser.parse(confirmationNumber);
		} catch (ParseException e) {
			return false;
		}
		
		if(jsonObject.get("Name") != null) {
			return true;
		}
		
		return false;
	}
}
