package com.lge.sureparksystem.parkserver.manager.reservationmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.lge.sureparksystem.parkserver.util.Logger;

public class ParkingLotStatus {
    private String parkingLotId;
    private int totalSlotNum;
    private HashMap<Integer, ParkingSlot> slotStatusMap = new HashMap<Integer, ParkingSlot>();
    private ArrayList<ParkingSlot> slotList = new ArrayList<ParkingSlot>();

    public ParkingLotStatus() {
    }

    public ParkingLotStatus(String parkingLotId, ArrayList<String> slotStatusList) {
        this.parkingLotId = parkingLotId;
        Integer slotIndex = 0;
        for (String slotSatus : slotStatusList) {
        	slotList.add(new ParkingSlot(slotIndex, Integer.valueOf(slotSatus)));
        	slotIndex++;
        }
        this.totalSlotNum = slotList.size();
    }

    public ParkingLotStatus(String parkingLotId, int totalSlotNum,
            ArrayList<String> slotStatusList) {
        this.parkingLotId = parkingLotId;
        this.totalSlotNum = totalSlotNum;
        Integer slotIndex = 0;
        for (String slotSatus : slotStatusList) {
            slotList.add(new ParkingSlot(slotIndex, Integer.valueOf(slotSatus)));
            slotIndex++;
        }
    }

    public int getOccupiedSlotCount() {
        int occupiedSlotCnt = 0;
        Iterator<Integer> iterator = slotStatusMap.keySet().iterator();
        while (iterator.hasNext()) {
            Integer slotIdx = iterator.next();
            if (slotStatusMap.get(slotIdx).getStatus() == ParkingSlot.OCCUPIED) {
                occupiedSlotCnt++;
            }
        }
        Logger.log("parkingLotId = " + parkingLotId + ", occupiedSlot count = " + occupiedSlotCnt);
        return occupiedSlotCnt;
    }

    public int getAvailableSlotCount() {
        int availableSlotNum = 4;
//        Iterator<Integer> iterator = slotStatusMap.keySet().iterator();
//        while (iterator.hasNext()) {
//            Integer slotIdx = iterator.next();
//            if (slotStatusMap.get(slotIdx).getStatus() == ParkingSlot.EMPTY) {
//                availableSlotNum++;
//            }
//        }
//        Logger.log("parkingLotId = " + parkingLotId + ", availableSlotNum count = "
//                + availableSlotNum);
        return availableSlotNum;
    }

    public int getAvailableSlot() {
        
        return -1;
    }
    
    public boolean setSlot(int slotNum, String driver_id) {
        
        return false;
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


}
