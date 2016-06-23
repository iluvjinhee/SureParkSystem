package com.lge.sureparksystem.parkserver.manager.reservationmanager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.eventbus.Subscribe;
import com.lge.sureparksystem.parkserver.manager.ManagerTask;
import com.lge.sureparksystem.parkserver.manager.databasemanager.DatabaseInfo;
import com.lge.sureparksystem.parkserver.manager.databasemanager.DatabaseProvider;
import com.lge.sureparksystem.parkserver.manager.databasemanager.ParkingData;
import com.lge.sureparksystem.parkserver.manager.databasemanager.ParkingLotData;
import com.lge.sureparksystem.parkserver.manager.databasemanager.ReservationData;
import com.lge.sureparksystem.parkserver.manager.databasemanager.UserAccountData;
import com.lge.sureparksystem.parkserver.message.DataMessage;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkserver.message.MessageValueType;
import com.lge.sureparksystem.parkserver.topic.CommunicationManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkHereNetworkManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkingLotNetworkManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ReservationManagerTopic;
import com.lge.sureparksystem.parkserver.util.Logger;
import com.lge.sureparksystem.parkserver.util.paymentremoteproxy.PaymentRemoteProxy;

public class ReservationManager extends ManagerTask {
	HashMap<String, ParkingLotInfo> parkinglotInfoMap = new HashMap<String, ParkingLotInfo>(); //<parkinglotID, ParkingLotStatus>
	DatabaseProvider dbProvider = null;
	DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
	String curParkinglotId = null;

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

	private String generateQRCode(String username) {
		String strQRCode = null;
		//		String key = "Name";
		strQRCode = "confirmationInfo : " + username;
		return strQRCode;
	}
	
	private boolean isExistValidParkingLot(String parkinglotId) {
		boolean result = true;
		if (parkinglotId == null) {
			Logger.log("parkinglotId is null");
			result = false;
		} else if (parkinglotInfoMap.get(parkinglotId) == null) {
			Logger.log("parkinglotInfoMap.get(parkinglotId) is null");
			result = false;
		}
		return result;
	}
	
	private ParkingLotInfo getParkingLotInfo(String parkinglotId) {
		ParkingLotInfo parkingLotInfo = null;
		if (parkinglotId == null) {
			Logger.log("parkinglotId is null");
			if (parkinglotInfoMap.size() == 1) {
				Iterator<String> iterator = parkinglotInfoMap.keySet().iterator();
				String parkinglot = iterator.next();
				parkingLotInfo = parkinglotInfoMap.get(parkinglot);
			}
		} else if (parkinglotInfoMap.get(parkinglotId) == null) {
			Logger.log("parkinglotInfoMap.get(parkinglotId) is null");
		}
		return parkingLotInfo;
	}

	private boolean isValidConfirmationInfo(String confirmationInfo) {
		boolean result = false;

		int reservationId = dbProvider.getReservationId(confirmationInfo);
		if (reservationId > 0) {
			result = true;
		}

		//		result = isValidConfirmationNumber_Temporary(reservationCode);
		Logger.log("isValidConfirmationInfo() result = " + result);
		return result;
	}

	boolean isValidConfirmationNumber_Temporary(String reservationCode) {
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = null;
		try {
			jsonObject = (JSONObject)jsonParser.parse(reservationCode);
		} catch (ParseException e) {
			return false;
		}

		if (jsonObject.get("Name") != null) {
			return true;
		}

		return false;
	}

	private float calculateTotalParkingFee(int reservationId) {
		float payment = -1.0f;
		ReservationData reservation = dbProvider.getReservationInfo(reservationId);
		ParkingData parkingData = dbProvider.getParkingInfo(reservationId);
		if (reservation != null && parkingData != null) {
			float fee = Float.valueOf(reservation.getParkingFee());
			Calendar cal = Calendar.getInstance();
			cal.setTime(parkingData.getParkingTime());
			long startTimeMs = cal.getTimeInMillis();
			Logger.log("calculateTotalParkingFee() startTimeMs = " + startTimeMs);
			cal.setTime(parkingData.getUnparkingTime());
			long endTimeMs = cal.getTimeInMillis();
			Logger.log("calculateTotalParkingFee() endTimeMs = " + endTimeMs);
			if (endTimeMs > startTimeMs) {
				long durationMin = TimeUnit.MILLISECONDS.toMinutes(endTimeMs - startTimeMs);
				payment = fee + durationMin * fee / 30;
			}
		}
		Logger.log("calculateTotalParkingFee() reservationId = " + reservationId + ", payment = "
				+ payment);
		return payment;
	}

