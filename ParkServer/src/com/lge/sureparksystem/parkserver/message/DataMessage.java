package com.lge.sureparksystem.parkserver.message;

import java.util.ArrayList;

public class DataMessage extends Message {
	public static String ASSIGNED_SLOT = "ASSIGNED_SLOT";
	public static String RESERVATION_CODE = "RESERVATION_CODE";
	
	public static String ENTRY_GATE_LED_STATUS = "entrygateled";
	public static String ENTRY_GATE_ARRIVE = "entrygate_arrive";
	public static String ENTRY_GATE_STATUS = "entrygate";
	public static String EXIT_GATE_LED_STATUS = "exitgateled";
	public static String EXIT_GATE_ARRIVE = "exitgate_arrive";
	public static String EXIT_GATE_STATUS = "exitgate";
	public static String ID = "id";
	public static String LED_NUMBER = "led_number";
	public static String LED_STATUS = "led_status";
	public static String PASSWORD = "pwd";
	public static String SENSOR_NUMBER = "sensor_number";
	public static String SLOT_COUNT = "slot_count";
	public static String SLOT_NUMBER = "slot_number";
	public static String SLOT_STATUS = "slot_status";
	public static String STATUS = "status";
	public static String TIMESTAMP = "timestamp";
	
	// Parking Here
	public static String CONFIRMATION_INFO = "confirmationinfo";
	public static String DRIVER_ID = "driver_id";
	public static String DRIVER_OFTEN = "driver_often";
	public static String GRACE_PERIOD = "graceperiod";
	public static String MESSAGE_TYPE = "messagetype";
	public static String PARKING_FEE = "parkingfee";
	public static String PARKING_LOT_COUNT = "parkinglot_count";
	public static String PARKING_LOT_ID = "parkinglot_id";
	public static String PARKING_LOT_LOCATION = "parkinglot_location";
	public static String PAYMENT_INFO = "paymentinfo";
	public static String RESERVATION_ID = "reservation_id";
	public static String RESERVATION_TIME = "reservation_time";
	public static String SLOT_DRIVER_ID = "slot_driverid";
	public static String SLOT_TIME = "slot_time";
	public static String TYPE = "type";
	
	public static String COMMAND = "command";
	public static String RESULT = "result";
	
	// ParkView
	String assignedSlot;
	String reservationCode;
	
	// Parking Lot
	ArrayList<String> led_status;
	ArrayList<String> slot_status;
	String command;
	String entry_gate_arrive;
	String entry_gate_led_status;
	String entry_gate_status;
	String exit_gate_arrive;
	String exit_gate_led_status;
	String exit_gate_status;
	String id;
	String pwd;
	String result;
	String status;
	int led_number;
	int sensor_number;;
	int slot_number;
	
	// Park Here
	ArrayList<String> driver_often;
	ArrayList<String> grace_period;
	ArrayList<String> parking_fee;
	ArrayList<String> parking_lot_id;
	ArrayList<String> parking_lot_location;
	ArrayList<String> slot_driver_id;
	ArrayList<String> slot_time;
	String confirmation_info;
	String driver_id;
	String payment_info;
	String reservation_id;
	String reservation_time;
	String type;
	int parking_lot_count;
	int slot_count;
	
	public DataMessage() {
		super();
	}

	public DataMessage(MessageType messageType) {
		super(messageType);
	}

	public String getAssignedSlot() {
		return assignedSlot;
	}

	public void setAssignedSlot(String assignedSlot) {
		this.assignedSlot = assignedSlot;
	}

	public String getReservationCode() {
		return reservationCode;
	}

	public void setReservationCode(String reservationCode) {
		this.reservationCode = reservationCode;
	}

	public int getSensorNumber() {
		return sensor_number;
	}

