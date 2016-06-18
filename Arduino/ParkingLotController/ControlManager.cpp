/**************************************************************
* File: ClientDemo
* Project: LG Exec Ed Program
* Copyright: Copyright (c) 2013 Anthony J. Lattanze
* Versions:
* 1.0 May 2013.
* 1.5 April 2014 - added #define for port id
*
* Description:
*
* This program runs on an Arduino processor with a WIFI shield. 
* This program is a client demo. This runs in a loop communicating
* with a server process that also runs in a loop. The protocol is
* that after we connect to a server, this process sends three '\n'
* terminated strings. The final string is "bye\n" which is a 
* signal to the server that we are done sending data. Then
* this process waits for the server to send a single string back. This illustrates basic client
* server connection and two-way communication.
*
* Compilation and Execution Instructions: Only Compile using 
* Arduino IDE VERSION 1.0.4
*
* Parameters: None
*
* Internal Methods: void printConnectionStatus()
*
************************************************************************************************/

#include <SPI.h>

#include "ControlManager.h"
#include "CommManager.h"

#include "SensorManager.h"
#include "LEDDriver.h"
#include "EntryExitBeamDriver.h"
#include "GateLiftDriver.h"



static void DoWhen_DriverArriveAtEntryGate(void);
static void DoWhen_DriverLeaveAtEntryGate(void);
static void DoWhen_DriverArriveAtExitGate(void);
static void DoWhen_DriverLeaveAtExitGate(void);

void ControlManagerSetup()  
{
	LEDSetup();
	
	GateLiftSetup();
					   
	SensorManagerSetup();  
}

void ControlManagerLoop() 
{
	bool bOnOff;
	
	SensorManagerLoop();

	SetParkingStallLED(PARKSLOT_001, CheckParkSlotOccupied(PARKSLOT_001));
	SetParkingStallLED(PARKSLOT_002, CheckParkSlotOccupied(PARKSLOT_002));
	SetParkingStallLED(PARKSLOT_003, CheckParkSlotOccupied(PARKSLOT_003));
	SetParkingStallLED(PARKSLOT_004, CheckParkSlotOccupied(PARKSLOT_004));

	if( DriverArriveAtEntryGate() == true )
	{
		DoWhen_DriverArriveAtEntryGate();
	}
	else
	{
		;
	}

	if( DriverLeaveAtEntryGate() == true )
	{
		DoWhen_DriverLeaveAtEntryGate();
	}
	else
	{
		;
	}

	if( DriverArriveAtExitGate() == true )
	{
		DoWhen_DriverArriveAtExitGate();
	}
	else
	{
		;
	}

	if( DriverLeaveAtExitGate() == true )
	{
		DoWhen_DriverLeaveAtExitGate();
	}
	else
	{
		;
	}

	
	

} //  LOOP

static void DoWhen_DriverArriveAtEntryGate(void)
{
	EntryGateOpen();
	SetEntryGateLED_Green();
}

static void DoWhen_DriverLeaveAtEntryGate(void)
{
	EntryGateClose();
	SetEntryGateLED_Red();
}

static void DoWhen_DriverArriveAtExitGate(void)
{
	ExitGateOpen();
	SetExitGateLED_Green();
}

static void DoWhen_DriverLeaveAtExitGate(void)
{
	ExitGateClose();
	SetExitGateLED_Red();
}





