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
#include "SensorManager.h"

static bool bEnterDriver;
static bool bExitDriver;

void SensorManagerSetup(void)  
{
	ParkingStallSensorSetup();
	EntryExitBeamSetup();
}

void SensorManagerLoop(void) 
{
	ParkingStallSensorLoop();
	EntryExitBeamLoop();
} //  LOOP



int CheckParkSlotOccupied(T_ParkingSlotID t_ParkSlotID)
{
	if( t_ParkSlotID >= PARKSLOT_MAX ) return -1;
	else return GetStallSensorOccupied((T_StallSensorID)t_ParkSlotID);
}

int DriverArriveAtEntryGate(void)
{
	int iDriverArrived=0;
	int iGateState;
	static int iPrevGateState;
	
	iGateState = GetEntryGateStatus();

	if( iGateState == BROKEN && iPrevGateState == NOTBROKEN ) 
	{
		iDriverArrived = 1;
	}

	iPrevGateState = iGateState;
	
	return iDriverArrived;
}

int DriverLeaveAtEntryGate(void)
{
	int iDriverLeave=0;
	int iGateState;
	static int iPrevGateState;
	
	iGateState = GetEntryGateStatus();

	if( iGateState == NOTBROKEN && iPrevGateState == BROKEN ) 
	{
		iDriverLeave = 1;
	}

	iPrevGateState = iGateState;
	
	return iDriverLeave;
}


int DriverArriveAtExitGate(void)
{
	int iDriverArrived=0;
	int iGateState;
	static int iPrevGateState;
	
	iGateState = GetExitGateStatus();

	if( iGateState == BROKEN && iPrevGateState == NOTBROKEN ) 
	{
		iDriverArrived = 1;
	}

	iPrevGateState = iGateState;
	
	return iDriverArrived;
}

int DriverLeaveAtExitGate(void)
{
	int iDriverLeave=0;
	int iGateState;
	static int iPrevGateState;
	
	iGateState = GetExitGateStatus();

	if( iGateState == NOTBROKEN && iPrevGateState == BROKEN ) 
	{
		iDriverLeave = 1;
	}

	iPrevGateState = iGateState;
	
	return iDriverLeave;
}




