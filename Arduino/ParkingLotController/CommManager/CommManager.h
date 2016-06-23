/****************************************************************
* File: EntryExitBeamTest
* Project: LG Exec Ed Program
* Copyright: Copyright (c) 2016 Anthony J. Lattanze
* Versions:
* 1.0 May 2016 - Initial version
*
* Description:
*
* This program tests the IR beam used at the entry and exit to detect when a car
* is entering or exiting the parking lot. It simply reads each sensor every 
* second and indicates whether the beam is broken (obstructed) or not.
*
* Parameters: None
*
* Internal Methods: 
*
* void InitEntryExitLEDs() - Initializes the entry and exit LEDs
* to ensure they are off. 
***************************************************************/

#ifndef _COMMMANAGER_H_
#define _COMMMANAGER_H_


#define __ID	"SP001"
#define __PWD	"SP001"

#define __CRYPTION_KEY	"SP1234567890"


#define __STR_MESSAGETYPE		"messagetype"
#define __STR_ID						"id"
#define __STR_PASSWORD			"pwd"
#define __STR_COMMAND			"command"
#define __STR_STATUS			"status"
#define __STR_1					"1"
#define __STR_0					"0"

#define __STR_OK				"ok"
#define __STR_NOK				"nok"

#define __STR_UP				"up"
#define __STR_DOWN				"down"

#define __STR_RED				"red"
#define __STR_GRN				"green"

#define __STR_OCCUFIED			"occupied"
#define __STR_EMPTY				"empty"


#define __STR_ON				"on"
#define __STR_OFF				"off"

#define __STR_RESULT			"result"
#define __STR_OK				"ok"
#define __STR_NOK				"nok"

#define __STR_SLOT_COUNT		"slot_count"
#define __STR_SLOTSTATUS		"slot_status"
#define __STR_LEDSTATUS			"led_status"
#define __STR_SLOT_NUMBER		"slot_number"

#define __STR_ENTRYGATE			"entrygate"
#define __STR_EXITGATE			"exitgate"
#define __STR_ENTRY_LED			"entrygateled"
#define __STR_EXIT_LED			"exitgateled"
#define __STR_ENTRYGATE_ARRIVE	"entrygate_arrive"
#define __STR_EXITGATE_ARRIVE	"exitgate_arrive"

#define __STR_TIMESTAMP			"timestamp"


enum ClientToServerMsg{
	CS_Authentication_Request = 0,
	CS_Parkinglot_Information,
	CS_Parkingslot_Sensor,
	CS_Parkingslot_LED,
	CS_EntryGate_Servo,
	CS_ExitGate_Servo,
	CS_EntryGate_LED,
	CS_ExitGate_LED,
	CS_EntryGate_Arrive,
	CS_EntryGate_PassBy,
	CS_ExitGate_Arrive,
	CS_ExitGate_PassBy,
	CS_HeartBeat,
	CS_SERVERTOCLIENTMSG_MAX
};

enum ServerToClientMsg  {
	SC_Authentication_Response = 0,
	SC_ParkInfo_Request,	
	SC_EntryGate_Control,
	SC_ExitGate_Control,
	SC_EntryGate_LED_Control,
	SC_ExitGate_LED_Control,
	SC_Parkingslot_LED_Control,
	SC_Parkingslot_Reset,
	SC_Ack,
	SC_CLIENTTOSERVERMSG_MAX
};


void SetMsgNumber(int iMsgIndex);
void SetSendToServer(int iOnOff);
int GetSendToServer(void);
int GetTimeStamp(void);
void ClrTimeStamp(void);

int GetServerConnectAuto(void);
void SetServerConnectAuto(int iStatus);
void AttempToConnectServer(void);
void AttempToDisconnectServer(void);

void CommManagerSetup();
void CommManagerLoop();

String Encrypt(const char * msg);
String Decrypt(const char * msg);

#endif // _COMMMANAGER_H_

/* End of File */



