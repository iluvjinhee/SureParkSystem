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

#define ON	1
#define OFF	0


#define GRN 1
#define RED	0

typedef enum
{
	STALL_LED_001 = 0,	///< Stall LED 1
	STALL_LED_002,			///< Stall LED 2
	STALL_LED_003,			///< Stall LED 3
	STALL_LED_004,			///< Stall LED 4
	STALL_LED_MAX				///< Stall LED Max
}T_StallLedID;


void LEDSetup();
void LEDLoop();
void SetEntryGateLED_Red(void);
void SetEntryGateLED_Green(void);
void SetExitGateLED_Red(void);
void SetExitGateLED_Green(void);

int GetEntryGateLED(void);
int GetExitGateLED(void);

void SetParkingStallLED(int iIndex, bool bOnOff);
int GetParkingStallLED(int iIndex);


#endif // _LEDDRIVER_H_

/* End of File */





