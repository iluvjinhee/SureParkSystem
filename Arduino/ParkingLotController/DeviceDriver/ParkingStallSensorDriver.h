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

#ifndef _PARKINGSTALLSENSORDRIVER_H_
#define _PARKINGSTALLSENSORDRIVER_H_

typedef enum
{
	STALLSENSOR_001 = 0,	///< Stall Sensor 1
	STALLSENSOR_002,			///< Stall Sensor 2
	STALLSENSOR_003,			///< Stall Sensor 3
	STALLSENSOR_004,			///< Stall Sensor 4
	STALLSENSOR_MAX				///< Stall Sensor Max
}T_StallSensorID;

#define OCCUFIED		1
#define UNOCCUFIED	0

int __debug_print_stallsensor(void);

void ParkingStallSensorSetup();
void ParkingStallSensorLoop();

int GetStallSensorReady(void);
int GetStallSensorOccupied(T_StallSensorID t_StallSensorId);
int GetStallSensorChanged(T_StallSensorID t_StallSensorId);
int SetStallSensorChanged(T_StallSensorID t_StallSensorId, int iStatus);
int SetStallSensorSensitivity(T_StallSensorID t_StallSensorId, unsigned char ucSt);

#endif // _PARKINGSTALLSENSORDRIVER_H_

/* End of File */





