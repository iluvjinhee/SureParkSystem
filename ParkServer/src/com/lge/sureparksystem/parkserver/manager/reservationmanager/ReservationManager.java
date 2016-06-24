package com.lge.sureparksystem.parkserver.manager.reservationmanager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
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
import com.lge.sureparksystem.parkserver.manager.databasemanager.StatisticsData;
import com.lge.sureparksystem.parkserver.manager.databasemanager.UserAccountData;
import com.lge.sureparksystem.parkserver.message.DataMessage;
import com.lge.sureparksystem.parkserver.message.MessageValueType;
import com.lge.sureparksystem.parkserver.paymentremoteproxy.PaymentRemoteProxy;
import com.lge.sureparksystem.parkserver.message.MessageParser;
import com.lge.sureparksystem.parkserver.message.MessageType;
import com.lge.sureparksystem.parkserver.message.MessageValueType;
import com.lge.sureparksystem.parkserver.topic.CommunicationManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkHereNetworkManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ParkingLotNetworkManagerTopic;
import com.lge.sureparksystem.parkserver.topic.ReservationManagerTopic;
import com.lge.sureparksystem.parkserver.util.Logger;
import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor.GetterSetterReflection;

public class ReservationManager extends ManagerTask {
	HashMap<String, ParkingLotInfo> parkinglotInfoMap = new HashMap<String, ParkingLotInfo>(); //<parkinglotID, ParkingLotStatus>
	DatabaseProvider dbProvider = null;
	DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
	String curParkinglotId = null;

	public class ReservationManagerListener {
		@Subscribe
		public void onSubscribe(ReservationManagerTopic topic) {
			System.out.println("ReservationManagerListener: " + topic);

			setSessionID(topic);

			processMessage(topic.getJsonObject());
		}
	}

	@Override
	public void init() {
		if (dbProvider == null) {
			dbProvider = DatabaseProvider.getInstance();
		}
		registerEventBus(new ReservationManagerListener());
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

	private void generateDummyParkinglot() {
		ArrayList<String> tempParkinglotList = (ArrayList<String>)dbProvider.getParkingLotList();
		String lotList = tempParkinglotList.get(0);
		//		for (String lotList : tempParkinglotList) {
		int slotNum = 4;
		ArrayList<ParkingSlot> slotList = new ArrayList<ParkingSlot>();
		for (int i = 0; i < slotNum; i++) {
			ParkingSlot slot = new ParkingSlot(i + 1, i % 2, 17293 + i);
			slotList.add(slot);
		}
		parkinglotInfoMap.put(lotList,
				new ParkingLotInfo(lotList, slotNum, 0, 0, slotList));

		Logger.log("parkinglotInfoMap" + parkinglotInfoMap.toString());
		//		}
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
		} else {
			Logger.log("parkinglotId = " + parkinglotId);
			Logger.log("parkinglotInfoMap = " + parkinglotInfoMap.get(parkinglotId).toString());
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
			processParkingLotStatusRequst(jsonObject);
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
		//From ParkHere's Owner
		case CHANGE_GRACE_PERIOD:
			processChangeGracePeriod(jsonObject);
			break;
		case CHANGE_PARKING_FEE:
			processChangeParkingFee(jsonObject);
			break;
		case PARKING_LOT_STATS_REQUEST:
			processParkingLotStatisticsRequst(jsonObject);
			break;
		//To Do List
		case REMOVE_PARKING_LOT:
		case ADD_PARKING_LOT:
			break;

		default:
			break;
		}
	}

