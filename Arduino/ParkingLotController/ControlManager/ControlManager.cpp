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
#include "..\CommManager\CommManager.h"

#include "SensorManager.h"
#include "..\DeviceDriver\LEDDriver.h"
#include "..\DeviceDriver\EntryExitBeamDriver.h"
#include "..\DeviceDriver\GateLiftDriver.h"




static void DoWhen_DriverArriveAtEntryGate(void);
static void DoWhen_DriverLeaveAtEntryGate(void);
static void DoWhen_DriverArriveAtExitGate(void);
static void DoWhen_DriverLeaveAtExitGate(void);
static void DoWhen_DriverArriveAtParkingSlot(int iSlot);
static void DoWhen_DriverLeaveAtParkingSlot(int iSlot);
static void SetChangedSlot(int iSlot);

static int iChangedSlot;
static int iRequestedLed;

void ControlManagerSetup()  
{
	LEDSetup();
	
	GateLiftSetup();
					   
	SensorManagerSetup();  

	ClrChangedSlot();
	ClrRequestedLed();
}

void ControlManagerLoop() 
{
	bool bOnOff;
	static int iSlot=0;

	SensorManagerLoop();

	if( GetStallSensorReady() )
	{
//		SetParkingStallLED(PARKSLOT_001, !CheckParkSlotOccupied(PARKSLOT_001));
//		SetParkingStallLED(PARKSLOT_002, !CheckParkSlotOccupied(PARKSLOT_002));
//		SetParkingStallLED(PARKSLOT_003, !CheckParkSlotOccupied(PARKSLOT_003));
//		SetParkingStallLED(PARKSLOT_004, !CheckParkSlotOccupied(PARKSLOT_004));

		if( iSlot >= PARKSLOT_MAX )
		{
			iSlot = 0;
		}
		else
		{
			DoWhen_DriverArriveAtParkingSlot(iSlot);
			DoWhen_DriverLeaveAtParkingSlot(iSlot);
			iSlot++;
		}

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
	}
} //  LOOP

static void DoWhen_DriverArriveAtEntryGate(void)
{
//	EntryGateOpen();
//	SetEntryGateLED_Green();

	SetMsgNumber(CS_EntryGate_Arrive);
	SetSendToServer(true);
}

static void DoWhen_DriverLeaveAtEntryGate(void)
{
//	EntryGateClose();
//	SetEntryGateLED_Red();

	SetMsgNumber(CS_EntryGate_PassBy);
	SetSendToServer(true);

}

static void DoWhen_DriverArriveAtExitGate(void)
{
//	ExitGateOpen();
//	SetExitGateLED_Green();

	SetMsgNumber(CS_ExitGate_Arrive);
	SetSendToServer(true);

}

static void DoWhen_DriverLeaveAtExitGate(void)
{
//	ExitGateClose();
//	SetExitGateLED_Red();

	SetMsgNumber(CS_ExitGate_PassBy);
	SetSendToServer(true);

}


static void DoWhen_DriverArriveAtParkingSlot(int iSlot)
{
	if( GetStallSensorChanged((T_StallSensorID)iSlot) == true &&  GetStallSensorOccupied((T_StallSensorID)iSlot) == OCCUFIED )
	{
		SetStallSensorChanged((T_StallSensorID)iSlot, false);
		SetMsgNumber(CS_Parkingslot_Sensor);
		SetChangedSlot(iSlot);
		SetSendToServer(true);
	}
}

static void DoWhen_DriverLeaveAtParkingSlot(int iSlot)
{
	if( GetStallSensorChanged((T_StallSensorID)iSlot) == true &&  GetStallSensorOccupied((T_StallSensorID)iSlot) == UNOCCUFIED )
	{
		SetStallSensorChanged((T_StallSensorID)iSlot, false);
		SetMsgNumber(CS_Parkingslot_Sensor);
		SetChangedSlot(iSlot);
		SetSendToServer(true);
	}
}

static void SetChangedSlot(int iSlot)
{
	iChangedSlot = iSlot;
}

int GetChangedSlot(void)
{
	return iChangedSlot;
}

int ClrChangedSlot(void)
{
	iChangedSlot = NOT;
}


int SetRequestedLed(int iSlot)
{
	iRequestedLed = iSlot;
}

int GetRequestedLed(void)
{
	return iRequestedLed;
}

int ClrRequestedLed(void)
{
	iRequestedLed = NOT;
}



