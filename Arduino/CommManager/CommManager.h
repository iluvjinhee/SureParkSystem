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

enum ServerToClientMsg {
	OPEN_ENTRY_GATE = 0,
	OPEN_EXIT_GATE,
	CLOSE_ENTRY_GATE,
	CLOSE_EXIT_GATE,
	SET_ENTRY_GATE_LED_GREEN,
	SET_ENTRY_GATE_LED_RED,
	SET_EXIT_GATE_LED_GREEN,
	SET_EXIT_GATE_LED_RED,
	SET_SLOT_LED_ON,
	SET_SLOT_LED_OFF,
	SERVERTOCLIENTMSG_MAX
};

enum ClientToServerMsg {
	PARKINGLOT_STATUS = 0,
	DRIVER_ARRIVED_ATENTRYGATE,
	DRIVER_LEAVE_ATENTRYGATE,
	DRIVER_ARRIVED_ATEXITGATE,
	DRIVER_LEAVE_ATEXITGATE,
	DRIVERATRIVED_SLOT1,
	DRIVERATRIVED_SLOT2,
	DRIVERATRIVED_SLOT3,
	DRIVERATRIVED_SLOT4,	
	DRIVERLEAVED_SLOT1,
	DRIVERLEAVED_SLOT2,
	DRIVERLEAVED_SLOT3,
	DRIVERLEAVED_SLOT4,	
	CLIENTTOSERVERMSG_MAX
};


void SetMsgNumber(int iMsgIndex);
void SetSendToServer(int iOnOff);
int GetSendToServer(void);

void CommManagerSetup();
void CommManagerLoop();

#endif // _COMMMANAGER_H_

/* End of File */



