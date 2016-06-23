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
#include <string.h>

#include "..\DeviceDriver\LEDDriver.h"
#include "..\DeviceDriver\EntryExitBeamDriver.h"
#include "..\DeviceDriver\GateLiftDriver.h"
#include "..\CommManager\CommManager.h"
#include "ConfigManager.h"



static int incomingByte = 0;   // for incoming serial data
static char s_cmd[0xff];
static int read_cnt = 0;
static int iCmdReady = 0;

static int iRunningMode=0;

static int iStallLed[STALL_LED_MAX]={0};
static int iEntryGateLED=0;
static int iEntryGateServo=0;
static int iExitGateLED=0;
static int iExitGateServo=0;


String cmdMsg[] = {
	"help",
	"connect",
	"disconnect",	
	"print on",
	"print off",	
	"all off",
	"all on",
	"reset",
	"open entry gate",
	"close entry gate",
	"open exit gate",
	"close exit gate",
	"set entry led red",
	"set entry led green",
	"set exit led red",
	"set exit led green"
};									


enum cmdMsgList{
	S_CMD_HELP = 0,
	S_CMD_CONNECT_SERVER,
	S_CMD_DISCONNECT_SERVER,
	S_CMD_PRINT_STATUS_ON,
	S_CMD_PRINT_STATUS_OFF,
	S_CMD_ALL_OFF,
	S_CMD_ALL_ON,
	S_CMD_RESET,
	S_CMD_OPEN_ENTRY_GATE,
	S_CMD_CLOSE_ENTRY_GATE,
	S_CMD_OPEN_EXIT_GATE,
	S_CMD_CLOSE_EXIT_GATE,
	S_CMD_ENTRY_LED_RED,
	S_CMD_ENTRY_LED_GRN,
	S_CMD_EXIT_LED_RED,
	S_CMD_EXIT_LED_GRN,
	S_CMD_MAX
};

static String ParkingLot_ID;
static String ParkingLot_PWD;