	private void processParkingLotStatisticsRequst(JSONObject jsonObject) {
		String period = MessageParser.getString(jsonObject, DataMessage.PERIOD);
		String parkinglotId = MessageParser.getString(jsonObject, DataMessage.PARKING_LOT_ID);

		Logger.log("parkinglotId = " + parkinglotId + ", period = " + period);

		int slotCount = 0;
		ArrayList<String> slotStatusList = new ArrayList<String>();
		ArrayList<String> slotDriverIDList = new ArrayList<String>();
		ArrayList<String> driverOftenList = new ArrayList<String>();
		ArrayList<String> slotTimeList = new ArrayList<String>();
		String occupancyRate = "";
		String revenue = "";
		String cancelRate = "";

		if (isExistValidParkingLot(parkinglotId)) {
			ParkingLotInfo parkinglot = parkinglotInfoMap.get(parkinglotId);
			slotCount = parkinglot.getTotalSlotNum();
			ArrayList<ParkingSlot> slotList = parkinglot.getSlotList();
			for (ParkingSlot slot : slotList) {
				boolean result = false;
				if (slot.getStatus() == ParkingSlot.OCCUPIED) {
					slotStatusList.add(MessageValueType.OCCUPIED);
					int reservationId = slot.getReservationId();
					if (reservationId > 0) {
						String driverId = dbProvider.getReservationInfo(reservationId)
								.getUserEmail();
						int driveropen = dbProvider.getReservationCount(driverId);
						slotDriverIDList.add(driverId);
						if (driveropen > 0) {
							driverOftenList.add(String.valueOf(driveropen));
						} else {
							driverOftenList.add("");
						}
						Date parkingtime = dbProvider.getParkingInfo(reservationId)
								.getParkingTime();
						Calendar cal = Calendar.getInstance();
						cal.setTime(parkingtime);
						long slotTime = cal.getTimeInMillis();
						slotTimeList.add(String.valueOf(slotTime));
						result = true;
					}
				} else {
					slotStatusList.add("empty");
				}
				if (result == false) {
					slotDriverIDList.add("");
					driverOftenList.add("");
					slotTimeList.add("0");
				}
			}
		}

		Calendar cal = Calendar.getInstance();
		Date startTime = null;
		Date endTime = cal.getTime();

		if ("day".equals(period)) {
			cal.add(Calendar.DATE, -1);
		} else if ("week".equals(period)) {
			cal.setTime(endTime);
			cal.add(Calendar.DATE, -7);
		} else if ("month".equals(period)) {
			cal.setTime(endTime);
			cal.add(Calendar.MONTH, -1);
		} else if ("year".equals(period)) {
			cal.setTime(endTime);
			cal.add(Calendar.YEAR, -1);
		} else {

		}
		startTime = cal.getTime();

		StatisticsData statistics = null;
		if (startTime != null) {
			statistics = dbProvider.getStatisticsInformation(parkinglotId, startTime, endTime);
		}
		if (statistics != null) {
			try {
				occupancyRate = String.valueOf(statistics.getOccupancyRate()) + "%";
				revenue = String.valueOf(statistics.getRevenue()) + "$";
				cancelRate = String.valueOf(statistics.getCancelRate()) + "%";
			} catch (ClassCastException e) {
				e.printStackTrace();
			}
		}

		DataMessage dataMessage = new DataMessage(MessageType.PARKING_LOT_STATISTICS);
		dataMessage.setParkingLotID(parkinglotId);
		dataMessage.setSlotCount(slotCount);
		dataMessage.setSlotStatusList(slotStatusList);
		dataMessage.setSlotDriverID(slotDriverIDList);
		dataMessage.setDriverOften(driverOftenList);
		dataMessage.setSlotTime(slotTimeList);
		dataMessage.setOccupancyRate(occupancyRate);
		dataMessage.setRevenue(revenue);
		dataMessage.setCancelRate(cancelRate);

		post(new ParkHereNetworkManagerTopic(dataMessage), this);
	}

	private void processChangeGracePeriod(JSONObject jsonObject) {
		String parkinglotId = MessageParser.getString(jsonObject, DataMessage.PARKING_LOT_ID);
		String graceperiod = MessageParser.getString(jsonObject, DataMessage.GRACE_PERIOD);

		Logger.log("parkinglotId = " + parkinglotId + ", changing graceperiod = " + graceperiod);
		boolean result = dbProvider.updateParkingLotGracePeriod(parkinglotId, graceperiod);

		DataMessage dataMessage = new DataMessage(MessageType.CHANGE_RESPONSE);
		if (result) {
			dataMessage.setResult(MessageValueType.OK);
			dataMessage.setType("graceperiod");
			dataMessage.setValue(graceperiod);
		} else {
			dataMessage.setResult(MessageValueType.NOK);
			dataMessage.setType("graceperiod");
			String curgraceperiod = dbProvider.getParkingLotInfo(parkinglotId).getGracePeriod();
			dataMessage.setValue(curgraceperiod);
		}
		post(new ParkHereNetworkManagerTopic(dataMessage), this);
	}

