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
#include <WiFi.h>
#include <ArduinoJson.h>

#include "CommManager.h"
#include "..\ControlManager\ControlManager.h"
#include "..\libraries\Timer\Timer.h"

//debug
#include "..\DeviceDriver\ParkingStallSensorDriver.h"

#define MAX_TARGET_NUM 5
#define MAX_WIFI_STRING_LENGTH 50


JsonObject& EncodingStrucMsgToJSONmsg(JsonBuffer& buf);
bool DecodingJSONmsgToStructMsg(JsonBuffer& buf, char *stringMsgJSONformat);

static void printConnectionStatus();
static void ConnectToWiFi(void);
static void HeartBeatComm(void){SetMsgNumber(0); SetSendToServer(true); __debug_print_stallsensor();}

#define PORTID  550               // IP socket port ID

static char ssid[] = "LGArchi_Guest1";              // The network SSID for CMU unsecure network
static char pass[] = "16swarchitect";
static char c;                           // Character read from server
static int status = WL_IDLE_STATUS;      // Network connection status
static IPAddress server(192,168,1,6);  // The server's IP address
static WiFiClient client;                // The client (our) socket
static IPAddress ip;                     // The IP address of the shield
static IPAddress subnet;                 // The IP address of the shield
static long rssi;                        // Wifi shield signal strength
static byte mac[6];                      // Wifi shield MAC address


static int iSendToServer=false;
static Timer t_HeartBeatTimer;

static String inputLine;	// Data from server
static int iMsgNumber=0;

char stringbuf[256]={0};

struct sMessage
{
  String send1;
  String send2;
  String send3;
} jTxMsg, jRxMsg;

/*
void SendStringWiFiClient(void)
{
  sendStringMsg = String(stringbuf);
  client.println(sendStringMsg.substring(0,20));
  for(int i = 0 ; i < sendStringMsg.length() ; i+=MAX_WIFI_STRING_LENGTH)
  {
    client.println(sendStringMsg.substring(i,i+MAX_WIFI_STRING_LENGTH-1));      
  }
} */    

static String serverMsg[] = {
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

#if 0
static String clientMsg[] = {
	"ParkingLotStatus:",
	"DriverArrivedAtEntryGate",
	"DriverLeaveAtEntryGate",
	"DriverArrivedAtExitGate",
	"DriverLeaveAtExitGate",
	"DriverAtrived_Slot1",
	"DriverAtrived_Slot2",
	"DriverAtrived_Slot3",
	"DriverAtrived_Slot4",
};										//
#endif

static String clientMsg[] = {
	"Status:00000000",
	"ArrivedEntryGate",
	"LeaveEntryGate",
	"ArrivedExitGate",
	"LeaveExitGate",
	"DriverAtrived_Slot1",
	"DriverAtrived_Slot2",
	"DriverAtrived_Slot3",
	"DriverAtrived_Slot4",
};										//



void CommManagerSetup()  
{
	ConnectToWiFi();
	t_HeartBeatTimer.every(5000, HeartBeatComm);
	SetMsgNumber(0);

}

static void ConnectToWiFi(void)
{
	Serial.println("Attempting to connect to network...");
	Serial.print("SSID: ");
	Serial.println(ssid);

	// Attempt to connect to Wifi network.
	while ( status != WL_CONNECTED) 
	{ 
		Serial.print("Attempting to connect to SSID: ");
		Serial.println(ssid);
		status = WiFi.begin(ssid, pass);
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
		delay(2000);
	}
    
    Serial.println("Connected.");
    Serial.println();
}

void CommManagerLoop() 
{
	t_HeartBeatTimer.update();

	// Here we attempt connect to the server on the port specified above
	StaticJsonBuffer<200> jsonBuffer;  
	int stringbufIndex = 0;
	Serial.print("\nAttempting to connect to server...");

	if (client.connect(server, PORTID)) 
	{
	  Serial.println("connected");

	  // We write a couple of messages to the server
	  Serial.print("Server Message: ");
	  
	  char c = ' ';      
	  stringbufIndex = 0;
	  while (1)
	  {
	    if (client.available()) 
	    {
	      c = client.read();
	      if( c == ';' )
	      {
	        stringbuf[stringbufIndex] = 0;
	        break;
	      }
	      stringbuf[stringbufIndex++] = c;
	    }
	  }       
	  DecodingJSONmsgToStructMsg(jsonBuffer, stringbuf);
	  jTxMsg = jRxMsg;
	  JsonObject& messageEncoding = EncodingStrucMsgToJSONmsg(jsonBuffer);     
	  messageEncoding.prettyPrintTo(Serial);   
	  messageEncoding.printTo(stringbuf,sizeof(stringbuf)); 
	  delay(1000);
	  String sendStringMsg = stringbuf;


	  for(int i = 0 ; i < sendStringMsg.length() ; i+=MAX_WIFI_STRING_LENGTH)
	  {
	    client.print(sendStringMsg.substring(i,i+MAX_WIFI_STRING_LENGTH));      
	  } 
	  client.println(); 
	  client.println("Bye.");     
	  client.println(); 
	  delay(2000);

	  // That's it. We wait a second, then do it all again.
	  client.stop();
	  Serial.println();
	  Serial.println( "Send JSON message Done...");
	  delay(1000);
	  
	} // if
	
#if 0
	if( iSendToServer )
	{
		// We write a couple of messages to the server
		client.println(clientMsg[iMsgNumber]);

		
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

		inputLine = "";
		SetSendToServer(false);

		if (!client.connected())
		{
		    Serial.println();
		    Serial.println("disconnecting.");
		    client.stop();
		    ConnectToWiFi();
		}
	}
#endif	
} //  LOOP

void SetMsgNumber(int iMsgIndex)
{
	iMsgNumber = iMsgIndex;
}


void SetSendToServer(int iOnOff)
{
	iSendToServer = iOnOff;
}

int GetSendToServer(void)
{
	return iSendToServer;
}



JsonObject& EncodingStrucMsgToJSONmsg(JsonBuffer& buf)
{
  JsonObject& msg = buf.createObject();

  msg["send1"] = jTxMsg.send1.c_str();
  msg["send2"] = jTxMsg.send2.c_str();
  msg["send3"] = jTxMsg.send3.c_str();
  

  return msg;
}


bool DecodingJSONmsgToStructMsg(JsonBuffer& buf, char *stringMsgJSONformat)
{

  JsonObject& msg = buf.parseObject(stringMsgJSONformat);

  if (!msg.success()) {
    Serial.println("parseObject() failed");
    return false;
  }

  jRxMsg.send1 = (const char*)(msg["send1"]);
  jRxMsg.send2 = (const char*)(msg["send2"]);
  jRxMsg.send3 = (const char*)(msg["send3"]);

  return true;
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
 


