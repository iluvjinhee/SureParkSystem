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

#ifndef _CONFIGMANAGER_H_
#define _CONFIGMANAGER_H_


enum ParkingLotRunningMode  {
	RUN_NORMAL = 0,
	RUN_PRINT_MODE,
	RUN_MODE_MAX
};


void ConfigManagerSetup(void);
void ConfigManagerLoop(void);

void SetRunningMode(int iMode);
int GetRunningMode(void);

void ParkingLotReset(void);

String Get_ID(void);
String Get_PWD(void);

void Set_ID(String id);
void Set_PWD(String pwd);


#endif // _CONFIGMANAGER_H_

/* End of File */



