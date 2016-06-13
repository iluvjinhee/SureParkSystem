/****************************************************************
* File: LEDTest
* Project: LG Exec Ed Program
* Copyright: Copyright (c) 2016 Anthony J. Lattanze
* Versions:
* 1.0 May 2016 - Initial version
*
* Description:
*
* This program simply turns on and off all the LEDs on the parking
* lot game board, Note that the entry and exit LEDs can be both
* green and red (blue as well, but the blue pin bas been cliped).
*
* Parameters: None
*
* Internal Methods: None
***************************************************************/

#ifndef _LEDDRIVER_H_
#define _LEDDRIVER_H_

void LEDSetup() ;
void SetEntryGateLED_Red(void);
void SetEntryGateLED_Green(void);
void SetExitGateLED_Red(void);
void SetExitGateLED_Green(void);
void SetParkingStallLED(int iIndex, bool bOnOff);

#endif // _LEDDRIVER_H_

/* End of File */





