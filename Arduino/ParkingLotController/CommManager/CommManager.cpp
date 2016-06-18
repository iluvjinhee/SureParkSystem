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
#include <Timer.h>

#include <ArduinoJson.h>

#include "CommManager.h"
#include "..\ControlManager\ControlManager.h"
#include "..\ControlManager\SensorManager.h"

//debug
#include "..\DeviceDriver\ParkingStallSensorDriver.h"
#include "..\DeviceDriver\GateLiftDriver.h"

#define MAX_TARGET_NUM 5
#define MAX_WIFI_STRING_LENGTH 50

JsonObject& EncodingTxToJSONmsg(JsonBuffer& buf, int iTx);
bool DecodingJSONmsgToRx(JsonBuffer& buf, char *stringMsgJSONformat);

static void printConnectionStatus();
static void ConnectToWiFi(void);
static void HeartBeatComm(void)
{
	SetMsgNumber(CS_HeartBeat);
	SetSendToServer(true);
	__debug_print_stallsensor();
}

int GetTimeStamp(void);
void ClrTimeStamp(void);


#define __ID	"team5-1"
#define __PWD	"abcd1234"


#define __STR_MESSAGETYPE		"messagetype"
#define __STR_ID				"id"
#define __STR_PASSWORD			"pwd"
#define __STR_COMMAND			"command"
#define __STR_STATUS			"status"
#define __STR_1					"1"
#define __STR_0					"0"

#define __STR_OK				"ok"
#define __STR_NOK				"nok"

#define __STR_UP				"up"
#define __STR_DOWN				"down"

#define __STR_RED				"red"
#define __STR_GRN				"green"

#define __STR_OCCUFIED			"occufied"
#define __STR_EMPTY				"empty"


#define __STR_STR_ON			"on"
#define __STR_STR_OFF			"off"



#define __STR_SLOT_COUNT		"slot_count"
#define __STR_SLOTSTATUS		"slot_status"
#define __STR_LEDSTATUS			"led_status"
#define __STR_SLOT_NUMBER		"slot_number"

#define __STR_ENTRYGATE			"entrygate"
#define __STR_EXITGATE			"exitgate"
#define __STR_ENTRY_LED			"entrygateled"
#define __STR_EXIT_LED			"exitgateled"
#define __STR_ENTRYGATE_ARRIVE	"entrygate_arrive"
#define __STR_EXITGATE_ARRIVE	"exitgate_arrive"

#define __STR_TIMESTAMP			"timestamp"



#define TEST_LOCAL

#ifdef TEST_LOCAL
#define PORTID  550               // IP socket port ID
static IPAddress server(192,168,1,6);  // The server's IP address

#else
#define PORTID  9897               // IP socket port ID
static IPAddress server(192,168,1,184);  // The server's IP address

#endif

static char ssid[] = "LGArchi_Guest1";              // The network SSID for CMU unsecure network
static char pass[] = "16swarchitect";
static char c;                           // Character read from server
static int status = WL_IDLE_STATUS;      // Network connection status
static WiFiClient client;                // The client (our) socket
static IPAddress ip;                     // The IP address of the shield
static IPAddress subnet;                 // The IP address of the shield
static long rssi;                        // Wifi shield signal strength
static byte mac[6];                      // Wifi shield MAC address

static int iServerConnected=false;

static int iSendToServer=false;
static Timer t_HeartBeatTimer;

static String inputLine;	// Data from server
static int iMsgNumber=0;

static unsigned int iTimeStamp=0;

char strBuff[256]={0};

String clientMsg[] = {
	"Authentication_Request",
	"Parkinglot_Information",
	"Parkingslot_Sensor",
	"Parkingslot_LED",
	"EntryGate_Servo",
	"ExitGate_Servo",
	"EntryGate_LED",
	"ExitGate_LED",
	"EntryGate_Arrive",
	"EntryGate_PassBy",
	"ExitGate_Arrive",
	"ExitGate_PassBy",
	"HeartBeat"
};										//

String serverMsg[] = {
	"Authentication_Response",
	"ParkInfo_Request",
	"EntryGate_Control",
	"ExitGate_Control",
	"EntryGate_LED",
	"ExitGate_LED",
	"Parkingslot_LED",
	"Ack"
};										//


