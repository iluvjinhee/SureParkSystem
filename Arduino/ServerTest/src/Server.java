/******************************************************************************************************************
* File: Server.java
* Course: 2013 LG Executive Education Program
* Project: Iot (2013), Autowarehouse (2014)
* Copyright: Copyright (c) 2013 Carnegie Mellon University
* Versions:
*	1.0 Apr 2013.
*
* Description:
*
* This class serves as an example for how to write a server application that a client Arudino application can
* connect to. There is nothing uniquely specific to the Arduino in this code, other than the application level
* protocol that is used between this application and the Arduino. The assumption is that the Arduino is running
* the ClientDemo application. When this application is started it listens until a client connects. Once the client
* connects, this app reads data from the client until the client sends the string "Bye." Each string
* read from the client will be writen to the terminal. Once the "Bye." string is read, the server will send
* a single message back to the client. After this the session ends and server will listen for another client to
* connect. Note, this example server application is single threaded.
*
* Compilation and Execution Instructions:
*
*	Compiled in a command window as follows: javac Server.java
*	Execute the program from a command window as follows: java Server
*
* Parameters: 		None
*
* Internal Methods: None
*
******************************************************************************************************************/

import java.net.*;
import java.io.*;


public class Server
{
 	public static void main(String[] args) throws IOException
 	{
    	ServerSocket serverSocket = null;							// Server socket object
		Socket clientSocket = null;									// Client socket object
    	int	portNum = 550;											// Port number for server socket
    	String[]serverMsg = {
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

    	String[]clientMsg = {
    			"",
    			"Status:00000000",
    			"ArrivedEntryGate",
    			"LeaveEntryGate",
    			"ArrivedExitGate",
    			"LeaveExitGate",
    			"DriverAtrived_Slot1",
    			"DriverAtrived_Slot2",
    			"DriverAtrived_Slot3",
    			"DriverAtrived_Slot4",
    			"DriverLeaved_Slot1",
    			"DriverLeaved_Slot2",
    			"DriverLeaved_Slot3",
    			"DriverLeaved_Slot4",
			};										//

    	String inputLine;											// Data from client

		while(true)
		{
    		/*****************************************************************************
    	 	* First we instantiate the server socket. The ServerSocket class also does
    	 	* the listen()on the specified port.
		 	*****************************************************************************/
    		try
    		{
        		serverSocket = new ServerSocket(portNum);
        		System.out.println ( "\n\nWaiting for connection on port " + portNum + "." );
        	}
    		catch (IOException e)
        	{
        		System.err.println( "\n\nCould not instantiate socket on port: " + portNum + " " + e);
        		System.exit(1);
        	}

			/*****************************************************************************
    	 	* If we get to this point, a client has connected. Now we need to
    	 	* instantiate a client socket. Once its instantiated, then we accept the
    	 	* connection.
		 	*****************************************************************************/

	    	try
    		{
        		clientSocket = serverSocket.accept();
        	}
    		catch (Exception e)
        	{
        		System.err.println("Accept failed.");
        		System.exit(1);
        	}

			/*****************************************************************************
    	 	* At this point we are all connected and we need to create the streams so
    	 	* we can read and write.
		 	*****************************************************************************/

    		System.out.println ("Connection successful");
    		System.out.println ("Waiting for input.....");

    		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
    		BufferedReader in = new BufferedReader( new InputStreamReader( clientSocket.getInputStream()));

			/*****************************************************************************
    		 * Now we can read and write to and from the client. Our protocol is to wait
    		 * for the client to send us strings which we read until we get a "Bye." string
    	 	 * from the client.
		 	 *****************************************************************************/

 	    	try
 	    	{
 	    		while ((inputLine = in.readLine()) != null)
    			{
      				System.out.println ("Client Message: " + inputLine);

      				for( int i=0 ; i<clientMsg.length ; i++ )
      				{
      					//System.out.println ("server message: " + clientMsg[i]);
      					
      					if( inputLine.equals(clientMsg[i]) )
						{
      						try
      			 	    	{
      			 				System.out.println( "Sending message to client : " );
      			   				out.write( clientMsg[i], 0, clientMsg[i].length() );
      							out.flush();
      							
      						} catch (Exception e) {

      							System.err.println("write failed::");
      			        		//System.exit(1);

      						}
						}
      				}
      				
      				
   				} // while

			} catch (Exception e) {

				System.err.println("readLine failed::");
        		//System.exit(1);

			}


			/*****************************************************************************
    		 * Close up the I/O ports then close up the sockets
		 	 *****************************************************************************/

	    	out.close();
	    	in.close();

   		 	clientSocket.close();
	    	serverSocket.close();

			System.out.println ( "\n.........................\n" );

    	} // while loop
   	} // main
} // class