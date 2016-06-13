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
#include "..\libraries\Timer\Timer.h"


#define AVG_CNT		10		//
#define STALLSENSOR_SAMPLING_CALLBACK_TIMER		10		//

#define DEFAULT_SENSITIVITY				5

#define AUTO_CALI_ENSITIVITY			5	// sensor average/AUTO_CALI_ENSITIVITY
#define AUTO_CALI_ENSITIVITY_MAX		10	// 
#define AUTO_CALI_ENSITIVITY_MIN		1	//

#define STALLSENSOR_INITCNT_MAX			10
#define STALLSENSOR_UNOCCUFIED_MIN		20


static void StallSensorSampling(void);
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
static unsigned char aucStallSensorSensitivity[STALLSENSOR_MAX];


//final result
static int aiStallSensorStatus[STALLSENSOR_MAX];
static int aiStallSensorStatusPrev[STALLSENSOR_MAX];

static int iStallSensorReady=0;

Timer t_StallSensorSamplingTimer;	


void ParkingStallSensorSetup()
{
	SetStallSensorSensitivity(STALLSENSOR_001, DEFAULT_SENSITIVITY);
	SetStallSensorSensitivity(STALLSENSOR_002, DEFAULT_SENSITIVITY);
	SetStallSensorSensitivity(STALLSENSOR_003, DEFAULT_SENSITIVITY);
	SetStallSensorSensitivity(STALLSENSOR_004, DEFAULT_SENSITIVITY);

	for(int i=0; i<STALLSENSOR_MAX ; i++ ) aiStallSensorStatus[i] = UNOCCUFIED;

	t_StallSensorSamplingTimer.every(STALLSENSOR_SAMPLING_CALLBACK_TIMER, StallSensorSampling);

}

void ParkingStallSensorLoop()
{

	t_StallSensorSamplingTimer.update();	
}

static void StallSensorSampling(void)
{
	static int i=0;
	static int j=0;
	static int iStallSensorStatusPrev=0;
	
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

			if( GetStallSensorReady() == false )
			{
				aiStallSensorStatus[k] = aucStallSensorCur[k]  < STALLSENSOR_UNOCCUFIED_MIN ? UNOCCUFIED : OCCUFIED;
			}
			else
			{
				switch( aiStallSensorStatus[k] )
				{
					case OCCUFIED :
						aiStallSensorStatus[k] = aucStallSensorPrev[k] - aucStallSensorCur[k] > aucStallSensorSensitivity[k] ? UNOCCUFIED : OCCUFIED;
						break;

					case UNOCCUFIED :
						aiStallSensorStatus[k] = aucStallSensorCur[k] - aucStallSensorPrev[k] > aucStallSensorSensitivity[k] ? OCCUFIED : UNOCCUFIED;
						break;

					default :
						break;
				}
			}

			// Sensitivity Setting When stall sensor change from UNOCCUFIED to OCCUFIED
			if( aiStallSensorStatusPrev[k] == UNOCCUFIED && aiStallSensorStatus[k] == OCCUFIED )
			{
				SetStallSensorSensitivity((T_StallSensorID)k, constrain(aucStallSensorPrev[k]/AUTO_CALI_ENSITIVITY, AUTO_CALI_ENSITIVITY_MIN, AUTO_CALI_ENSITIVITY_MAX));			
			}
		}

		if( GetStallSensorReady() == false ) iStallSensorReady++;
		
	}
}

int GetStallSensorReady(void)
{
	if( iStallSensorReady < STALLSENSOR_INITCNT_MAX ) return false;
	else return true;
}

int GetStallSensorOccupied(T_StallSensorID t_StallSensorId)
{
	if( t_StallSensorId >= STALLSENSOR_MAX ) return -1;	// error
	else return aiStallSensorStatus[t_StallSensorId]; 
}

int SetStallSensorSensitivity(T_StallSensorID t_StallSensorId, unsigned char ucSt)
{
	if( t_StallSensorId >= STALLSENSOR_MAX ) return -1;	// error
	else
	{
		return (int)(aucStallSensorSensitivity[t_StallSensorId] = ucSt) ; 
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
		Serial.println(aucStallSensorCur[3]);
		Serial.print("Sensitivity : ");
		Serial.print(aucStallSensorSensitivity[0]);
		Serial.print(", ");
		Serial.print(aucStallSensorSensitivity[1]);
		Serial.print(", ");
		Serial.print(aucStallSensorSensitivity[2]);
		Serial.print(", ");
		Serial.print(aucStallSensorSensitivity[3]);
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



