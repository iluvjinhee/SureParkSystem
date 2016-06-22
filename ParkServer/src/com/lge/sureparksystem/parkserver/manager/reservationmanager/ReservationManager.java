package com.lge.sureparksystem.parkserver.manager.reservationmanager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.manager.ManagerTask;
import com.lge.sureparksystem.parkserver.manager.databasemanager.DatabaseInfo;
import com.lge.sureparksystem.parkserver.manager.databasemanager.DatabaseProvider;
import com.lge.sureparksystem.parkserver.manager.databasemanager.ParkingLotData;
import com.lge.sureparksystem.parkserver.manager.databasemanager.ReservationData;
import com.lge.sureparksystem.parkserver.message.DataMessage;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkserver.topic.ParkHereNetworkManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkViewNetworkManagerTopic;
import com.lge.sureparksystem.parkserver.topic.CommunicationManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkHereNetworkManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ReservationManagerTopic;

public class ReservationManager extends ManagerTask {
    HashMap<String, ParkingLotStatus> parkinglotSatusMap = new HashMap<String, ParkingLotStatus>();  //<parkinglotID, ParkingLotStatus>
    DatabaseProvider dbProvider = null;

    public class ReservationManagerListener {
        @Subscribe
        public void onSubscribe(ReservationManagerTopic topic) {
            System.out.println("ReservationManagerListener: " + topic);

            processMessage(topic.getJsonObject());
        }
    }

    @Override
    public void init() {
        if (dbProvider == null) {
            dbProvider = DatabaseProvider.getInstance();
        }
        getEventBus().register(new ReservationManagerListener());
    }

