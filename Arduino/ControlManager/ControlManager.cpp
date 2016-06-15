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
static void DoWhen_DriverArriveAtParkingSlot(void);
static void DoWhen_DriverLeaveAtParkingSlot(void);

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

	if( GetStallSensorReady() )
	{

		DoWhen_DriverArriveAtParkingSlot();
		DoWhen_DriverLeaveAtParkingSlot();

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
	}


	
	

} //  LOOP

static void DoWhen_DriverArriveAtEntryGate(void)
{
	EntryGateOpen();
	SetEntryGateLED_Green();
/*
	PARKINGLOT_STATUS
	DRIVER_ARRIVED_ATENTRYGATE,
	DRIVER_LEAVE_ATENTRYGATE,
	DRIVER_ARRIVED_ATEXITGATE,
	DRIVER_LEAVE_ATEXITGATE,
	DRIVERATRIVED_SLOT1,
	DRIVERATRIVED_SLOT2,
	DRIVERATRIVED_SLOT3,
	DRIVERATRIVED_SLOT4,	
	DRIVERLEAVED_SLOT1,
	DRIVERLEAVED_SLOT2,
	DRIVERLEAVED_SLOT3,
	DRIVERLEAVED_SLOT4,	

	CLIENTTOSERVERMSG_MAX
*/	
	
	SetMsgNumber(DRIVER_ARRIVED_ATENTRYGATE);
	SetSendToServer(true);

}

static void DoWhen_DriverLeaveAtEntryGate(void)
{
	EntryGateClose();
	SetEntryGateLED_Red();
/*
	PARKINGLOT_STATUS
	DRIVER_ARRIVED_ATENTRYGATE,
	DRIVER_LEAVE_ATENTRYGATE,
	DRIVER_ARRIVED_ATEXITGATE,
	DRIVER_LEAVE_ATEXITGATE,
	DRIVERATRIVED_SLOT1,
	DRIVERATRIVED_SLOT2,
	DRIVERATRIVED_SLOT3,
	DRIVERATRIVED_SLOT4,	
	DRIVERLEAVED_SLOT1,
	DRIVERLEAVED_SLOT2,
	DRIVERLEAVED_SLOT3,
	DRIVERLEAVED_SLOT4,	

	CLIENTTOSERVERMSG_MAX
*/	
	
	SetMsgNumber(DRIVER_LEAVE_ATENTRYGATE);
	SetSendToServer(true);

}

static void DoWhen_DriverArriveAtExitGate(void)
{
	ExitGateOpen();
	SetExitGateLED_Green();
/*
	PARKINGLOT_STATUS
	DRIVER_ARRIVED_ATENTRYGATE,
	DRIVER_LEAVE_ATENTRYGATE,
	DRIVER_ARRIVED_ATEXITGATE,
	DRIVER_LEAVE_ATEXITGATE,
	DRIVERATRIVED_SLOT1,
	DRIVERATRIVED_SLOT2,
	DRIVERATRIVED_SLOT3,
	DRIVERATRIVED_SLOT4,	
	DRIVERLEAVED_SLOT1,
	DRIVERLEAVED_SLOT2,
	DRIVERLEAVED_SLOT3,
	DRIVERLEAVED_SLOT4,	

	CLIENTTOSERVERMSG_MAX
*/		
	
	SetMsgNumber(DRIVER_ARRIVED_ATEXITGATE);
	SetSendToServer(true);

}

static void DoWhen_DriverLeaveAtExitGate(void)
{
	ExitGateClose();
	SetExitGateLED_Red();
/*
	PARKINGLOT_STATUS
	DRIVER_ARRIVED_ATENTRYGATE,
	DRIVER_LEAVE_ATENTRYGATE,
	DRIVER_ARRIVED_ATEXITGATE,
	DRIVER_LEAVE_ATEXITGATE,
	DRIVERATRIVED_SLOT1,
	DRIVERATRIVED_SLOT2,
	DRIVERATRIVED_SLOT3,
	DRIVERATRIVED_SLOT4,	
	DRIVERLEAVED_SLOT1,
	DRIVERLEAVED_SLOT2,
	DRIVERLEAVED_SLOT3,
	DRIVERLEAVED_SLOT4,	

	CLIENTTOSERVERMSG_MAX
*/		
	SetMsgNumber(DRIVER_LEAVE_ATEXITGATE);
	SetSendToServer(true);

}


static void DoWhen_DriverArriveAtParkingSlot(void)
{
	for( int i=0 ; i<PARKSLOT_MAX ; i++ )
	{
		if( GetStallSensorChanged((T_StallSensorID)i) == true &&  GetStallSensorOccupied((T_StallSensorID)i) == OCCUFIED )
		{
			SetMsgNumber(DRIVERATRIVED_SLOT1+i);
			break;
		}
	}
/*
	PARKINGLOT_STATUS
	DRIVER_ARRIVED_ATENTRYGATE,
	DRIVER_LEAVE_ATENTRYGATE,
	DRIVER_ARRIVED_ATEXITGATE,
	DRIVER_LEAVE_ATEXITGATE,
	DRIVERATRIVED_SLOT1,
	DRIVERATRIVED_SLOT2,
	DRIVERATRIVED_SLOT3,
	DRIVERATRIVED_SLOT4,	
	DRIVERLEAVED_SLOT1,
	DRIVERLEAVED_SLOT2,
	DRIVERLEAVED_SLOT3,
	DRIVERLEAVED_SLOT4,	

	CLIENTTOSERVERMSG_MAX
*/	

//		

	SetSendToServer(true);

}

static void DoWhen_DriverLeaveAtParkingSlot(void)
{
	for( int i=0 ; i<PARKSLOT_MAX ; i++ )
	{
		if( GetStallSensorChanged((T_StallSensorID)i) == true &&  GetStallSensorOccupied((T_StallSensorID)i) == UNOCCUFIED )
		{
			SetMsgNumber(DRIVERLEAVED_SLOT1+i);
			break;
		}
	}

/*
	PARKINGLOT_STATUS
	DRIVER_ARRIVED_ATENTRYGATE,
	DRIVER_LEAVE_ATENTRYGATE,
	DRIVER_ARRIVED_ATEXITGATE,
	DRIVER_LEAVE_ATEXITGATE,
	DRIVERATRIVED_SLOT1,
	DRIVERATRIVED_SLOT2,
	DRIVERATRIVED_SLOT3,
	DRIVERATRIVED_SLOT4,	
	DRIVERLEAVED_SLOT1,
	DRIVERLEAVED_SLOT2,
	DRIVERLEAVED_SLOT3,
	DRIVERLEAVED_SLOT4,	

	CLIENTTOSERVERMSG_MAX
*/	

//		GetStallSensorChanged

	SetSendToServer(true);

}