	@Override
	protected void processMessage(JSONObject jsonObject) {
		Logger.log("processMessage() MessageType = "
				+ MessageParser.getMessageType(jsonObject).toString());
		switch (MessageParser.getMessageType(jsonObject)) {
		//From ParkView
		case CONFIRMATION_SEND:
			processVerificationConfirmationInfo(jsonObject);
			break;
		//From ParkingLot
		case PARKING_LOT_INFORMATION:
			storeParkingLotInformation(jsonObject);
			break;
		case ENTRY_GATE_PASSBY:
			processEntryGatePassBy(jsonObject);
			break;
		case SLOT_SENSOR_STATUS:
			processChangedSlotStatus(jsonObject);
			break;
		case EXIT_GATE_ARRIVE:
			processExitGateArrive(jsonObject);
			break;
		//From ParkHere's Attendant
		case PARKING_LOT_STATUS_REQUEST:
			//			processParkingLotStatusRequst(jsonObject);
			break;
		//From ParkHere's Driver
		case PARKING_LOT_INFO_REQUEST:
			processParkingLotInfoRequst(jsonObject);
			break;
		case RESERVATION_REQUEST:
			processReservationRequest(jsonObject);
			break;
		case RESERVATION_INFO_REQUEST:
			processReservationInfoRequest(jsonObject);
			break;
		case CANCEL_REQUEST:
			processReservationCancelRequst(jsonObject);
			break;
		default:
			break;
		}
	}

	private void processReservationCancelRequst(JSONObject jsonObject) {
		String driverId = MessageParser.getString(jsonObject, DataMessage.DRIVER_ID);
		String reservationId = MessageParser.getString(jsonObject, DataMessage.RESERVATION_ID);

		boolean result = dbProvider.updateReservationState(Integer.valueOf(reservationId),
				DatabaseInfo.Reservation.STATE_TYPE.CANCELED);

		DataMessage dataMessage = new DataMessage(MessageType.RESPONSE);
		if (result) {
			dataMessage.setResult(MessageValueType.OK);
		} else {
			dataMessage.setResult(MessageValueType.NOK);
		}
		dataMessage.setType(MessageValueType.CANCEL_RESERVATION);

		getEventBus().post(new ParkHereNetworkManagerTopic(dataMessage));
	}

