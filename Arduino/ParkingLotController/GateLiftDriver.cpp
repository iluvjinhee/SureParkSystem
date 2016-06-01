/****************************************************************
* File: GateLiftTest
* Project: LG Exec Ed Program
* Copyright: Copyright (c) 2016 Anthony J. Lattanze
* Versions:
* 1.0 May 2016 - Initial version
*
* Description:
*
* This program lifts and closes the entry and exit gates. Note that the servos are positional servos. 
* So the 0 position is closed. The 90 position is open. The other positions should be avoided for this
* project.
*
* Parameters: None
*
* Internal Methods: 
*
* void InitEntryExitLEDs() - Initializes the entry and exit LEDs
* to ensure they are off. 
***************************************************************/
#include "GateLiftDriver.h"
#include <SPI.h>
#include <Servo.h> 

#define EntryGateServoPin 5
#define ExitGateServoPin 6
#define Open  90
#define Close 0

static int delayvalue = 1000;
static Servo EntryGateServo;
static Servo ExitGateServo;

void GateLiftSetup() 
{ 
 
  // Initialize the Serial port:
  Serial.begin(9600);
  
  // Map servo to pin
  EntryGateServo.attach(EntryGateServoPin);
  ExitGateServo.attach(ExitGateServoPin);

} 

void GateLiftLoop() 
{
  Serial.println( "Close Both Gates" );  //Here we close both gates
  EntryGateServo.write(Close); 
  ExitGateServo.write(Close);  
  delay( delayvalue );
  
  Serial.println( "Open Entry Gate" );   //Here we open the entry gate
  EntryGateServo.write(Open);
  delay( delayvalue );

  Serial.println( "Close Entry Gate" );  //Here we close the entry gate
  EntryGateServo.write(Close);
  delay( delayvalue );
  
  Serial.println( "Open Exit Gate" );    //Here we open the exit gate
  ExitGateServo.write(Open);
  delay( delayvalue );
  
  Serial.println( "Close Exit Gate" );   //Here we close the exit gate
  ExitGateServo.write(Close);
  delay( delayvalue );
} 