//--------------------------------------------------- interface start
// Client to Server
void CS_JSON_Authentification_Request(char *pcID, char *pcPWD)
{
}

// Server to Client
void SC_JSON_Authentication_Response(char *pcKey)
{
}
//--------------------------------------------------- interface end





JsonObject& EncodingTxToJSONmsg(JsonBuffer& buf, int iTx)
{
	JsonObject& root = buf.createObject();
	
	root[__STR_MESSAGETYPE]  = clientMsg[iTx];

	switch( iTx )
	{
		case CS_Authentication_Request :
			root[__STR_ID]  = __ID;		// id:string
			root[__STR_PASSWORD] = __PWD;	// pwd:string
			break;
			
		case CS_Parkinglot_Information :
			/*
			1. slot_number (int)
			2. slot_status (string[])
			3. led_status (string[])
			4. entrygate (string)
			5. exitgate (string)
			6. entrygateled (string)
			7. exitgateled (string)
			8. entrygate_arrive (string)
			9. exitgate_arrive (string)
			*/
			//1. slot_number (int)
			root[__STR_SLOT_COUNT] = (int)PARKSLOT_MAX;

			//2. slot_status (string[])
			{
				JsonArray& sl_status = root.createNestedArray(__STR_SLOTSTATUS);
				for( int i=0 ; i<PARKSLOT_MAX ; i++ )
				{
					sl_status.add(GetStallSensorOccupied((T_StallSensorID)i) == OCCUFIED ? __STR_1 : __STR_0);	// status : "empty" or "occupied"
				}
			}

			//3. led_status (string[])
			{
				JsonArray& jled_status =  root.createNestedArray(__STR_LEDSTATUS);
				for( int i=0 ; i<PARKSLOT_MAX ; i++ )
				{
					jled_status.add(GetParkingStallLED(i) == ON ? __STR_1 : __STR_0);	// status : "on" or "off"
				}
			}
			//4. entrygate_status (string)
			root[__STR_ENTRYGATE] = GetEntryGateServo() == Open ? __STR_1 : __STR_0;	// status : "up" or "down"

			//5. exitgate_status (string)
			root[__STR_EXITGATE] = GetExitGateServo() == Open ? __STR_1 : __STR_0;	// status : "up" or "down"
			
			//6. entrygateled_status (string)
			root[__STR_ENTRY_LED] = GetEntryGateLED() == GRN ? __STR_1 : __STR_0;	// status : "red" or "green"

			//7. exitgateled_status (string)
			root[__STR_EXIT_LED] = GetExitGateLED() == GRN ? __STR_1 : __STR_0;	// status : "red" or "green"

			//8. entrygate_arrive (string)
			root[__STR_ENTRYGATE_ARRIVE] = GetEntryBeamStatus() == BROKEN ? __STR_1 : __STR_0;	// status : "yes" or "no"

			//9. exitgate_arrive (string)
			root[__STR_EXITGATE_ARRIVE] = GetExitBeamStatus() == BROKEN ? __STR_1 : __STR_0;	// status : "yes" or "no"			
			break;
			
		case CS_Parkingslot_Sensor :
			root[__STR_SLOT_NUMBER] = GetChangedSlot()+1;
			root[__STR_STATUS] = GetStallSensorOccupied((T_StallSensorID)GetChangedSlot()) == OCCUFIED ? __STR_OCCUFIED : __STR_EMPTY;	// status : "empty" or "occupied"
			ClrChangedSlot();
			break;
			
		case CS_Parkingslot_LED :
			root[__STR_SLOT_NUMBER] = GetParkingStallLED(GetRequestedLed())+1;
			root[__STR_STATUS] = GetParkingStallLED(GetRequestedLed()) == ON ? __STR_STR_ON : __STR_STR_OFF;	// status : "on" or "off"
			ClrRequestedLed();
			break;
			
		case CS_EntryGate_Servo :
			root[__STR_STATUS] = GetEntryGateServo() == Open ? __STR_UP : __STR_DOWN;	// status : "up" or "down"
			break;
			
		case CS_ExitGate_Servo :
			root[__STR_STATUS] = GetExitGateServo() == Open ? __STR_UP : __STR_DOWN;	// status : "up" or "down"
			break;
			
		case CS_EntryGate_LED :
			root[__STR_STATUS] = GetEntryGateLED() == RED ? __STR_GRN : __STR_RED;	// status : "red" or "green"
			
		case CS_ExitGate_LED :
			root[__STR_STATUS] = GetExitGateLED() == RED ? __STR_GRN : __STR_RED;	// status : "red" or "green"
			break;
			
		case CS_EntryGate_Arrive :
			break;
			
		case CS_EntryGate_PassBy :
			break;
			
		case CS_ExitGate_Arrive :
			break;
			
		case CS_ExitGate_PassBy :
			break;
			
		case CS_HeartBeat :
			break;
			
		default :
			break;
	}




	root[__STR_TIMESTAMP] = iTimeStamp++;	// 0 ~ 4294967295(4Bytes)
	
	Serial.println();
	Serial.print("CS-");
	root.printTo(Serial);
	
//	Serial.println("EncodingTxToJSONmsg");
	return root;
	
}


