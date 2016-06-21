package com.lge.sureparksystem.parkserver.message;

import java.util.ArrayList;

public class DataMessage extends Message {
	public static final String PORT = "port";
	
	// Parking Lot
	public static final String ENTRY_GATE_LED_STATUS = "entrygateled";
	public static final String ENTRY_GATE_ARRIVE = "entrygate_arrive";
	public static final String ENTRY_GATE_STATUS = "entrygate";
	public static final String EXIT_GATE_LED_STATUS = "exitgateled";
	public static final String EXIT_GATE_ARRIVE = "exitgate_arrive";
	public static final String EXIT_GATE_STATUS = "exitgate";
	public static final String ID = "id";
	public static final String LED_NUMBER = "led_number";
	public static final String LED_STATUS = "led_status";
	public static final String PASSWORD = "pwd";
	public static final String SENSOR_NUMBER = "sensor_number";
	public static final String SLOT_COUNT = "slot_count";
	public static final String SLOT_NUMBER = "slot_number";
	public static final String SLOT_STATUS = "slot_status";
	public static final String STATUS = "status";
	public static final String TIMESTAMP = "timestamp";
	
	// Park Here
	public static final String CONFIRMATION_INFO = "confirmationinfo";
	public static final String DRIVER_ID = "driver_id";
	public static final String DRIVER_OFTEN = "driver_often";
	public static final String GRACE_PERIOD = "graceperiod";
	public static final String MESSAGE_TYPE = "messagetype";
	public static final String PARKING_FEE = "parkingfee";
	public static final String PARKING_LOT_COUNT = "parkinglot_count";
	public static final String PARKING_LOT_ID = "parkinglot_id";
	public static final String PARKING_LOT_LOCATION = "parkinglot_location";
	public static final String PAYMENT_INFO = "paymentinfo";
	public static final String RESERVATION_ID = "reservation_id";
	public static final String RESERVATION_TIME = "reservation_time";
	public static final String SLOT_DRIVER_ID = "slot_driverid";
	public static final String SLOT_TIME = "slot_time";
	public static final String TYPE = "type";
	
	public static final String ADDRESS = "address";
	public static final String CANCEL_RATE = "cancel_rate";
	public static final String NAME = "name";
	public static final String OCCUPANCY_RATE = "occupancy_rate";
	public static final String PARKINGLOT_COUNT = "parkinglot_count";
	public static final String PERIOD = "period";
	public static final String REVENUE = "revenue";
	public static final String VALUE = "value";
	
	public static final String COMMAND = "command";
	public static final String RESULT = "result";
	
	int port;
	
	// ParkView
	String confirmationInfo;
	
	// Parking Lot
	ArrayList<String> ledStatusList;
	ArrayList<String> slotStatusList;
	String command;
	String entryGateArrive;
	String entryGateLEDStatus;
	String entryGateStatus;
	String exitGateArrive;
	String exitGateLEDStatus;
	String exitGateStatus;
	String id;
	String password;
	String result;
	String status;
	int ledNumber;
	int sensorNumber;;
	int slotNumber;
	
	// Park Here
	ArrayList<String> driverOftenList;
	ArrayList<String> gracePeriodList;
	ArrayList<String> parkingFeeList;
	ArrayList<String> parkingLotIDList;
	ArrayList<String> parkingLotLocationList;
	ArrayList<String> slotDriverIDList;
	ArrayList<String> slotTimeList;
	String address;
	String cancelRate;
	String driverID;
	String name;
	String occupancyRate;
	String paymentInfo;
	String period;
	String reservationID;
	String reservationTime;
	String revenue;
	String type;
	String value;
	int parkingLotCount;
	int slotCount;
	
	public DataMessage() {
		super();
	}

	public DataMessage(MessageType messageType) {
		super(messageType);
	}

	public int getSensorNumber() {
		return sensorNumber;
	}