	private void processParkingLotInfoRequst(JSONObject jsonObject) {

		//[Sart] temporary for test
		//		ArrayList<String> tempParkinglotList = (ArrayList<String>)dbProvider.getParkingLotList();
		//		for (String lotList : tempParkinglotList) {
		//			int slotNum = 4;
		//			ArrayList<String> slotStaus = new ArrayList<String>();
		//			for (int i = 0; i < slotNum; i++) {
		//				slotStaus.add(String.valueOf(i % 2));
		//			}
		//			parkinglotInfoMap.put(lotList,
		//					new ParkingLotInfo(lotList, slotNum, slotStaus));
		//		}
		//[End] temporary for test

		Iterator<String> iterator = parkinglotInfoMap.keySet().iterator();
		int parkingLotCount = parkinglotInfoMap.size();
		Logger.log("parkingLotCount = " + parkingLotCount);
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
		dataMessage.setParkingLotLocationList(parkingLotLocationList);
		dataMessage.setParkingFeeList(parkingFeeList);
		dataMessage.setGracePeriodList(gracePeriodList);

		getEventBus().post(new ParkHereNetworkManagerTopic(dataMessage));

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
				dataMessage.setReservationTime(dateFormat.format(reservation.getReservationTime()));
				dataMessage.setParkingLotID(parkinglotId);
				dataMessage.setParkingLotLocation(parkinglotData.getLotAddress());
				dataMessage.setParkingFee(parkinglotData.getFee());
				dataMessage.setGracePeriod(parkinglotData.getGracePeriod());
				String paymentInfo = dbProvider.getReservationCreditInfo(reservation.getId());	//For descryption
				dataMessage.setPaymentInfo(paymentInfo);
				dataMessage.setConfirmationInfo(reservation.getConfirmInfo());

				jsonObject = MessageParser.convertToJSONObject(dataMessage);
				getEventBus().post(new ParkHereNetworkManagerTopic(jsonObject));
				result = true;
			}
		}
		if (result == false) {
			sendNOKResponseToParkHere();
		}
	}
	
	private void sendNOKResponseToParkHere() {
		DataMessage dataMessage = new DataMessage(MessageType.RESERVATION_INFORMATION);
		dataMessage.setResult("nok");

		JSONObject jsonObject = MessageParser.convertToJSONObject(dataMessage);
		getEventBus().post(new ParkHereNetworkManagerTopic(jsonObject));
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
		
		if (PaymentRemoteProxy.isVaildCreditCard(paymentInfo) == false) {
			Logger.log("Invalid paymentInfo" + paymentInfo);
			sendNOKResponseToParkHere();
		}
		ReservationData newreservation = null;
		ParkingLotData parkinglotData = dbProvider.getParkingLotInfo(parkinglotId);
		UserAccountData userAccount = dbProvider.getUserInfo(driverId);
		if (userAccount != null
				&& parkinglotInfoMap.get(parkinglotId).getAvailableSlotCount() > 0) {
			newreservation = new ReservationData();
			newreservation.setUserEmail(driverId);
			try {
				newreservation.setReservationTime(dateFormat.parse(reservationTime));
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
			newreservation.setParkinglotId(parkinglotId);
			newreservation.setCreditInfo(paymentInfo);
			//			String confirmInfo = generateQRCode(userAccount.getUsername());
			newreservation.setConfirmInfo(userAccount.getUsername());
			newreservation.setParkingFee(parkinglotData.getFee());
			newreservation.setGracePeriod(parkinglotData.getGracePeriod());
			newreservation.setReservationState(DatabaseInfo.Reservation.STATE_TYPE.RESERVED);
			dbProvider.createReservation(newreservation);
			sendReservationInformation(driverId);
		} else {
			sendNOKResponseToParkHere();
		}
	}

	private void storeParkingLotInformation(JSONObject jsonObject) {
		int slotCount = MessageParser.getInt(jsonObject, DataMessage.SLOT_COUNT);
		ArrayList<String> slotStaus = MessageParser.getStringList(jsonObject,
				DataMessage.SLOT_STATUS);

		String parkinglotId = MessageParser.getString(jsonObject, DataMessage.ID);
		//				String parkinglotId = dbProvider.getParkingLotList().get(0); // temporary for test
		//		curParkinglotId = parkinglotId;// temporary for test
		if (parkinglotId == null) {
			Logger.log("parkinglotId is null");
			return;
		}

		if (parkinglotInfoMap.containsKey(parkinglotId)) {
			boolean validation = parkinglotInfoMap.get(parkinglotId).checkValidation(slotCount,
					slotStaus);
			if (validation == false) {
				Logger.log("Slot satus is weird");
				callAttendant("parkinglot error");
			}
		} else {
			parkinglotInfoMap.put(parkinglotId,
					new ParkingLotInfo(parkinglotId, slotCount, slotStaus));
		}

	}

	private void processEntryGatePassBy(JSONObject jsonObject) {
		String parkinglotId = MessageParser.getString(jsonObject, DataMessage.ID);
		//		String parkinglotId = curParkinglotId;
		if (isExistValidParkingLot(parkinglotId) == false) {
			return;
		}
		parkinglotInfoMap.get(parkinglotId).changeToMovingState();
	}

	private void processChangedSlotStatus(JSONObject jsonObject) {
		String parkinglotId = MessageParser.getString(jsonObject, DataMessage.ID);
		//		String parkinglotId = curParkinglotId;
		if (isExistValidParkingLot(parkinglotId) == false) {
			return;
		}
		int changedSlot = MessageParser.getInt(jsonObject, DataMessage.SLOT_NUMBER);
		String status = MessageParser.getString(jsonObject, DataMessage.STATUS);
		int reservationId = 0;//parkinglotInfoMap.get(parkinglotId).getMovingReservationId();
		Date changedtime = Calendar.getInstance().getTime();
		Logger.log("processChangedSlotStatus() status= " + status + ", changedSlot = " + changedSlot); 
		if ("occupied".equalsIgnoreCase(status)) {
			reservationId = parkinglotInfoMap.get(parkinglotId).getMovingReservationId();
			String strAssingedslot = dbProvider.getParkingInfo(reservationId).getAssigned_slot();
			int assingedslot = Integer.valueOf(strAssingedslot);
			dbProvider.updateParkingParkedSlot(reservationId, String.valueOf(changedSlot),
					changedtime);
			dbProvider.updateReservationState(reservationId,
					DatabaseInfo.Reservation.STATE_TYPE.PARKED);
			parkinglotInfoMap.get(parkinglotId).completeParking(changedSlot);
			if (changedSlot != assingedslot) {
				Logger.log("Slot was reallocated..from " + assingedslot + " to " + changedSlot);
				callAttendant("reallocation");
			}
		} else {
			reservationId = parkinglotInfoMap.get(parkinglotId).getRservationIdOfParkingSlot(changedSlot);
			dbProvider.updateParkingUnparkedTime(reservationId, changedtime);
			dbProvider.updateReservationState(reservationId,
					DatabaseInfo.Reservation.STATE_TYPE.UNPARKED);
			parkinglotInfoMap.get(parkinglotId).startUnparking(changedSlot);
		}
	}

	private void processExitGateArrive(JSONObject jsonObject) {
		String parkinglotId = MessageParser.getString(jsonObject, DataMessage.ID);
		//		String parkinglotId = curParkinglotId;
		if (isExistValidParkingLot(parkinglotId) == false) {
			return;
		}
		int reservationId = parkinglotInfoMap.get(parkinglotId).getMovingReservationId();
		String cardNumber = dbProvider.getReservationCreditInfo(reservationId);
		Float amount = calculateTotalParkingFee(reservationId);
		boolean result = false;
		if (amount > 0) {
			result = PaymentRemoteProxy.pay(cardNumber, Integer.valueOf(amount.toString()));
		}
		
		if (result == true) {
			dbProvider.updateReservationPayment(reservationId,
					DatabaseInfo.Reservation.STATE_TYPE.UNPARKED, Integer.valueOf(amount.toString()));
			
			controlExitGate(MessageValueType.UP);
		} else {
			Logger.log("There is a problem in payment");
			
			callAttendant(MessageValueType.PAYMENT_ERROR);
		}
		parkinglotInfoMap.get(parkinglotId).changeToSlientState();
	}

	private void controlExitGate(String command) {
		DataMessage sendMessage = new DataMessage(MessageType.EXIT_GATE_CONTROL);
		sendMessage.setCommand(command);
		getEventBus().post(new ParkingLotNetworkManagerTopic(sendMessage));
	}

	private void processVerificationConfirmationInfo(JSONObject jsonObject) {
		String confirmationInfo = MessageParser.getString(jsonObject,
				DataMessage.CONFIRMATION_INFO);
		String parkinglotId = MessageParser.getString(jsonObject, DataMessage.ID);
		//		String parkinglotId = curParkinglotId; //temporary
		Logger.log("parkinglotId = " + parkinglotId);
		Logger.log("parkingLot = " +  parkinglotInfoMap.get(parkinglotId).toString());

		ParkingLotInfo parkingLot = parkinglotInfoMap.get(parkinglotId);
		int availSlot = parkingLot.getAvailalbeSlotNumber();

		if (availSlot > 0 && isValidConfirmationInfo(confirmationInfo)) {
			int reservationId = dbProvider.getReservationId(confirmationInfo);
			dbProvider.createParkingData(reservationId, String.valueOf(availSlot),
					Calendar.getInstance().getTime());
			parkingLot.changeToArrivalState(reservationId);

			DataMessage dataMessage = new DataMessage(MessageType.CONFIRMATION_RESPONSE);
			dataMessage.setResult("ok");
			dataMessage.setSlotNumber(availSlot);

			getEventBus().post(new CommunicationManagerTopic(dataMessage));
		} else {
			parkingLot.changeToSlientState();

			DataMessage dataMessage = new DataMessage(MessageType.CONFIRMATION_RESPONSE);
			dataMessage.setResult("nok");

			getEventBus().post(new CommunicationManagerTopic(dataMessage));

			String notiMsg = null;
			if (availSlot <= 0) {
				notiMsg = "parkinglot error";
			} else {
				notiMsg = "confirmation information error";
			}
			Logger.log(notiMsg);
			callAttendant(notiMsg);
		}
	}

	private void callAttendant(String string) {
		DataMessage message = new DataMessage(MessageType.NOTIFICATION);
		message.setType(string);

		getEventBus().post(new ParkHereNetworkManagerTopic(message));
	}
}
