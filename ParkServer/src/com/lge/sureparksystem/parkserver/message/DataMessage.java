package com.lge.sureparksystem.parkserver.message;

import java.util.ArrayList;

public class DataMessage extends Message {
	public static String ASSIGNED_SLOT = "ASSIGNED_SLOT";
	public static String RESERVATION_CODE = "RESERVATION_CODE";
	
	public static String ENTRYGATELED_STATUS = "entrygateled";
	public static String ENTRYGATE_ARRIVE = "entrygate_arrive";
	public static String ENTRYGATE_STATUS = "entrygate";
	public static String EXITGATELED_STATUS = "exitgateled";
	public static String EXITGATE_ARRIVE = "exitgate_arrive";
	public static String EXITGATE_STATUS = "exitgate";
	public static String ID = "id";
	public static String LED_NUMBER = "led_number";
	public static String LED_STATUS = "led_status";
	public static String PASSWORD = "pwd";
	public static String SENSOR_NUMBER = "sensor_number";
	public static String SLOT_NUMBER = "slot_number";
	public static String SLOT_STATUS = "slot_status";
	public static String STATUS = "status";
	public static String TIMESTAMP = "timestamp";
	
	public static String COMMAND = "command";
	public static String RESULT = "result";
	
	// ParkView
	String assignedSlot;
	String reservationCode;
	
	// Parking Lot
	ArrayList<String> led_status;
	ArrayList<String> slot_status;
	String entry_gate_arrive;
	String entry_gate_status;
	String entry_gate_led_status;
	String exit_gate_arrive;
	String exit_gate_status;
	String exit_gate_led_status;
	String id;
	String pwd;
	String status;
	String result;
	String command;
	
	int led_number;
	int sensor_number;;
	int slot_number;
	
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

	@Override
	public String toString() {
		return "DataMessage [assignedSlot=" + assignedSlot + ", reservationCode=" + reservationCode + ", led_status="
				+ led_status + ", slot_status=" + slot_status + ", entry_gate_arrive=" + entry_gate_arrive
				+ ", entry_gate_status=" + entry_gate_status + ", entry_gate_led_status=" + entry_gate_led_status
				+ ", exit_gate_arrive=" + exit_gate_arrive + ", exit_gate_status=" + exit_gate_status
				+ ", exit_gate_led_status=" + exit_gate_led_status + ", id=" + id + ", pwd=" + pwd + ", status="
				+ status + ", result=" + result + ", command=" + command + ", led_number=" + led_number
				+ ", sensor_number=" + sensor_number + ", slot_number=" + slot_number + ", messageType=" + messageType
				+ ", timestamp=" + timestamp + "]";
	}
}