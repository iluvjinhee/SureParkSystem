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
#include "Timer.h"

#include "CommManager.h"
#include "ControlManager.h"

//debug
#include "ParkingStallSensorDriver.h"

static void printConnectionStatus();

static void TimerCallBack(void){SetSendToServer(true); __debug_print_stallsensor();}

#define PORTID  550               // IP socket port ID

char ssid[] = "LGArchi";              // The network SSID for CMU unsecure network
char c;                           // Character read from server
int status = WL_IDLE_STATUS;      // Network connection status
IPAddress server(192,168,1,6);  // The server's IP address
WiFiClient client;                // The client (our) socket
IPAddress ip;                     // The IP address of the shield
IPAddress subnet;                 // The IP address of the shield
long rssi;                        // Wifi shield signal strength
byte mac[6];                      // Wifi shield MAC address


static int iSendToServer=false;
Timer myTimer;

String serverMsg[] = {
	"OpenEntryGate\n",
	"OpenExitGate\n",
	"CloseEntryGate\n",
	"OpenExitGate\n",
	"SetEntryGateLED_Green\n",
	"SetEntryGateLED_Red\n",
	"SetExitGateLED_Green\n",
	"SetExitGateLED_Red\n",
	"SetSlotLedOn\n",
	"SetSlotLedOff\n"
};										//

String inputLine;	// Data from server
char readBuff[0xff];


enum ServerToClientMsg {
	OPEN_ENTRY_GATE = 0,
	OPEN_EXIT_GATE,
	CLOSE_ENTRY_GATE,
	CLOSE_EXIT_GATE,
	SET_ENTRY_GATE_LED_GREEN,
	SET_ENTRY_GATE_LED_RED,
	SET_EXIT_GATE_LED_GREEN,
	SET_EXIT_GATE_LED_RED,
	SET_SLOT_LED_ON,
	SET_SLOT_LED_OFF,
	SERVERTOCLIENTMSG_MAX
};

/*
char *clientMsg[] = {
	"OpenEntryGate\n",
	"OpenExitGate\n",
	"CloseEntryGate\n",
	"OpenExitGate\n",
	"SetEntryGateLED_Green\n",
	"SetEntryGateLED_Red\n",
	"SetExitGateLED_Green\n",
	"SetExitGateLED_Red\n",
	"SetSlotLedOn\n",
	"SetSlotLedOff\n",
};										//
*/

void CommManagerSetup()  
{
	Serial.println("Attempting to connect to network...");
	Serial.print("SSID: ");
	Serial.println(ssid);

	// Attempt to connect to Wifi network.
	while ( status != WL_CONNECTED) 
	{ 
		Serial.print("Attempting to connect to SSID: ");
		Serial.println(ssid);
		status = WiFi.begin(ssid);
	}  

	Serial.println( "Connected to network:" );
	Serial.println( "\n----------------------------------------" );

	// Print the basic connection and network information.
	printConnectionStatus();

	Serial.println( "\n----------------------------------------\n" );

	// Attempt to connect to server
	while( !client.connect(server, PORTID) )
	{
		Serial.println("Attempt to connect to server..");
	}
    
    Serial.println("Connected.");
    Serial.println();


	myTimer.every(2000, TimerCallBack);

}

void CommManagerLoop() 
{
	myTimer.update();
	
	if( iSendToServer )
	{
		// We write a couple of messages to the server

		client.println(serverMsg[0]);
		// Now read a message from the server

		char c = ' ';  
		while ( c!= '\n' )
		{
			if( client.available() )
			{
			  c = client.read();
			  inputLine += c;
			}
			else break;
		}

		Serial.println("From Server : ");
		Serial.println(inputLine);
		Serial.println();
		SetSendToServer(false);

		if (!client.connected())
		{
		    Serial.println();
		    Serial.println("disconnecting.");
		    client.stop();
		    for(;;)
		      ;
		}
	}
} //  LOOP

void SetSendToServer(int iOnOff)
{
	iSendToServer = iOnOff;
}

int GetSendToServer(void)
{
	return iSendToServer;
}


/************************************************************************************************
* The following method prints out the connection information
************************************************************************************************/

static void printConnectionStatus() 
{
 // Print the basic connection and network information: Network, IP, and Subnet mask
 ip = WiFi.localIP();
 Serial.print("Connected to ");
 Serial.print(ssid);
 Serial.print(" IP Address:: ");
 Serial.println(ip);
 subnet = WiFi.subnetMask();
 Serial.print("Netmask: ");
 Serial.println(subnet);

 // Print our MAC address.
 WiFi.macAddress(mac);
 Serial.print("WiFi Shield MAC address: ");
 Serial.print(mac[5],HEX);
 Serial.print(":");
 Serial.print(mac[4],HEX);
 Serial.print(":");
 Serial.print(mac[3],HEX);
 Serial.print(":");
 Serial.print(mac[2],HEX);
 Serial.print(":");
 Serial.print(mac[1],HEX);
 Serial.print(":");
 Serial.println(mac[0],HEX);

 // Print the wireless signal strength:
 rssi = WiFi.RSSI();
 Serial.print("Signal strength (RSSI): ");
 Serial.print(rssi);
 Serial.println(" dBm");

} // printConnectionStatus
 