void SeiralCommand(String sCmd)
{
	int iCmdIndex;
	
	for( iCmdIndex=0 ; iCmdIndex<S_CMD_MAX ; iCmdIndex++ )
	{
		if( sCmd.equalsIgnoreCase(cmdMsg[iCmdIndex]) == true ) break;
	}

	switch( iCmdIndex )
	{
		case S_CMD_HELP : 
			Serial.println();
			Serial.println("----------------------------------------------------------");
			for( int i=0 ; i<S_CMD_MAX ; i++ )
			{
				Serial.print(i);
				Serial.print(". ");
				Serial.println(cmdMsg[i]);
			}
			Serial.println();
			Serial.println("-. turn on/off led:number");
			Serial.println("-. set id:id");
			Serial.println("-. set pwd:password");
			Serial.println("----------------------------------------------------------");
			break;

		case S_CMD_CONNECT_SERVER :
			AttempToConnectServer();
			SetServerConnectAuto(true);
			break;
			
		case S_CMD_DISCONNECT_SERVER :
			AttempToDisconnectServer();
			SetServerConnectAuto(false);
			break;
			
		case S_CMD_PRINT_STATUS_ON :
			SetRunningMode(GetRunningMode() | 1<<RUN_PRINT_MODE );
			Serial.println(GetRunningMode());
			break;

		case S_CMD_PRINT_STATUS_OFF :
			SetRunningMode(GetRunningMode() & ~(1<<RUN_PRINT_MODE));
			Serial.println(GetRunningMode());
			break;
			
		case S_CMD_ALL_OFF :
			EntryGateClose();
			ExitGateClose();
			SetEntryGateLED_Red();
			SetExitGateLED_Red();
			for( int i=0 ; i<STALL_LED_MAX ; i++ ) SetParkingStallLED(i, OFF);
			break;

		case S_CMD_ALL_ON :
			EntryGateOpen();
			ExitGateOpen();
			SetEntryGateLED_Green();
			SetExitGateLED_Green();
			for( int i=0 ; i<STALL_LED_MAX ; i++ ) SetParkingStallLED(i, ON);
			break;
			
		case S_CMD_RESET :
			ClrTimeStamp();
			ParkingLotReset();
			break;
			
		case S_CMD_OPEN_ENTRY_GATE :
			EntryGateOpen();
			break;

		case S_CMD_CLOSE_ENTRY_GATE :
			EntryGateClose();
			break;

		case S_CMD_OPEN_EXIT_GATE :
			ExitGateOpen();
			break;

		case S_CMD_CLOSE_EXIT_GATE :
			ExitGateClose();
			break;

		case S_CMD_ENTRY_LED_RED :
			SetEntryGateLED_Red();
			break;

		case S_CMD_ENTRY_LED_GRN :
			SetEntryGateLED_Green();
			break;

		case S_CMD_EXIT_LED_RED :
			SetExitGateLED_Red();
			break;

		case S_CMD_EXIT_LED_GRN :
			SetExitGateLED_Green();
			break;

		default :

			char deli[]={":"};
			char *nk;
			int iSlot;
			String cmdtok = String(strtok_r(s_cmd, deli, &nk));
			String partok = String(strtok_r(NULL, deli, &nk));
			iSlot = atoi((const char*)partok.c_str());
			
			if( cmdtok.equalsIgnoreCase("set id") ) Set_ID(partok);
			if( cmdtok.equalsIgnoreCase("set pwd") ) Set_PWD(partok);
			if( cmdtok.equalsIgnoreCase("turn on led") )
			{
				if( iSlot == 0 )
				{
					for( int i=0 ; i<STALL_LED_MAX ; i++ ) SetParkingStallLED(i, ON);
				}
				else if( iSlot > 0 && iSlot < STALL_LED_MAX )
				{
					SetParkingStallLED(iSlot, ON);
				}
			}

			if( cmdtok.equalsIgnoreCase("turn off led") )
			{
				if( iSlot == 0 )
				{
					for( int i=0 ; i<STALL_LED_MAX ; i++ ) SetParkingStallLED(i, OFF);
				}
				else if( iSlot > 0 && iSlot < STALL_LED_MAX )
				{
					SetParkingStallLED(iSlot, OFF);
				}
			}
			break;
	}

	Serial.println();
	Serial.print("CMD(");
	Serial.print(iCmdIndex);
	Serial.print("):");
	Serial.println(sCmd);

}

void ParkingLotReset(void)
{
	EntryGateClose();
	ExitGateClose();
	SetEntryGateLED_Red();
	SetExitGateLED_Red();
	for( int i=0 ; i<STALL_LED_MAX ; i++ ) SetParkingStallLED(i, OFF);
}


void ConfigManagerSetup(void)  
{
	SetRunningMode(0);
	Set_ID(__ID);
	Set_PWD(__PWD);
}

void ConfigManagerLoop(void) 
{
	String cmd;

	char c = ' ';
	while( Serial.available() )
	{
		c = Serial.read();
		if( c != '\n' ) s_cmd[read_cnt++] = c;
		else
		{
			s_cmd[read_cnt] = '\0';
			cmd = String(s_cmd);
			iCmdReady = true;
		}
	}

	if( iCmdReady == true )
	{
		SeiralCommand(cmd);
		iCmdReady = false;
		memset(s_cmd, 0, read_cnt);
		read_cnt = 0;		
	}
} //  LOOP

void SetRunningMode(int iMode)
{
	iRunningMode = iMode;
}

int GetRunningMode(void)
{
	return iRunningMode;
}

String Get_ID(void)
{
	return ParkingLot_ID;
}

String Get_PWD(void)
{
	return ParkingLot_PWD;
}

void Set_ID(String id)
{
	ParkingLot_ID = id;
}

void Set_PWD(String pwd)
{
	ParkingLot_PWD = pwd;
}



