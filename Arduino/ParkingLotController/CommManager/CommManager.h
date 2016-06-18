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
	SC_EntryGate_LED,
	SC_ExitGate_LED,
	SC_Parkingslot_LED,
	SC_Ack,
	SC_CLIENTTOSERVERMSG_MAX
};


void SetMsgNumber(int iMsgIndex);
void SetSendToServer(int iOnOff);
int GetSendToServer(void);

int GetServerConnected(void);

void CommManagerSetup();
void CommManagerLoop();

#endif // _COMMMANAGER_H_

/* End of File */



