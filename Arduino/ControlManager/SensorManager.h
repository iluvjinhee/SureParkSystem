/****************************************************************
* File: EntryExitBeamTest
* Project: LG Exec Ed Program
* Copyright: Copyright (c) 2016 Anthony J. Lattanze
* Versions:
* 1.0 May 2016 - Initial version
*
* Description:
*
* This program tests the IR beam used at the entry and exit to detect when a car
* is entering or exiting the parking lot. It simply reads each sensor every 
* second and indicates whether the beam is broken (obstructed) or not.
*
* Parameters: None
*
* Internal Methods: 
*
* void InitEntryExitLEDs() - Initializes the entry and exit LEDs
* to ensure they are off. 
***************************************************************/

#ifndef _SENSORMANAGER_H_
#define _SENSORMANAGER_H_

#include "..\DeviceDriver\ParkingStallSensorDriver.h"
#include "..\DeviceDriver\EntryExitBeamDriver.h"

typedef enum
{
	PARKSLOT_001 = 0,	///< PARK Slot 1
	PARKSLOT_002,			///< PARK Slot 2
	PARKSLOT_003,			///< PARK Slot 3
	PARKSLOT_004,			///< PARK Slot 4
	PARKSLOT_MAX			///< PARK Slot Max
}T_ParkingSlotID;

void SensorManagerSetup(void);  
void SensorManagerLoop(void); 
int CheckParkSlotOccupied(T_ParkingSlotID t_ParkSlotID);
int DriverArriveAtEntryGate(void);
int DriverLeaveAtEntryGate(void);
int DriverArriveAtExitGate(void);
int DriverLeaveAtExitGate(void);


#endif // _SENSORMANAGER_H_

/* End of File */