    @Override
    public void run() {
        while (loop) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

	public boolean isValid(String reservationCode) {
		boolean result = false;
		
		result = isValidConfirmationNumber_Temporary(reservationCode);
		
		return result;
	}
	
	boolean isValidConfirmationNumber_Temporary(String reservationCode) {
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = null;
		try {
			jsonObject = (JSONObject) jsonParser.parse(reservationCode);
		} catch (ParseException e) {
			return false;
		}
		
		if(jsonObject.get("Name") != null) {
			return true;
		}
		
		return false;
	}
	
	public int getAvailableSlot() {
		Random r = new Random();

		return r.nextInt(getSlotSize()) + 1;
	}
	
	private int getSlotSize() {
		return 4;
	}

    @Override
    protected void processMessage(JSONObject jsonObject) {
        switch (MessageParser.getMessageType(jsonObject)) {
        case CONFIRMATION_SEND:
            processVerification(jsonObject);
            break;
        case PARKING_LOT_INFORMATION:
            storeParkingLotInformation(jsonObject);
            break;
        case RESERVATION_REQUEST:
            processReservationRequest(jsonObject);
            break;
        case RESERVATION_INFO_REQUEST:
            processReservationInfoRequest(jsonObject);
            break;
        case PARKING_LOT_INFO_REQUEST:
            processParkingLotInfoRequst(jsonObject);
            break;
        default:
            break;
        }
    }

    private void processParkingLotInfoRequst(JSONObject jsonObject) {

        Iterator<String> iterator = parkinglotSatusMap.keySet().iterator();
        int parkingLotCount = parkinglotSatusMap.size();
        ArrayList<String> parkingLotIDList = new ArrayList<String>();
        ArrayList<String> parkingLotLocationList = new ArrayList<String>();
        ArrayList<String> parkingFeeList = new ArrayList<String>();
        ArrayList<String> gracePeriodList = new ArrayList<String>();
        while (iterator.hasNext()) {
            String parkinglotId = iterator.next();
            ParkingLotData parkinglotData = dbProvider.getParkingLotInfo(parkinglotId);            
            parkingLotIDList.add(parkinglotId);
            parkingLotLocationList.add(parkinglotData.getLotAddress());
            parkingFeeList.add(parkinglotData.getFee());
            gracePeriodList.add(parkinglotData.getGracePeriod());
        }
        DataMessage dataMessage = new DataMessage(MessageType.PARKING_LOT_LIST);
        dataMessage.setParkingLotCount(parkingLotCount);
        dataMessage.setParkingLotIDList(parkingLotIDList);
        dataMessage.setParkingLotLocation(parkingLotLocationList);
        dataMessage.setParkingFee(parkingFeeList);
        dataMessage.setGracePeriod(gracePeriodList);
        
        jsonObject = MessageParser.convertToJSONObject(dataMessage);
        getEventBus().post(new ParkHereNetworkManagerTopic(jsonObject));
        
    }
    
    private void sendReservationInformation(String driverId) {
        boolean result = false;
        JSONObject jsonObject = null;
        ReservationData reservation = dbProvider.getReservationInfo(driverId);
        if (reservation != null) {
            String parkinglotId = reservation.getParkinglotId();
            ParkingLotData parkinglotData = dbProvider.getParkingLotInfo(parkinglotId);
            if (parkinglotData != null) {
                DataMessage dataMessage = new DataMessage(MessageType.RESERVATION_INFORMATION);
                dataMessage.setResult("ok");
                dataMessage.setReservationId(String.valueOf(reservation.getId()));
                Calendar cal = Calendar.getInstance();
                cal.setTime(reservation.getReservationTime());
                dataMessage.setReservationTime(String.valueOf(cal.getTimeInMillis()));
                ArrayList<String> parkinglotIDList = new ArrayList<String>();
                parkinglotIDList.add(parkinglotId);
                dataMessage.setParkingLotIDList(parkinglotIDList);
                ArrayList<String> parkinglotLocationList = new ArrayList<String>();
                parkinglotLocationList.add(parkinglotData.getLotAddress());
                dataMessage.setParkingLotLocation(parkinglotLocationList);
                ArrayList<String> parkingFeeList = new ArrayList<String>();
                parkingFeeList.add(parkinglotData.getFee());
                dataMessage.setParkingFee(parkingFeeList);
                ArrayList<String> gracePeriodList = new ArrayList<String>();
                gracePeriodList.add(parkinglotData.getGracePeriod());
                dataMessage.setGracePeriod(gracePeriodList);
                dataMessage.setPaymentInfo(reservation.getCreditInfo());
                dataMessage.setConfirmationInfo(reservation.getConfirmInfo());

                jsonObject = MessageParser.convertToJSONObject(dataMessage);
                getEventBus().post(new ParkHereNetworkManagerTopic(jsonObject));
                result = true;
            }
        }
        if (result == false) {
            DataMessage dataMessage = new DataMessage(MessageType.RESERVATION_INFORMATION);
            dataMessage.setResult("nok");

            jsonObject = MessageParser.convertToJSONObject(dataMessage);
            getEventBus().post(new ParkHereNetworkManagerTopic(jsonObject));
        }
    }

    private void processReservationInfoRequest(JSONObject jsonObject) {

        String driverId = MessageParser.getString(jsonObject, DataMessage.DRIVER_ID);
        sendReservationInformation(driverId);

    }

    private void processReservationRequest(JSONObject jsonObject) {

        String driverId = MessageParser.getString(jsonObject, DataMessage.DRIVER_ID);
        String parkinglotId = MessageParser.getString(jsonObject, DataMessage.PARKING_LOT_ID);
        String reservationTime = MessageParser.getString(jsonObject, DataMessage.RESERVATION_TIME);
        String paymentInfo = MessageParser.getString(jsonObject, DataMessage.PAYMENT_INFO);

        ReservationData newreservation = null;
        ParkingLotData parkinglotData = dbProvider.getParkingLotInfo(parkinglotId);
        if (parkinglotSatusMap.get(parkinglotId).getAvailableSlotCount() > 0) {
            newreservation = new ReservationData();
            newreservation.setUserEmail(driverId);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(Long.valueOf(reservationTime));
            newreservation.setReservationTime(cal.getTime());
            newreservation.setParkinglotId(parkinglotId);
            newreservation.setCreditInfo(paymentInfo);
            newreservation.setParkingFee(parkinglotData.getFee());
            newreservation.setGracePeriod(parkinglotData.getGracePeriod());
            newreservation.setReservationState(DatabaseInfo.Reservation.STATE_TYPE.PARKED);
            dbProvider.createReservation(newreservation);
            sendReservationInformation(driverId);
        }
    }

    private void storeParkingLotInformation(JSONObject jsonObject) {
        int slotCount = MessageParser.getInt(jsonObject, DataMessage.SLOT_COUNT);
        ArrayList<String> slotStaus = MessageParser.getStringList(jsonObject,
                DataMessage.SLOT_STATUS);

        String parkinglotId = dbProvider.getParkingLotList().get(0); // temporary for test

        parkinglotSatusMap.put(parkinglotId, new ParkingLotStatus(parkinglotId, slotCount, slotStaus));

    }

	private void processVerification(JSONObject jsonObject) {
		String confirmationInfo = MessageParser.getString(jsonObject, DataMessage.CONFIRMATION_INFO);
		
		if(isValid(confirmationInfo)) {
			DataMessage dataMessage = new DataMessage(MessageType.CONFIRMATION_RESPONSE);
			dataMessage.setResult("ok");
			dataMessage.setSlotNumber(getAvailableSlot());
			
			getEventBus().post(new CommunicationManagerTopic(dataMessage));
		}
		else {
			DataMessage dataMessage = new DataMessage(MessageType.CONFIRMATION_RESPONSE);
			dataMessage.setResult("nok");
			
			getEventBus().post(new CommunicationManagerTopic(dataMessage));
		}
	}
	
	private void callAttendant(String string) {
		DataMessage message = new DataMessage(MessageType.NOTIFICATION);
		message.setType(string);
		
		getEventBus().post(new ParkHereNetworkManagerTopic(message));		
	}
}