	public void setSensorNumber(int sensor_number) {
		this.sensor_number = sensor_number;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<String> getLedStatus() {
		return led_status;
	}

	public void setLedStatus(ArrayList<String> led_status) {
		this.led_status = led_status;
	}

	public ArrayList<String> getSlotStatus() {
		return slot_status;
	}

	public void setSlotStatus(ArrayList<String> slot_status) {
		this.slot_status = slot_status;
	}

	public String getEntrygateArrive() {
		return entry_gate_arrive;
	}

	public void setEntrygateArrive(String entrygate_arrive) {
		this.entry_gate_arrive = entrygate_arrive;
	}

	public String getEntrygateStatus() {
		return entry_gate_status;
	}

	public void setEntryGateStatus(String entrygate_status) {
		this.entry_gate_status = entrygate_status;
	}

	public String getEntrygateledStatus() {
		return entry_gate_led_status;
	}

	public void setEntryGateLEDStatus(String entrygateled_status) {
		this.entry_gate_led_status = entrygateled_status;
	}

	public String getExitgateArrive() {
		return exit_gate_arrive;
	}

	public void setExitgateArrive(String exitgate_arrive) {
		this.exit_gate_arrive = exitgate_arrive;
	}

	public String getExitgateStatus() {
		return exit_gate_status;
	}

	public void setExitGateStatus(String exitgate_status) {
		this.exit_gate_status = exitgate_status;
	}

	public String getExitgateledStatus() {
		return exit_gate_led_status;
	}

	public void setExitGateLEDStatus(String exitgateled_status) {
		this.exit_gate_led_status = exitgateled_status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public int getLedNumber() {
		return led_number;
	}

	public void setLedNumber(int led_number) {
		this.led_number = led_number;
	}

	public int getSlotNumber() {
		return slot_number;
	}

	public void setSlotNumber(int slot_number) {
		this.slot_number = slot_number;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getCommand() {
		return command;
	}
	
	public void setCommand(String command) {
		this.command = command;
	}
	
	public ArrayList<String> getDriverOften() {
		return driver_often;
	}

	public void setDriverOften(ArrayList<String> driver_often) {
		this.driver_often = driver_often;
	}

	public ArrayList<String> getGracePeriod() {
		return grace_period;
	}

	public void setGracePeriod(ArrayList<String> graceperiod) {
		this.grace_period = graceperiod;
	}

	public ArrayList<String> getParkingFee() {
		return parking_fee;
	}

	public void setParkingFee(ArrayList<String> parkingfee) {
		this.parking_fee = parkingfee;
	}

	public ArrayList<String> getParkingLotId() {
		return parking_lot_id;
	}

	public void setParkingLotId(ArrayList<String> parkinglot_id) {
		this.parking_lot_id = parkinglot_id;
	}

	public ArrayList<String> getParkingLotLocation() {
		return parking_lot_location;
	}

	public void setParkingLotLocation(ArrayList<String> parkinglot_location) {
		this.parking_lot_location = parkinglot_location;
	}

	public ArrayList<String> getSlotDriverId() {
		return slot_driver_id;
	}

	public void setSlotDriverId(ArrayList<String> slot_driverid) {
		this.slot_driver_id = slot_driverid;
	}

	public ArrayList<String> getSlotTime() {
		return slot_time;
	}

	public void setSlotTime(ArrayList<String> slot_time) {
		this.slot_time = slot_time;
	}

	public String getConfirmationInfo() {
		return confirmation_info;
	}

	public void setConfirmationInfo(String confirmationinfo) {
		this.confirmation_info = confirmationinfo;
	}

	public String getDriverID() {
		return driver_id;
	}

	public void setDriverId(String driver_id) {
		this.driver_id = driver_id;
	}

	public String getPaymentInfo() {
		return payment_info;
	}

	public void setPaymentInfo(String paymentinfo) {
		this.payment_info = paymentinfo;
	}

	public String getReservationID() {
		return reservation_id;
	}

	public void setReservationId(String reservation_id) {
		this.reservation_id = reservation_id;
	}

	public String getReservationTime() {
		return reservation_time;
	}

	public void setReservationTime(String reservation_time) {
		this.reservation_time = reservation_time;
	}

	public int getParkingLotCount() {
		return parking_lot_count;
	}

	public void setParkingLotCount(int parkinglot_count) {
		this.parking_lot_count = parkinglot_count;
	}

	public int getSlotCount() {
		return slot_count;
	}

	public void setSlotCount(int slot_count) {
		this.slot_count = slot_count;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "DataMessage [assignedSlot=" + assignedSlot + ", reservationCode=" + reservationCode + ", led_status="
				+ led_status + ", slot_status=" + slot_status + ", command=" + command + ", entry_gate_arrive="
				+ entry_gate_arrive + ", entry_gate_led_status=" + entry_gate_led_status + ", entry_gate_status="
				+ entry_gate_status + ", exit_gate_arrive=" + exit_gate_arrive + ", exit_gate_led_status="
				+ exit_gate_led_status + ", exit_gate_status=" + exit_gate_status + ", id=" + id + ", pwd=" + pwd
				+ ", result=" + result + ", status=" + status + ", led_number=" + led_number + ", sensor_number="
				+ sensor_number + ", slot_number=" + slot_number + ", driver_often=" + driver_often + ", grace_period="
				+ grace_period + ", parking_fee=" + parking_fee + ", parking_lot_id=" + parking_lot_id
				+ ", parking_lot_location=" + parking_lot_location + ", slot_driver_id=" + slot_driver_id
				+ ", slot_time=" + slot_time + ", confirmation_info=" + confirmation_info + ", driver_id=" + driver_id
				+ ", payment_info=" + payment_info + ", reservation_id=" + reservation_id + ", reservation_time="
				+ reservation_time + ", type=" + type + ", parking_lot_count=" + parking_lot_count + ", slot_count="
				+ slot_count + ", messageType=" + messageType + ", timestamp=" + timestamp + "]";
	}
}