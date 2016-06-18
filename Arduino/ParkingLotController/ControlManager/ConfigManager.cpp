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

static int incomingByte = 0;   // for incoming serial data

void ConfigManagerSetup(void)  
{

}

void ConfigManagerLoop(void) 
{
	static char s_cmd[0xff];
	static int read_cnt = 0;
	static int iCmdReady = 0;
	
	String cmd;

	char c = ' ';
	while( Serial.available() )
	{
		c = Serial.read();
		if( c != '\n' ) s_cmd[read_cnt++] = c;
		else
		{
			s_cmd[read_cnt] = '\n';
			cmd = String(s_cmd);
			iCmdReady = true;
		}
	}

	if( iCmdReady == true )
	{
		Serial.println();
        Serial.print("I received: ");
		Serial.println(cmd);

		iCmdReady = false;
		read_cnt = 0;
		s_cmd[0] = '\n';
		cmd = "";
	}
} //  LOOP




