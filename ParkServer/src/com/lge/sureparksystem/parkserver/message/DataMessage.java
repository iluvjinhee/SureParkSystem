package com.lge.sureparksystem.parkserver.message;

import java.util.ArrayList;

public class DataMessage extends Message {
	public static String ASSIGNED_SLOT = "ASSIGNED_SLOT";
	public static String RESERVATION_CODE = "RESERVATION_CODE";
	
	public static String LED_STATUS = "LED_STATUS";
	public static String SLOT_STATUS = "SLOT_STATUS";
	public static String ENTRYGATE_ARRIVE = "ENTRYGATE_ARRIVE";
	public static String ENTRYGATE_STATUS = "ENTRYGATE_STATUS";
	public static String ENTRYGATELED_STATUS = "ENTRYGATELED_STATUS";
	public static String EXITGATE_ARRIVE = "EXITGATE_ARRIVE";
	public static String EXITGATE_STATUS = "EXITGATE_STATUS";
	public static String EXITGATELED_STATUS = "EXITGATELED_STATUS";
	public static String ID = "ID";
	public static String PWD = "PWD";
	public static String STATUS = "STATUS";
	public static String LED_NUMBER = "LED_NUMBER";
	public static String SENSOR_NUMBER = "SENSOR_NUMBER";
	public static String SLOT_NUMBER = "SLOT_NUMBER";
	public static String TIMESTAMP = "TIMESTAMP";
	
	// ParkView
	String assignedSlot;
	String reservationCode;
	
	// Parking Lot
	ArrayList<String> led_status;
	ArrayList<String> slot_status;
	String entrygate_arrive;
	String entrygate_status;
	String entrygateled_status;
	String exitgate_arrive;
	String exitgate_status;
	String exitgateled_status;
	String id;
	String pwd;
	String status;;
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
		return entrygate_arrive;
	}

	public void setEntrygateArrive(String entrygate_arrive) {
		this.entrygate_arrive = entrygate_arrive;
	}

	public String getEntrygateStatus() {
		return entrygate_status;
	}

	public void setEntrygateStatus(String entrygate_status) {
		this.entrygate_status = entrygate_status;
	}

	public String getEntrygateledStatus() {
		return entrygateled_status;
	}

	public void setEntrygateledStatus(String entrygateled_status) {
		this.entrygateled_status = entrygateled_status;
	}

	public String getExitgateArrive() {
		return exitgate_arrive;
	}

	public void setExitgateArrive(String exitgate_arrive) {
		this.exitgate_arrive = exitgate_arrive;
	}

	public String getExitgateStatus() {
		return exitgate_status;
	}

	public void setExitgateStatus(String exitgate_status) {
		this.exitgate_status = exitgate_status;
	}

	public String getExitgateledStatus() {
		return exitgateled_status;
	}

	public void setExitgateledStatus(String exitgateled_status) {
		this.exitgateled_status = exitgateled_status;
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

	@Override
	public String toString() {
		return "DataMessage [assignedSlot=" + assignedSlot + ", reservationCode=" + reservationCode + ", led_status="
				+ led_status + ", slot_status=" + slot_status + ", entrygate_arrive=" + entrygate_arrive
				+ ", entrygate_status=" + entrygate_status + ", entrygateled_status=" + entrygateled_status
				+ ", exitgate_arrive=" + exitgate_arrive + ", exitgate_status=" + exitgate_status
				+ ", exitgateled_status=" + exitgateled_status + ", id=" + id + ", pwd=" + pwd + ", status=" + status
				+ ", led_number=" + led_number + ", sensor_number=" + sensor_number + ", slot_number=" + slot_number
				+ ", messageType=" + messageType + ", timestamp=" + timestamp + "]";
	}
}