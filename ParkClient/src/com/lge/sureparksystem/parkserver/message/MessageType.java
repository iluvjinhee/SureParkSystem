
package com.lge.sureparksystem.parkserver.message;

public enum MessageType {
    ACKNOWLEDGE("ACK"),
    AUTHENTICATION_OK("AUTHENTICATION OK"),
    AUTHENTICATION_FAIL("AUTHENTICATION FAIL"),

    // Login
    AUTHENTICATION_REQUEST("Authentication_Request"),
    AUTHENTICATION_RESPONSE("Authentication_Response"),
    
    CREATE_DRVIER_REQUEST("Create_Driver"),
    CREATE_DRVIER_RESOPNSE("Response"),
    
    // Driver
    RESERVATION_INFO_REQUEST("ReservationInfo_Request"),
    RESERVATION_INFORMATION_RESPONSE("Reservation_Information"),

    PARKINGLOTINFO_REQUEST("ParkinglotInfo_Request"),
    PARKINGLOT_LIST_RESPONSE("Parkinglot_list"),

    RESERVATION_REQUEST("Reservation_Request"), 
    // return RESERVATION_INFORMATION("Reservation_Information"),

    CANCEL_REQUEST("Cancel_Request"),

    // Attendant
    PARKING_LOT_STATUS_REQUEST("ParkinglotStatus_Request"),
    PARKING_LOT_STATUS("ParkinglotStatus"),

    NOTIFICATION("Notification"),
    
    // Owner
    PARKINGLOT_STATS_REQUEST("Parkinglot_stats_Request"),
    PARKINGLOT_STATISTICS("Parkinglot_Statistics"),
    CHANGE_RESPONSE("Change_Response"),
    CHANGE_PARKINGFEE("Change_parkingfee"),
    CHANGE_GRACEPERIOD("Change_graceperiod"),
    CREATE_ATTENDANT("Create_Attendant"),
    
    NONE("NONE");

    private String text;

    MessageType(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static MessageType fromText(String text) {
        if (text != null) {
            for (MessageType b : MessageType.values()) {
                if (text.equalsIgnoreCase(b.text)) {
                    return b;
                }
            }
        }
        return null;
    }
}