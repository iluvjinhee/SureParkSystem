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

#ifndef _CONTROLMANAGER_H_
#define _CONTROLMANAGER_H_

#define NOT	(-1)

void ControlManagerSetup();
void ControlManagerLoop();
int GetChangedSlot(void);
int ClrChangedSlot(void);

int GetRequestedLed(void);
int SetRequestedLed(int iSlot);
int ClrRequestedLed(void);

#endif // _CONTROLMANAGER_H_

/* End of File */



