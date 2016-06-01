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
#include <SPI.h>
#include "EntryExitBeamDriver.h"

#define EntryBeamRcvr  34 
#define ExitBeamRcvr   35


int EntryBeamState;
int ExitBeamState;

        
void EntryExitBeamSetup() 
{
  pinMode(EntryBeamRcvr, INPUT);     // Make entry IR rcvr an input
  digitalWrite(EntryBeamRcvr, HIGH); // enable the built-in pullup

  pinMode(ExitBeamRcvr, INPUT);      // Make exit IR rcvr an input
  digitalWrite(ExitBeamRcvr, HIGH);  // enable the built-in pullup

  Serial.begin(9600);
}
     
void EntryExitBeamLoop()
{
  EntryBeamState = digitalRead(EntryBeamRcvr);  // Here we read the state of the
                                                // entry beam.

  if (EntryBeamState == LOW)  // if EntryBeamState is LOW the beam is broken
  {   
    Serial.println("Entry beam broken");
  } else {
    Serial.println("Entry beam is not broken.");
  }

  ExitBeamState = digitalRead(ExitBeamRcvr);  // Here we read the state of the
                                              // exit beam.  
  if (ExitBeamState == LOW)  // if ExitBeamState is LOW the beam is broken
  {     
    Serial.println("Exit beam broken");
  } else {
    Serial.println("Exit beam is not broken.");
  }
  delay(1000);  
}