bool DecodingJSONmsgToRx(JsonBuffer& buf, char *stringMsgJSONformat)
{
	String sKey;
	int iMsgIndex;
	String str;
	const char* ccMsgType;
	int iSlotNumber;
	
	JsonObject& root = buf.parseObject(stringMsgJSONformat);

	if (!root.success()) {
		Serial.println("parseObject() failed");
		return false;
	}

	ccMsgType = root[__STR_MESSAGETYPE];

	sKey = ccMsgType;

	for( iMsgIndex=0 ; iMsgIndex<SC_CLIENTTOSERVERMSG_MAX ; iMsgIndex++ )
	{
		if( sKey.equalsIgnoreCase(serverMsg[iMsgIndex]) == true ) break;
	}

	switch( iMsgIndex )
	{
		case SC_Authentication_Response :
			Serial.println();
			Serial.print(sKey);
			Serial.println("-Received.");
			//SetSendToServer(false);
			break;

		case SC_ParkInfo_Request :
			Serial.println();
			Serial.print(sKey);
			Serial.println("-Received.");
			SetMsgNumber(CS_Parkinglot_Information);
			SetSendToServer(true);
			break;
			
		case SC_EntryGate_Control :
			str = (const char*)root[__STR_COMMAND];

			if( str.equalsIgnoreCase(__STR_UP) )
			{
				EntryGateOpen();
			}
			else
			{
				EntryGateClose();
			}
			//SetSendToServer(false);
			break;

		case SC_ExitGate_Control :
			str = (const char*)root[__STR_COMMAND];

			if( str.equalsIgnoreCase(__STR_UP) )
			{
				ExitGateOpen();
			}
			else
			{
				ExitGateClose();
			}
			//SetSendToServer(false);			
			break;

		case SC_EntryGate_LED :
			str = (const char*)root[__STR_COMMAND];

			if( str.equalsIgnoreCase(__STR_GRN) )
			{
				SetEntryGateLED_Green();
			}
			else
			{
				SetEntryGateLED_Red();
			}
			//SetSendToServer(false);
			break;

		case SC_ExitGate_LED :
			str = (const char*)root[__STR_COMMAND];

			if( str.equalsIgnoreCase(__STR_GRN) )
			{
				SetExitGateLED_Green();
			}
			else
			{
				SetExitGateLED_Red();
			}
			//SetSendToServer(false);
			break;

		case SC_Parkingslot_LED :
			iSlotNumber = (int)(root[__STR_SLOT_NUMBER])-1;
			str = (const char*)root[__STR_COMMAND];
			if( iSlotNumber < PARKSLOT_MAX && iSlotNumber > 0 ) SetParkingStallLED(iSlotNumber, str.equalsIgnoreCase(__STR_STR_ON)?true:false);
			//SetSendToServer(false);
			break;

		case SC_Ack :
			Serial.println();
			Serial.print(sKey);
			Serial.println("-Received.");
			//SetSendToServer(false);
			break;
					
		default :
			Serial.println();
			Serial.print("Not found key..");
			Serial.println("-Received.");
			//SetSendToServer(false);
			break;
	}

	Serial.println();
	Serial.print("SC-");
	root.printTo(Serial);

//	Serial.println("DecodingJSONmsgToRx");
	return true;
}

