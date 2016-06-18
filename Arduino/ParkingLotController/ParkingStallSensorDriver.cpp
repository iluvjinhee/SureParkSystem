/****************************************************************
* File: ParkingStallSensorTest
* Project: LG Exec Ed Program
* Copyright: Copyright (c) 2014 Anthony J. Lattanze
* Versions:
* 1.0 May 2014 - Initial version
* 2.0 May 2016 - Updated pins for new parking garage project.
*
* Description:
*
* The parking stall detectors are QTI sensors. This program reads
* the sensor values and writes them to the debug console.
* This program can be used to calibrate the values read by the 
* QTIs to determine if cars are parked in the parking spaces 
*
* Parameters: None
*
* Internal Methods: 
*
* void ProximityVal(int sensorIn) - returns an integer value from
* the QTI sensor that is an indication of the amount of light be 
* reflected into the sensor.
*
* void InitEntryExitLEDs() - Initializes the entry and exit LEDs
* to ensure they are off. 
***************************************************************/

#include <SPI.h>
#include "ParkingStallSensorDriver.h"

#define AVG_CNT		4
#define DEFAULT_OCCUPIED_SENSITIVITY	5
#define DEFAULT_UNOCCUPIED_SENSITIVITY	5

static long ProximityVal(int Pin);


static long  Stall1SensorVal;
static long  Stall2SensorVal;
static long  Stall3SensorVal;
static long  Stall4SensorVal;

static int acStallSensorPin[STALLSENSOR_MAX] = {30, 31, 32, 33};
static long alStallSensorVal[STALLSENSOR_MAX];

static unsigned int auiStallSensorSum[STALLSENSOR_MAX]={0};

//regulated sensor value
static unsigned char aucStallSensorCur[STALLSENSOR_MAX]={0};
static unsigned char aucStallSensorPrev[STALLSENSOR_MAX]={0};


//sensitivity
static unsigned char aucStallSensorOccufiedSensitivity[STALLSENSOR_MAX];
static unsigned char aucStallSensorUnOccufiedSensitivity[STALLSENSOR_MAX];


//final result
static int aiStallSensorStatus[STALLSENSOR_MAX]={UNOCCUFIED};

static int iStallSensorReady = 0;


void ParkingStallSensorSetup()
{
	SetStallSensorOccufiedSensitivity(STALLSENSOR_001, DEFAULT_OCCUPIED_SENSITIVITY);
	SetStallSensorOccufiedSensitivity(STALLSENSOR_002, DEFAULT_OCCUPIED_SENSITIVITY);
	SetStallSensorOccufiedSensitivity(STALLSENSOR_003, DEFAULT_OCCUPIED_SENSITIVITY);
	SetStallSensorOccufiedSensitivity(STALLSENSOR_004, DEFAULT_OCCUPIED_SENSITIVITY);

	SetStallSensorUnOccufiedSensitivity(STALLSENSOR_001, DEFAULT_UNOCCUPIED_SENSITIVITY);
	SetStallSensorUnOccufiedSensitivity(STALLSENSOR_002, DEFAULT_UNOCCUPIED_SENSITIVITY);
	SetStallSensorUnOccufiedSensitivity(STALLSENSOR_003, DEFAULT_UNOCCUPIED_SENSITIVITY);
	SetStallSensorUnOccufiedSensitivity(STALLSENSOR_004, DEFAULT_UNOCCUPIED_SENSITIVITY);

}

