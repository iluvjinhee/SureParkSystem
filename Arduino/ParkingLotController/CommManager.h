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

#include <SPI.h>
#include <WiFi.h>

void SetSendToServer(int iOnOff);
int GetSendToServer(void);

void CommManagerSetup();
void CommManagerLoop();

#endif // _COMMMANAGER_H_

/* End of File */