void CommManagerSetup()  
{
	ConnectToWiFi();
	t_HeartBeatTimer.every(5000, HeartBeatComm);
	SetMsgNumber(CS_Authentication_Request);
}

static void ConnectToWiFi(void)
{

	if( status != WL_CONNECTED )
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
	}

	// Attempt to connect to server
	while( !client.connect(server, PORTID) )
	{
		Serial.println("Attempt to connect to server..");
		iServerConnected = false;
		delay(5000);
		return;
	}
	
    
    Serial.println("Connected.");
	iServerConnected = true;
    Serial.println();
}

void CommManagerLoop() 
{
	StaticJsonBuffer<300> JsonBuffer;  
	int strBuffIndex = 0;

	// wait for sensor init
	if( GetStallSensorReady() == false ) return;

	// heartbeat timer update
	t_HeartBeatTimer.update();

	//------------------------------------	
	// Check socket
	//------------------------------------
	if (!client.connected())
	{
		ClrTimeStamp();
		iServerConnected = false;
	    Serial.println();
	    Serial.println("disconnecting.");
	    client.stop();
	    ConnectToWiFi();
		return;
	}

	// message for schedule
	switch( GetTimeStamp() )
	{
		case 0 : SetMsgNumber(CS_Authentication_Request); break;
		case 1 : SetMsgNumber(CS_Parkinglot_Information); break;
		default :
			break;
	}

		
//#if 0


	if( iSendToServer == true )
	{
		//------------------------------------
		// Write to socket
		//------------------------------------
//		Serial.println("write to socket.");
		JsonObject& messageEncoding = EncodingTxToJSONmsg(JsonBuffer, iMsgNumber);     
		//messageEncoding.prettyPrintTo(Serial);   
		messageEncoding.printTo(strBuff,sizeof(strBuff)); 
		String sendStringBuf = strBuff;

		for( int i=0 ; i<sendStringBuf.length() ; i+=MAX_WIFI_STRING_LENGTH)
		{
			client.print(sendStringBuf.substring(i,i+MAX_WIFI_STRING_LENGTH));      
		}
		client.println();
		strBuff[0] = 0;
		strBuffIndex = 0;
		SetSendToServer(false);
	}
	else
	{
		//------------------------------------	
		// Read from socket
		//------------------------------------
//		Serial.println("Read from socket");
		char c = ' ';      
		strBuffIndex = 0;
		while(1)
		{
			if (client.available()) 
			{
				c = client.read();
				if( c == '\n' )
				{
					strBuff[strBuffIndex] = 0;
					break;
				}
				strBuff[strBuffIndex++] = c;
			}
			else break;
		}
		
		if( strBuffIndex > 0 )
		{
//			Serial.println();
//			Serial.print(strBuffIndex);
//			Serial.print(":");
//			Serial.println(strBuff);

			DecodingJSONmsgToRx(JsonBuffer, strBuff);
			strBuff[0] = 0;
			strBuffIndex = 0;
		}		
	}


//#endif	
	
#if 0

	if( iSendToServer )
	{
		Serial.print("To Server : ");
		Serial.println(clientMsg[iMsgNumber]);

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

		if( inputLine.length() > 0 )
		{
			Serial.print("From Server : ");
			Serial.println(inputLine);
			Serial.println();
		}

		inputLine = "";
		iMsgNumber=0;
		SetSendToServer(false);

	}

	if (!client.connected())
	{
		iServerConnected = false;
	    Serial.println();
	    Serial.println("disconnecting.");
	    client.stop();
	    ConnectToWiFi();
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

int GetServerConnected(void)
{
	return iServerConnected;
}


int GetTimeStamp(void)
{
	return iTimeStamp;
}

void ClrTimeStamp(void)
{
	iTimeStamp=0;
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
 


