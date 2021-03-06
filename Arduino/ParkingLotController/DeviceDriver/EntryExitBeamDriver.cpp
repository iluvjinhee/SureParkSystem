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
#include <SPI.h>
#include "EntryExitBeamDriver.h"

#define CHATTERING_CNT	0xff

#define EntryBeamRcvr  34 
#define ExitBeamRcvr   35


static int EntryBeamState;
static int ExitBeamState;

        
void EntryExitBeamSetup() 
{
	pinMode(ExitBeamRcvr, INPUT);      // Make exit IR rcvr an input
	digitalWrite(ExitBeamRcvr, HIGH);  // enable the built-in pullup

	pinMode(EntryBeamRcvr, INPUT);     // Make entry IR rcvr an input
	digitalWrite(EntryBeamRcvr, HIGH); // enable the built-in pullup


	EntryBeamState = NOTBROKEN;
	ExitBeamState = NOTBROKEN;
}

void EntryExitBeamLoop()
{
	int EntryBeamInput;
	static int EntryBeamBuff;
	static int EntryBeamChatCnt=0;

	int ExitBeamInput;
	static int ExitBeamBuff;
	static int ExitBeamChatCnt=0;


	EntryBeamInput = digitalRead(EntryBeamRcvr);  // Here we read the state of the
	                                            // entry beam.
	// Exit Gate
	ExitBeamInput = digitalRead(ExitBeamRcvr);  // Here we read the state of the
	                                          // exit beam.  


	// Entry Gate
	if (EntryBeamInput != EntryBeamBuff)  //
	{   
		EntryBeamChatCnt = 0;
	}
	else
	{
		EntryBeamChatCnt++;
	}

	EntryBeamBuff = EntryBeamInput;

	if( EntryBeamChatCnt > CHATTERING_CNT )
	{
		if( EntryBeamInput == LOW )
		{
			EntryBeamState = BROKEN;
		}
		else
		{
			EntryBeamState = NOTBROKEN;
		}
		EntryBeamChatCnt = 0;
	}
	else
	{
		;
	}


	if (ExitBeamInput != ExitBeamBuff)
	{
		ExitBeamChatCnt = 0;
	}
	else
	{
		ExitBeamChatCnt++;
	}

	ExitBeamBuff = ExitBeamInput;


	if( ExitBeamChatCnt > CHATTERING_CNT )
	{
		if( ExitBeamInput == LOW )
		{
			ExitBeamState = BROKEN;
		}
		else
		{
			ExitBeamState = NOTBROKEN;
		}
		ExitBeamChatCnt = 0;
	}
	else
	{
		;
	}
}


int GetEntryBeamStatus()
{
	return EntryBeamState;
}

int GetExitBeamStatus()
{
	return ExitBeamState;
}




