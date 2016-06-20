/****************************************************************
* File: GateLiftTest
* Project: LG Exec Ed Program
* Copyright: Copyright (c) 2016 Anthony J. Lattanze
* Versions:
* 1.0 May 2016 - Initial version
*
* Description:
*
* This program lifts and closes the entry and exit gates. Note that the servos are positional servos. 
* So the 0 position is closed. The 90 position is open. The other positions should be avoided for this
* project.
*
* Parameters: None
*
* Internal Methods: 
*
* void InitEntryExitLEDs() - Initializes the entry and exit LEDs
* to ensure they are off. 
***************************************************************/

#ifndef _GATELIFTDRIVER_H_
#define _GATELIFTDRIVER_H_

#define Open  90
#define Close 0

void GateLiftSetup(void);
void EntryGateClose(void);
void EntryGateOpen(void);
void ExitGateClose(void);
void ExitGateOpen(void);

int GetEntryGateServo(void);
int GetExitGateServo(void);

#endif // _GATELIFTDRIVER_H_

/* End of File */