	private void processChangeParkingFee(JSONObject jsonObject) {
		String parkinglotId = MessageParser.getString(jsonObject, DataMessage.PARKING_LOT_ID);
		String parkingfee = MessageParser.getString(jsonObject, DataMessage.PARKING_FEE);

		Logger.log("parkinglotId = " + parkinglotId + ", changing fee = " + parkingfee);
		boolean result = dbProvider.updateParkingLotFee(parkinglotId, parkingfee);

		DataMessage dataMessage = new DataMessage(MessageType.CHANGE_RESPONSE);
		if (result) {
			dataMessage.setResult(MessageValueType.OK);
			dataMessage.setType("fee");
			dataMessage.setValue(parkingfee);
		} else {
			dataMessage.setResult(MessageValueType.NOK);
			dataMessage.setType("fee");
			String curparkingfee = dbProvider.getParkingLotInfo(parkinglotId).getFee();
			dataMessage.setValue(curparkingfee);
		}
		post(new ParkHereNetworkManagerTopic(dataMessage), this);
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

		post(new ParkHereNetworkManagerTopic(dataMessage), this);
	}

	private void processParkingLotStatusRequst(JSONObject jsonObject) {
		String attendantId = MessageParser.getString(jsonObject, DataMessage.ID);
		if (attendantId == null) {
			Logger.log("attendantId is null");
			attendantId = getSessionID();
		}
		String parkinglotId = dbProvider.getParkingLotId(attendantId);

		//		generateDummyParkinglot(); //temp for test

		int slotCount = 0;
		ArrayList<String> slotStatusList = new ArrayList<String>();
		ArrayList<String> slotDriverIDList = new ArrayList<String>();
		ArrayList<String> driverOftenList = new ArrayList<String>();
		ArrayList<String> slotTimeList = new ArrayList<String>();

		if (isExistValidParkingLot(parkinglotId)) {
			ParkingLotInfo parkinglot = parkinglotInfoMap.get(parkinglotId);
			slotCount = parkinglot.getTotalSlotNum();
			ArrayList<ParkingSlot> slotList = parkinglot.getSlotList();
			for (ParkingSlot slot : slotList) {
				boolean result = false;
				if (slot.getStatus() == ParkingSlot.OCCUPIED) {
					slotStatusList.add(MessageValueType.OCCUPIED);
					int reservationId = slot.getReservationId();
					if (reservationId > 0) {
						String driverId = dbProvider.getReservationInfo(reservationId)
								.getUserEmail();
						int driveropen = dbProvider.getReservationCount(driverId);
						slotDriverIDList.add(driverId);
						if (driveropen > 0) {
							driverOftenList.add(String.valueOf(driveropen));
						} else {
							driverOftenList.add("");
						}
						Date parkingtime = dbProvider.getParkingInfo(reservationId)
								.getParkingTime();
						Calendar cal = Calendar.getInstance();
						cal.setTime(parkingtime);
						long slotTime = cal.getTimeInMillis();
						slotTimeList.add(String.valueOf(slotTime));
						result = true;
					}
				} else {
					slotStatusList.add("empty");
				}
				if (result == false) {
					slotDriverIDList.add("");
					driverOftenList.add("");
					slotTimeList.add("0");
				}
			}
		}

		DataMessage dataMessage = new DataMessage(MessageType.PARKING_LOT_STATUS);
		dataMessage.setSlotCount(slotCount);
		dataMessage.setSlotStatusList(slotStatusList);
		dataMessage.setSlotDriverID(slotDriverIDList);
		dataMessage.setDriverOften(driverOftenList);
		dataMessage.setSlotTime(slotTimeList);

		post(new ParkHereNetworkManagerTopic(dataMessage), this);
	}

