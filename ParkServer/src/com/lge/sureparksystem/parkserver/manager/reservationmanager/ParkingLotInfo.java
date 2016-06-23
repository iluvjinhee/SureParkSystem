package com.lge.sureparksystem.parkserver.manager.reservationmanager;

import java.util.ArrayList;

import com.lge.sureparksystem.parkserver.util.Logger;

public class ParkingLotInfo {
	private String parkingLotId;
	private int totalSlotNum;
	private int parkingState = PARKINGLOT_STATE.SLIENT;
	private int movingReservationId;
	private ArrayList<ParkingSlot> slotList = new ArrayList<ParkingSlot>();

	public interface PARKINGLOT_STATE {
		public static final int SLIENT = 0;
		public static final int ARRIVAL = 1;
		public static final int MOVING = 2;
	}

	public ParkingLotInfo() {
	}

	public ParkingLotInfo(String parkingLotId, ArrayList<String> slotStatusList) {
		this.parkingLotId = parkingLotId;
		Integer slotIndex = 0;
		for (String slotSatus : slotStatusList) {
			slotList.add(new ParkingSlot(slotIndex, Integer.valueOf(slotSatus)));
			slotIndex++;
		}
		this.totalSlotNum = slotList.size();
		this.parkingState = PARKINGLOT_STATE.SLIENT;
	}

	public ParkingLotInfo(String parkingLotId, int totalSlotNum,
			ArrayList<String> slotStatusList) {
		this.parkingLotId = parkingLotId;
		this.totalSlotNum = totalSlotNum;
		Integer slotIndex = 0;
		for (String slotSatus : slotStatusList) {
			slotList.add(new ParkingSlot(slotIndex, Integer.valueOf(slotSatus)));
			slotIndex++;
		}
		this.parkingState = PARKINGLOT_STATE.SLIENT;
	}

	public boolean checkValidation(int slotNum, ArrayList<String> slotStatusList) {
		boolean isValid = true;
		if (totalSlotNum != slotNum) {
			Logger.log("totalSlotNum = " + totalSlotNum + "slotNum = " + slotNum);
			isValid = false;
		} else {
			for (int slot = 0; slot < totalSlotNum; slot++) {
				if (slotList.get(slot).getStatus() != Integer.valueOf(slotStatusList.get(slot))) {
					Logger.log("slotList = " + slotList.toString());
					Logger.log("slotStatusList = " + slotStatusList.toString());
					isValid = false;
					break;
				}
			}
		}
		Logger.log("parkingLotId = " + parkingLotId + ", isValid = " + isValid);
		return isValid;
	}

	public int getOccupiedSlotCount() {
		int occupiedSlotCnt = 0;
		for (ParkingSlot slot : slotList) {
			if (slot.getStatus() == ParkingSlot.OCCUPIED) {
				occupiedSlotCnt++;
			}
		}
		Logger.log("parkingLotId = " + parkingLotId + ", occupiedSlot count = " + occupiedSlotCnt);
		return occupiedSlotCnt;
	}

	public int getAvailableSlotCount() {
		int availableSlotNum = 0;
		for (ParkingSlot slot : slotList) {
			if (slot.getStatus() == ParkingSlot.EMPTY) {
				availableSlotNum++;
			}
		}
		Logger.log("parkingLotId = " + parkingLotId + ", availableSlotNum count = "
				+ availableSlotNum);
		return availableSlotNum;
	}

	public boolean changeToArrivalState(int reservationId) {
		if (parkingState != PARKINGLOT_STATE.SLIENT) {
			Logger.log("Please wait.. Parkinglot is busy.");
		}
		parkingState = PARKINGLOT_STATE.ARRIVAL;
		movingReservationId = reservationId;
		Logger.log("parkingState changed to ARRIVAL.reservationId = " + reservationId);
		return true;
	}

	public void changeToMovingState() {
		parkingState = PARKINGLOT_STATE.MOVING;
		Logger.log("changeToMovingState() movingReservationId = " + movingReservationId);
		Logger.log("parkingState changed to MOVING.");
	}

	public void changeToSlientState() {
		parkingState = PARKINGLOT_STATE.SLIENT;
		movingReservationId = 0;
		Logger.log("parkingState changed to SLIENT.");
	}