	public void setSensorNumber(int sensor_number) {
		this.sensorNumber = sensor_number;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<String> getLEDStatusList() {
		return ledStatusList;
	}

	public void setLedStatusList(ArrayList<String> ledStatusList) {
		this.ledStatusList = ledStatusList;
	}

	public ArrayList<String> getSlotStatusList() {
		return slotStatusList;
	}

	public void setSlotStatus(ArrayList<String> slotStatusList) {
		this.slotStatusList = slotStatusList;
	}

	public String getEntrygateArrive() {
		return entryGateArrive;
	}

	public void setEntrygateArrive(String entrygate_arrive) {
		this.entryGateArrive = entrygate_arrive;
	}

	public String getEntrygateStatus() {
		return entryGateStatus;
	}

	public void setEntryGateStatus(String entrygate_status) {
		this.entryGateStatus = entrygate_status;
	}

	public String getEntrygateledStatus() {
		return entryGateLEDStatus;
	}

	public void setEntryGateLEDStatus(String entrygateled_status) {
		this.entryGateLEDStatus = entrygateled_status;
	}

	public String getExitgateArrive() {
		return exitGateArrive;
	}

	public void setExitgateArrive(String exitgate_arrive) {
		this.exitGateArrive = exitgate_arrive;
	}

	public String getExitgateStatus() {
		return exitGateStatus;
	}

	public void setExitGateStatus(String exitgate_status) {
		this.exitGateStatus = exitgate_status;
	}

	public String getExitgateledStatus() {
		return exitGateLEDStatus;
	}

	public void setExitGateLEDStatus(String exitgateled_status) {
		this.exitGateLEDStatus = exitgateled_status;
	}

	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getLedNumber() {
		return ledNumber;
	}

	public void setLedNumber(int led_number) {
		this.ledNumber = led_number;
	}

	public int getSlotNumber() {
		return slotNumber;
	}

	public void setSlotNumber(int slot_number) {
		this.slotNumber = slot_number;
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
	
	public ArrayList<String> getDriverOftenList() {
		return driverOftenList;
	}

	public void setDriverOften(ArrayList<String> driverOftenList) {
		this.driverOftenList = driverOftenList;
	}

	public ArrayList<String> getGracePeriodList() {
		return gracePeriodList;
	}

	public void setGracePeriod(ArrayList<String> gracePeriodList) {
		this.gracePeriodList = gracePeriodList;
	}

	public ArrayList<String> getParkingFeeList() {
		return parkingFeeList;
	}

	public void setParkingFee(ArrayList<String> parkingFeeList) {
		this.parkingFeeList = parkingFeeList;
	}

	public ArrayList<String> getParkingLotIDList() {
		return parkingLotIDList;
	}

	public void setParkingLotIDList(ArrayList<String> parkingLotIDList) {
		this.parkingLotIDList = parkingLotIDList;
	}

	public ArrayList<String> getParkingLotLocationList() {
		return parkingLotLocationList;
	}

	public void setParkingLotLocation(ArrayList<String> parkingLotLocationList) {
		this.parkingLotLocationList = parkingLotLocationList;
	}

	public ArrayList<String> getSlotDriverIDList() {
		return slotDriverIDList;
	}

	public void setSlotDriverID(ArrayList<String> slotDriverIDList) {
		this.slotDriverIDList = slotDriverIDList;
	}

	public ArrayList<String> getSlotTimeList() {
		return slotTimeList;
	}

	public void setSlotTime(ArrayList<String> slotTimeList) {
		this.slotTimeList = slotTimeList;
	}

	public String getConfirmationInfo() {
		return confirmationInfo;
	}

	public void setConfirmationInfo(String confirmationinfo) {
		this.confirmationInfo = confirmationinfo;
	}

	public String getDriverID() {
		return driverID;
	}

	public void setDriverID(String driverID) {
		this.driverID = driverID;
	}

	public String getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(String paymentInfo) {
		this.paymentInfo = paymentInfo;
	}

	public String getReservationID() {
		return reservationID;
	}

	public void setReservationId(String reservationID) {
		this.reservationID = reservationID;
	}

	public String getReservationTime() {
		return reservationTime;
	}

	public void setReservationTime(String reservationTime) {
		this.reservationTime = reservationTime;
	}

	public int getParkingLotCount() {
		return parkingLotCount;
	}

	public void setParkingLotCount(int parkingLotCount) {
		this.parkingLotCount = parkingLotCount;
	}

	public int getSlotCount() {
		return slotCount;
	}

	public void setSlotCount(int slotCount) {
		this.slotCount = slotCount;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCancelRate() {
		return cancelRate;
	}

	public void setCancelRate(String cancelRate) {
		this.cancelRate = cancelRate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOccupancyRate() {
		return occupancyRate;
	}

	public void setOccupancyRate(String occupancyRate) {
		this.occupancyRate = occupancyRate;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getRevenue() {
		return revenue;
	}

	public void setRevenue(String revenue) {
		this.revenue = revenue;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return "DataMessage [port=" + port + ", confirmationInfo=" + confirmationInfo + ", ledStatusList="
				+ ledStatusList + ", slotStatusList=" + slotStatusList + ", command=" + command + ", entryGateArrive="
				+ entryGateArrive + ", entryGateLEDStatus=" + entryGateLEDStatus + ", entryGateStatus="
				+ entryGateStatus + ", exitGateArrive=" + exitGateArrive + ", exitGateLEDStatus=" + exitGateLEDStatus
				+ ", exitGateStatus=" + exitGateStatus + ", id=" + id + ", password=" + password + ", result=" + result
				+ ", status=" + status + ", ledNumber=" + ledNumber + ", sensorNumber=" + sensorNumber + ", slotNumber="
				+ slotNumber + ", driverOftenList=" + driverOftenList + ", gracePeriodList=" + gracePeriodList
				+ ", parkingFeeList=" + parkingFeeList + ", parkingLotIDList=" + parkingLotIDList
				+ ", parkingLotLocationList=" + parkingLotLocationList + ", slotDriverIDList=" + slotDriverIDList
				+ ", slotTimeList=" + slotTimeList + ", address=" + address + ", cancelRate=" + cancelRate
				+ ", driverID=" + driverID + ", name=" + name + ", occupancyRate=" + occupancyRate + ", paymentInfo="
				+ paymentInfo + ", period=" + period + ", reservationID=" + reservationID + ", reservationTime="
				+ reservationTime + ", revenue=" + revenue + ", type=" + type + ", value=" + value
				+ ", parkingLotCount=" + parkingLotCount + ", slotCount=" + slotCount + ", messageType=" + messageType
				+ ", timestamp=" + timestamp + "]";
	}
}