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
	SetMsgNumber(CS_IamAlive);
	SetSendToServer(true);
//	__debug_print_stallsensor();
}

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

static int iServerConnected=false;


static int iSendToServer=false;
static Timer t_HeartBeatTimer;

static String inputLine;	// Data from server
static int iMsgNumber=0;

static unsigned int iTimeStamp=0;

char strBuff[256]={0};

String clientMsg[] = {
	"Authentification_Request",
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
	"IamAlive",
	"Parkinglot_Status"
};										//

String serverMsg[] = {
	"Authentification_Response",
	"EntryGate_Control",
	"ExitGate_Control",
	"EntryGate_LED",
	"ExitGate_LED",
	"Parkingslot_LED",
};										//

/*
struct sCSMessage
{
	String Authentification_Request;
	String Parkinglot_Information;
	String Parkingslot_Sensor;
	String Parkingslot_LED;
	String EntryGate_Servo;
	String ExitGate_Servo;
	String EntryGate_LED;
	String ExitGate_LED;
	String EntryGate_Arrive;
	String EntryGate_PassBy;
	String ExitGate_Arrive;
	String ExitGate_PassBy;
	String IamAlive;
	String Parkinglot_Status;
}jTxMsg;

struct sSCMessage
{
	String Authentification_Response;
	String EntryGate_Control;
	String ExitGate_Control;
	String EntryGate_LED;
	String ExitGate_LED;
	String Parkingslot_LED;
}jRxMsg;
*/

//--------------------------------------------------- interface start
// Client to Server
void CS_JSON_Authentification_Request(char *pcID, char *pcPWD)
{
}

// Server to Client
void SC_JSON_Authentification_Response(char *pcKey)
{
}
//--------------------------------------------------- interface end





JsonObject& EncodingTxToJSONmsg(JsonBuffer& buf, int iTx)
{
	JsonObject& root = buf.createObject();
	JsonArray& data = root.createNestedArray(clientMsg[iTx]);

	switch( iTx )
	{
		case CS_Authentification_Request :
//			JsonArray& data = root.createNestedArray(clientMsg[iTx]);
			data.add("my_id"); // id:string
			data.add("my_password");	// password:string
			break;
			
		case CS_Parkinglot_Information :
			root[clientMsg[iTx]] = (int)PARKSLOT_MAX;	// number of slot : int
			break;
			
		case CS_Parkingslot_Sensor :
//			JsonArray& data = root.createNestedArray(clientMsg[iTx]);
			data.add(GetChangedSlot()); // sensor number : int
			data.add(GetStallSensorOccupied((T_StallSensorID)GetChangedSlot()) == OCCUFIED ? "occupied":"empty");	// status : "empty" or "occupied"
			ClrChangedSlot();
			break;
			
		case CS_Parkingslot_LED :
//			JsonArray& data = root.createNestedArray(clientMsg[iTx]);
			data.add(GetParkingStallLED(GetRequestedLed()));					// requested led : int
			data.add(GetParkingStallLED(GetRequestedLed()) == ON ? "on":"off");	// status : "on" or "off"
			ClrRequestedLed();
			break;
			
		case CS_EntryGate_Servo :
			root[clientMsg[iTx]] = GetEntryGateServo() == Open ? "up":"down";	// status : "up" or "down"
			break;
			
		case CS_ExitGate_Servo :
			root[clientMsg[iTx]] = GetExitGateServo() == Open ? "up":"down";	// status : "up" or "down"
			break;
			
		case CS_EntryGate_LED :
			root[clientMsg[iTx]] = GetEntryGateLED() == RED ? "red":"green";	// status : "red" or "green"
			
		case CS_ExitGate_LED :
			root[clientMsg[iTx]] = GetExitGateLED() == RED ? "red":"green";	// status : "red" or "green"
			break;
			
		case CS_EntryGate_Arrive :
			root[clientMsg[iTx]] = "";	// -
			break;
			
		case CS_EntryGate_PassBy :
			root[clientMsg[iTx]] = "";	// -
			break;
			
		case CS_ExitGate_Arrive :
			root[clientMsg[iTx]] = "";	// -
			break;
			
		case CS_ExitGate_PassBy :
			root[clientMsg[iTx]] = "";	// -
			break;
			
		case CS_IamAlive :
			root[clientMsg[iTx]] = iTimeStamp++;	// 0 ~ 4294967295(4Bytes)
			break;
			
		case CS_Parkinglot_Status :
//			JsonArray& data = root.createNestedArray(clientMsg[iTx]);
			/*
			1. slot info : number of slot
			2. stall sensor status : all slot
			3. parking slot led status : all slot
			4. entry gate servo status : open/close
			5. exit gate servo status : open/close
			6. entry gate led status : red/green
			7. exit gate led status : red/green
			*/
			data.add((int)PARKSLOT_MAX); // 1. number of slot

			for( int i=0 ; i<PARKSLOT_MAX ; i++ )
				data.add(GetStallSensorOccupied((T_StallSensorID)i) == OCCUFIED ? "occupied":"empty");	// status : "empty" or "occupied"

			for( int i=0 ; i<PARKSLOT_MAX ; i++ )
				data.add(GetParkingStallLED(i) == ON ? "on":"off");	// status : "on" or "off"

			data.add(GetEntryGateServo() == Open ? "up":"down");	// status : "up" or "down"
			data.add(GetExitGateServo() == Open ? "up":"down");	// status : "up" or "down"
			
			data.add(GetEntryGateLED() == RED ? "red":"green");	// status : "red" or "green"
			data.add(GetExitGateLED() == RED ? "red":"green");	// status : "red" or "green"
			break;
			
		default :
			break;	
	}
	
	Serial.println();
	Serial.print("CS-");
	root.printTo(Serial);
	
//	Serial.println("EncodingTxToJSONmsg");
	return root;
	
}