	//return assigned slot number, return -1 if fail
	public int getAvailalbeSlotNumber() {
		int slotNumber = -1;
		int slotIdx = 0;
		boolean bSuccess = false;
		for (ParkingSlot slot : slotList) {
			if (slot.getStatus() == ParkingSlot.EMPTY) {
				bSuccess = true;
				break;
			}
			slotIdx++;
		}
		if (bSuccess) {
			slotNumber = slotIdx + 1;
			Logger.log("parkingLotId = " + parkingLotId + ", assigned slotIdx  = " + slotIdx);
		} else {
			slotNumber = -1;
			Logger.log("There is no available slot.");
		}
		return slotNumber;
	}

	//return slot number, return -1 if fail
	public boolean completeParking(int parkedSlotNumber) {
		if (parkingState != PARKINGLOT_STATE.MOVING) {
			Logger.log("State is not normal. parkingState = " + parkingState);
			//			return false;
		}
		boolean result = false;
		try {
			Logger.log("completeParking() movingReservationId = " + movingReservationId);
			ParkingSlot parkedSlot = slotList.get(parkedSlotNumber - 1);
			parkedSlot.setStatus(ParkingSlot.OCCUPIED);
			parkedSlot.setReservationId(movingReservationId);
			changeToSlientState();
			result = true;
		} catch (ArrayIndexOutOfBoundsException e) {
			Logger.log("parkedSlotNumber exceed limit. parkedSlotNumber = " + parkedSlotNumber);
			e.printStackTrace();
		}
		Logger.log("parkingLotId = " + parkingLotId + ", result  = " + result);
		return result;
	}

	public boolean startUnparking(int unparkedSlotNumber) {
		if (parkingState != PARKINGLOT_STATE.SLIENT) {
			Logger.log("State is not normal. parkingState = " + parkingState);
			//			return false;
		}
		boolean result = false;
		try {
			ParkingSlot parkedSlot = slotList.get(unparkedSlotNumber - 1);
			movingReservationId = parkedSlot.getReservationId();
			Logger.log("startUnparking() movingReservationId = " + movingReservationId);
			parkedSlot.setStatus(ParkingSlot.EMPTY);
			parkedSlot.setReservationId(0);
			changeToMovingState();
			result = true;
		} catch (ArrayIndexOutOfBoundsException e) {
			Logger.log("parkedSlotNumber exceed limit. parkedSlotNumber = " + unparkedSlotNumber);
			e.printStackTrace();
		}
		Logger.log("parkingLotId = " + parkingLotId + ", result  = " + result);
		return result;
	}

	public int getRservationIdOfParkingSlot(int slotNumber) {
		int reservationId = -1;
		try {
			ParkingSlot parkedSlot = slotList.get(slotNumber - 1);
			if (parkedSlot.getStatus() == ParkingSlot.OCCUPIED) {
				reservationId = parkedSlot.getReservationId();
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			Logger.log("parkedSlotNumber exceed limit. slotNumber = " + slotNumber);
			e.printStackTrace();
		}
		Logger.log("parkingLotId = " + parkingLotId + ", reservationId  = " + reservationId);
		return reservationId;
	}

	public String getParkingLotId() {
		return parkingLotId;
	}

	public void setParkingLotId(String parkingLotId) {
		this.parkingLotId = parkingLotId;
	}

	public int getTotalSlotNum() {
		if (slotList.size() != totalSlotNum) {
			Logger.log("Warnning : totalSlotNum is not correct" + ", slotStatusMap.size() = "
					+ slotList.size() + ", totalSlotNum = " + totalSlotNum);
			totalSlotNum = slotList.size();
		}
		return totalSlotNum;
	}

	public void setTotalSlotNum(int totalSlotNum) {
		if (slotList.size() != totalSlotNum) {
			Logger.log("Warnning : totalSlotNum is not correct" + ", slotStatusMap.size() = "
					+ slotList.size() + ", totalSlotNum = " + totalSlotNum);
		}
		this.totalSlotNum = totalSlotNum;
	}

	public int getParkingState() {
		return parkingState;
	}

	public void setParkingState(int parkingState) {
		this.parkingState = parkingState;
	}

	public int getMovingReservationId() {
		Logger.log("getMovingReservationId() movingReservationId = " + movingReservationId);
		return movingReservationId;
	}

	public void setMovingReservationId(int movingReservationId) {
		Logger.log("movingReservationId = " + movingReservationId);
		this.movingReservationId = movingReservationId;
	}

	@Override
	public String toString() {
		return "ParkingLotInfo [parkingLotId=" + parkingLotId + ", totalSlotNum=" + totalSlotNum
				+ ", parkingState=" + parkingState + ", movingReservationId="
				+ movingReservationId
				+ ", slotList=" + slotList + "]";
	}

}