void ParkingStallSensorLoop()
{
	static int i=0;
	static int j=0;
	
	alStallSensorVal[i] = ProximityVal(acStallSensorPin[i]);

	auiStallSensorSum[i] += map(alStallSensorVal[i], 0, 1023, 0, 255);

	i++;

	// every turn change sensor
	if( i >= STALLSENSOR_MAX )
	{
		i=0;
		j++;
	}

	//data smoothing
	if( j >= AVG_CNT )
	{
		j=0;

		for( int k=0 ; k<STALLSENSOR_MAX ; k++ )
		{
			aucStallSensorPrev[k] = aucStallSensorCur[k];
			aucStallSensorCur[k] = auiStallSensorSum[k]/AVG_CNT;
			auiStallSensorSum[k] = 0;

			switch( aiStallSensorStatus[k] )
			{
				case OCCUFIED :
					aiStallSensorStatus[k] = aucStallSensorPrev[k] - aucStallSensorCur[k] > aucStallSensorOccufiedSensitivity[k] ? UNOCCUFIED : OCCUFIED;
					break;

				case UNOCCUFIED :
					aiStallSensorStatus[k] = aucStallSensorCur[k] - aucStallSensorPrev[k] > aucStallSensorUnOccufiedSensitivity[k] ? OCCUFIED : UNOCCUFIED;
					break;

				default :
					break;
			}
		}


#if 0
		Serial.print(aiStallSensorStatus[0]);
		Serial.print(", ");
		Serial.print(aiStallSensorStatus[1]);
		Serial.print(", ");
		Serial.print(aiStallSensorStatus[2]);
		Serial.print(", ");
		Serial.print(aiStallSensorStatus[3]);
		Serial.print("\n");
#endif
	
	}

}

int GetStallSensorOccupied(T_StallSensorID t_StallSensorId)
{
	if( t_StallSensorId >= STALLSENSOR_MAX ) return -1;	// error
	else return aiStallSensorStatus[t_StallSensorId]; 
}

int SetStallSensorOccufiedSensitivity(T_StallSensorID t_StallSensorId, unsigned char ucSt)
{
	if( t_StallSensorId >= STALLSENSOR_MAX ) return -1;	// error
	else
	{
		return (int)(aucStallSensorOccufiedSensitivity[t_StallSensorId] = ucSt) ; 
	}
}

int SetStallSensorUnOccufiedSensitivity(T_StallSensorID t_StallSensorId, unsigned char ucSt)
{
	if( t_StallSensorId >= STALLSENSOR_MAX ) return -1;	// error
	else
	{
		return (int)(aucStallSensorUnOccufiedSensitivity[t_StallSensorId] = ucSt) ; 
	}
}

int __debug_print_stallsensor(void)
{
		Serial.print("StallSensorVal : ");
		Serial.print(aucStallSensorCur[0]);
		Serial.print(", ");
		Serial.print(aucStallSensorCur[1]);
		Serial.print(", ");
		Serial.print(aucStallSensorCur[2]);
		Serial.print(", ");
		Serial.print(aucStallSensorCur[3]);
		Serial.println("\n");
}


/*********************************************************************
* long ProximityVal(int Pin)
* Parameters:            
* int pin - the pin on the Arduino where the QTI sensor is connected.
*
* Description:
* QTI schematics and specs: http://www.parallax.com/product/555-27401
* This method initalizes the QTI sensor pin as output and charges the
* capacitor on the QTI. The QTI emits IR light which is reflected off 
* of any surface in front of the sensor. The amount of IR light 
* reflected back is detected by the IR resistor on the QTI. This is 
* the resistor that the capacitor discharges through. The amount of 
* time it takes to discharge determines how much light, and therefore 
* the lightness or darkness of the material in front of the QTI sensor.
* Given the closeness of the object in this application you will get
* 0 if the sensor is covered
***********************************************************************/
static long ProximityVal(int Pin)
{
    long duration = 0;
    pinMode(Pin, OUTPUT);         // Sets pin as OUTPUT
    digitalWrite(Pin, HIGH);      // Pin HIGH
    delay(1);                     // Wait for the capacitor to stabilize

    pinMode(Pin, INPUT);          // Sets pin as INPUT
    digitalWrite(Pin, LOW);       // Pin LOW
    while(digitalRead(Pin))       // Count until the pin goes
    {                             // LOW (cap discharges)
       duration++;                
    }   
    return duration;              // Returns the duration of the pulse
}