	private void processParkingLotInfoRequst(JSONObject jsonObject) {

		//		generateDummyParkinglot(); //temp for test

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

		post(new ParkHereNetworkManagerTopic(dataMessage), this);

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
				String paymentInfo = dbProvider.getReservationCreditInfo(reservation.getId()); //For descryption
				dataMessage.setPaymentInfo(paymentInfo);
				dataMessage.setConfirmationInfo(reservation.getConfirmInfo());

				jsonObject = MessageParser.convertToJSONObject(dataMessage);
				post(new ParkHereNetworkManagerTopic(jsonObject), this);
				result = true;
			}
		}
		if (result == false) {
			sendReservationNOKResponse();
		}
	}

	private void sendReservationNOKResponse() {
		DataMessage dataMessage = new DataMessage(MessageType.RESERVATION_INFORMATION);
		dataMessage.setResult("nok");

		JSONObject jsonObject = MessageParser.convertToJSONObject(dataMessage);
		post(new ParkHereNetworkManagerTopic(jsonObject), this);
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
			sendReservationNOKResponse();
			return;
		}
		if (isExistValidParkingLot(parkinglotId) == false) {
			Logger.log("Invalid ParkingLot Info" + paymentInfo);
			sendReservationNOKResponse();
			return;
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
			String graceperiod = parkinglotData.getGracePeriod();
			newreservation.setGracePeriod(graceperiod);
			newreservation.setReservationState(DatabaseInfo.Reservation.STATE_TYPE.RESERVED);
			dbProvider.createReservation(newreservation);
			sendReservationInformation(driverId);

			ReservationData reservation = dbProvider.getReservationInfo(driverId);
			if (reservation.getId() > 0) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(reservation.getReservationTime());
				cal.add(Calendar.MINUTE, Integer.valueOf(graceperiod));
				Date noShowTime = cal.getTime();
				Logger.log("noShowTime = " + noShowTime);
				ScheduledJob job = new ScheduledJob(reservation.getId());
				Timer jobScheduler = new Timer();
				jobScheduler.schedule(job, noShowTime);
			}

		} else {
			sendReservationNOKResponse();
		}
	}

	private void storeParkingLotInformation(JSONObject jsonObject) {
		int slotCount = MessageParser.getInt(jsonObject, DataMessage.SLOT_COUNT);
		ArrayList<String> slotStaus = MessageParser.getStringList(jsonObject,
				DataMessage.SLOT_STATUS);

		String parkinglotId = getSessionID(); 
//				MessageParser.getString(jsonObject, DataMessage.ID);
		Logger.log("parkinglotId = " + parkinglotId);

		if (parkinglotId == null) {
			Logger.log("parkinglotId is null");
			return;
		}

		if (parkinglotInfoMap.containsKey(parkinglotId)) {
			//			boolean validation = parkinglotInfoMap.get(parkinglotId).checkValidation(slotCount,
			//					slotStaus);
			boolean validation = parkinglotInfoMap.get(parkinglotId).updateSlotStatus(slotCount,
					slotStaus);
			if (validation == false) {
				Logger.log("Slot satus is weird");
				callAttendant(MessageValueType.PARKING_ERROR);
			}
		} else {
			parkinglotInfoMap.put(parkinglotId,
					new ParkingLotInfo(parkinglotId, slotCount, slotStaus));
		}

	}

	private void processEntryGatePassBy(JSONObject jsonObject) {
		String parkinglotId = getSessionID();

		if (isExistValidParkingLot(parkinglotId) == false) {
			return;
		}
		parkinglotInfoMap.get(parkinglotId).changeToMovingState();
	}

	private void processChangedSlotStatus(JSONObject jsonObject) {
		String parkinglotId = getSessionID();

		if (isExistValidParkingLot(parkinglotId) == false) {
			return;
		}
		int changedSlot = MessageParser.getInt(jsonObject, DataMessage.SLOT_NUMBER);
		String status = MessageParser.getString(jsonObject, DataMessage.STATUS);
		int reservationId = 0;
		Date changedtime = Calendar.getInstance().getTime();

		if (MessageValueType.OCCUPIED.equalsIgnoreCase(status)) {
			reservationId = parkinglotInfoMap.get(parkinglotId).getMovingReservationId();
			Logger.log("reservationId = " + reservationId);
			if (reservationId > 0) {
				ParkingData parking = dbProvider.getParkingInfo(reservationId);
				if (parking != null) {
					String strAssingedslot = parking.getAssigned_slot();
					int assingedslot = Integer.valueOf(strAssingedslot);
					dbProvider.updateParkingParkedSlot(reservationId, String.valueOf(changedSlot),
							changedtime);
					dbProvider.updateReservationState(reservationId,
							DatabaseInfo.Reservation.STATE_TYPE.PARKED);
					parkinglotInfoMap.get(parkinglotId).completeParking(changedSlot);
					if (changedSlot != assingedslot) {
						Logger.log("Slot was reallocated..from " + assingedslot + " to "
								+ changedSlot);
						callAttendant(MessageValueType.REALLOCATION);
					}
				}
			} else {
				Logger.log("parkinglotInfoMap = " + parkinglotInfoMap.get(parkinglotId).toString());
				Logger.log("SlotList Info = "
						+ parkinglotInfoMap.get(parkinglotId).getSlotList().toString());
			}
		} else {
			reservationId = parkinglotInfoMap.get(parkinglotId)
					.getRservationIdOfParkingSlot(changedSlot);
			Logger.log("reservationId = " + reservationId);
			if (reservationId > 0) {
				dbProvider.updateParkingUnparkedTime(reservationId, changedtime);
				dbProvider.updateReservationState(reservationId,
						DatabaseInfo.Reservation.STATE_TYPE.UNPARKED);
				parkinglotInfoMap.get(parkinglotId).startUnparking(changedSlot);
			} else {
				Logger.log("parkinglotInfoMap = " + parkinglotInfoMap.get(parkinglotId).toString());
				Logger.log("SlotList Info = "
						+ parkinglotInfoMap.get(parkinglotId).getSlotList().toString());
			}
		}
	}

	private void processExitGateArrive(JSONObject jsonObject) {
		String parkinglotId = getSessionID();

		if (isExistValidParkingLot(parkinglotId) == false) {
			return;
		}
		int reservationId = parkinglotInfoMap.get(parkinglotId).getMovingReservationId();
		String cardNumber = dbProvider.getReservationCreditInfo(reservationId);
		float amount = calculateTotalParkingFee(reservationId);
		boolean result = false;
		if (amount > 0) {
			result = PaymentRemoteProxy.pay(cardNumber, (int)amount);
		}

		if (result == true) {
			dbProvider.updateReservationPayment(reservationId,
					DatabaseInfo.Reservation.STATE_TYPE.UNPARKED, (int)amount);

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
		post(new ParkingLotNetworkManagerTopic(sendMessage), this);
	}

	private void processVerificationConfirmationInfo(JSONObject jsonObject) {
		String confirmationInfo = MessageParser.getString(jsonObject,
				DataMessage.CONFIRMATION_INFO);
		String parkinglotId = MessageParser.getString(jsonObject, DataMessage.ID);
		if (parkinglotId == null) {
			Logger.log("parkinglotId is null");
			parkinglotId = getSessionID();
		}
		if (isExistValidParkingLot(parkinglotId) == false) {
			String notiMsg = MessageValueType.PARKING_ERROR;
			Logger.log(notiMsg);
			callAttendant(notiMsg);
			return;
		}

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

			post(new CommunicationManagerTopic(dataMessage), this);
		} else {
			parkingLot.changeToSlientState();

			DataMessage dataMessage = new DataMessage(MessageType.CONFIRMATION_RESPONSE);
			dataMessage.setResult("nok");

			post(new CommunicationManagerTopic(dataMessage), this);

			String notiMsg = null;
			if (availSlot <= 0) {
				notiMsg = MessageValueType.PARKING_ERROR;
			} else {
				notiMsg = MessageValueType.CONFIRMATION_INFORMATION_ERROR;
			}
			Logger.log(notiMsg);
			callAttendant(notiMsg);
		}
	}

	private void callAttendant(String string) {
		DataMessage message = new DataMessage(MessageType.NOTIFICATION);
		message.setType(string);
		
		post(new ParkHereNetworkManagerTopic(message), this);
	}

	class ScheduledJob extends TimerTask {
		int reservationId = 0;

		public ScheduledJob(int reservedId) {
			super();
			reservationId = reservedId;
		}

		public void run() {
			ReservationData reservation = dbProvider.getReservationInfo(reservationId);
			if (reservation != null && reservation
					.getReservationState() == DatabaseInfo.Reservation.STATE_TYPE.RESERVED) {
				dbProvider.updateReservationState(reservationId,
						DatabaseInfo.Reservation.STATE_TYPE.CANCELED);
				Logger.log("Cancelled by Grace Timeout..reservationId = " + reservationId);
				reservationId = 0;
			}
		}
	}
}