bool DecodingJSONmsgToRx(JsonBuffer& buf, char *stringMsgJSONformat)
{
	String sKey;

	JsonObject& root = buf.parseObject(stringMsgJSONformat);

	if (!root.success()) {
		Serial.println("parseObject() failed");
		return false;
	}
/*
	SC_Authentification_Response = 0,
	SC_EntryGate_Control,
	SC_ExitGate_Control,
	SC_EntryGate_LED,
	SC_ExitGate_LED,
	SC_Parkingslot_LED,
	SC_CLIENTTOSERVERMSG_MAX
*/
	sKey = String((const char *)root[serverMsg[SC_Authentification_Response]]);

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
	SetMsgNumber(CS_IamAlive);
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
		delay(1000);
	}
    
    Serial.println("Connected.");
	iServerConnected = true;
    Serial.println();
}

void CommManagerLoop() 
{
	StaticJsonBuffer<200> JsonBuffer;  
	int strBuffIndex = 0;

	if( GetStallSensorReady() == false ) return;

	t_HeartBeatTimer.update();
	
//#if 0
	if( iSendToServer || client.available() )
	{
//		Serial.print("To Server : ");
//		Serial.println(clientMsg[iMsgNumber]);

		// We write a couple of messages to the server
		//client.println(clientMsg[iMsgNumber]);

//------------------------------------
		JsonObject& messageEncoding = EncodingTxToJSONmsg(JsonBuffer, iMsgNumber);     
		//messageEncoding.prettyPrintTo(Serial);   
		messageEncoding.printTo(strBuff,sizeof(strBuff)); 
		String sendStringBuf = strBuff;

		for( int i=0 ; i<sendStringBuf.length() ; i+=MAX_WIFI_STRING_LENGTH)
		{
			client.print(sendStringBuf.substring(i,i+MAX_WIFI_STRING_LENGTH));      
		}
//------------------------------------	

		char c = ' ';      
		strBuffIndex = 0;
		while(1)
		{
			if (client.available()) 
			{
				c = client.read();
				if( c == ';' )
				{
					strBuff[strBuffIndex] = 0;
					break;
				}
				strBuff[strBuffIndex++] = c;
			}
			else break;
		}

		DecodingJSONmsgToRx(JsonBuffer, strBuff);

		SetSendToServer(false);

		if (!client.connected())
		{
			iServerConnected = false;
		    Serial.println();
		    Serial.println("disconnecting.");
		    client.stop();
		    ConnectToWiFi();
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
 


