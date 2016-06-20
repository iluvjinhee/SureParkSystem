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
import java.util.Scanner;
import java.util.Iterator;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
public class ServerJson  extends Thread
{
	
    int seq;
	static BufferedWriter out;
	static BufferedReader in;
    
    public ServerJson(int seq) {
        this.seq = seq;
    }

    public void run() {
        //System.out.println(this.seq+" thread start.");
    	while(true)
    	{

	    	try {
	        		Scanner keyboard = new Scanner(System.in);
	        		System.out.println("enter an integer");
	        		int myint = keyboard.nextInt();
	        		
	        		switch(myint)
	        		{
	        			case 1:
	        				SC_Authentication_Response();
	        				break;
	        			case 2:
	        				SC_Parkinglot_Status_Request();
	        				break;
	        			case 3:
	        				SC_LED2_Control_on();
	        				break;
	        			case 4:
	        				SC_LED2_Control_off();
	        				break;
	        			case 5:
	        				SC_EntryGate_Control_on();
	        				break;
	        			case 6:
	        				SC_EntryGate_Control_off();
	        				break;
	        			default:
	        				System.out.println("Nop!!");
	        				break;
	        				
	        		}
	        		//Thread.sleep(1000);
	        }catch(Exception e) {}
    	}
//        System.out.println(this.seq+" thread end.");
    }
	
	public void SC_Authentication_Response() throws IOException
	{
 		JSONObject msgRuleManager = new JSONObject();
 		msgRuleManager.put("messagetype","Authentication_Response");
 		msgRuleManager.put("result","ok");
	
 		String sendMsgPacket = msgRuleManager.toString() + "\n";
 
		System.out.println( "Sending message to client...." ); 		
 		System.out.println ("sendMsgJSON:" + sendMsgPacket);
 		out.write( sendMsgPacket, 0, sendMsgPacket.length() ); 	
		out.flush(); 		
	}

	public void SC_Parkinglot_Status_Request() throws IOException
	{
 		JSONObject msgRuleManager = new JSONObject();
 		msgRuleManager.put("messagetype","ParkInfo_Request"); 		
 		
 		String sendMsgPacket = msgRuleManager.toString() + "\n";
 
		System.out.println( "Sending message to client...." ); 		
 		System.out.println ("sendMsgJSON:" + sendMsgPacket);
 		out.write( sendMsgPacket, 0, sendMsgPacket.length() ); 	
		out.flush(); 		
	}

	public void SC_LED2_Control_on() throws IOException
	{
 		JSONObject msgRuleManager = new JSONObject();
 		msgRuleManager.put("messagetype","Parkingslot_LED");
 		msgRuleManager.put("slot_number", 2);
 		msgRuleManager.put("command", "on");
 		
 		String sendMsgPacket = msgRuleManager.toString() + "\n";
 
		System.out.println( "Sending message to client...." ); 		
 		System.out.println ("sendMsgJSON:" + sendMsgPacket);
 		out.write( sendMsgPacket, 0, sendMsgPacket.length() ); 	
		out.flush(); 		
	}

	public void SC_LED2_Control_off() throws IOException
	{
 		JSONObject msgRuleManager = new JSONObject();
 		msgRuleManager.put("messagetype","Parkingslot_LED");
 		msgRuleManager.put("slot_number", 6);
 		msgRuleManager.put("command", "off");
  		
 		String sendMsgPacket = msgRuleManager.toString() + "\n";
 
		System.out.println( "Sending message to client...." ); 		
 		System.out.println ("sendMsgJSON:" + sendMsgPacket);
 		out.write( sendMsgPacket, 0, sendMsgPacket.length() ); 	
		out.flush(); 		
	}

	public void SC_EntryGate_Control_on() throws IOException
	{
 		JSONObject msgRuleManager = new JSONObject();
 		msgRuleManager.put("messagetype","EntryGate_Control");
 		msgRuleManager.put("command", "up");
 
 		String sendMsgPacket = msgRuleManager.toString() + "\n";
 
		System.out.println( "Sending message to client...." ); 		
 		System.out.println ("sendMsgJSON:" + sendMsgPacket);
 		out.write( sendMsgPacket, 0, sendMsgPacket.length() ); 	
		out.flush(); 		
	}
	
	public void SC_EntryGate_Control_off() throws IOException
	{
 		JSONObject msgRuleManager = new JSONObject();
 		msgRuleManager.put("messagetype","EntryGate_Control");
 		msgRuleManager.put("command", "down");
		
 		String sendMsgPacket = msgRuleManager.toString() + "\n";
 
		System.out.println( "Sending message to client...." ); 		
 		System.out.println ("sendMsgJSON:" + sendMsgPacket);
 		out.write( sendMsgPacket, 0, sendMsgPacket.length() ); 	
		out.flush(); 		
	}
	
 	public static void main(String[] args) throws IOException
 	{
    	ServerSocket serverSocket = null;							// Server socket object
		Socket clientSocket = null;									// Client socket object
    	int	portNum = 550;											// Port number for server socket
    	String[]serverMsg = {	"Hello there little Arduino;..\n", 	// Server messages. You can add
    							"Hello Arduino from the PC...\n"	//  more messages here if you want to.
    						};										//
    	int msgNum = 0;												// Message to display from serverMsg[]
   		String inputLine;											// Data from client

 		//event message from rule manager
   		
        Thread t = new ServerJson(0);
        t.start();

 		
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
    		

			/*****************************************************************************
			 * Print out the fact that we are sending a message to the client, then we
			 * send the message to the client.
			 *****************************************************************************/
    		out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
    		in = new BufferedReader( new InputStreamReader( clientSocket.getInputStream()));
/*		
 	    	try
 	    	{
 				System.out.println( "Sending message to client...." );
   				out.write( sendMsgPacket, 0, sendMsgPacket.length() );
   				//out.write( serverMsg[0], 0, serverMsg[0].length() );
   				
				out.flush();

			} catch (Exception e) {

				System.err.println("write failed::");
        		//System.exit(1);

			}
 */	    	
			/*****************************************************************************
    		 * Now we can read and write to and from the client. Our protocol is to wait
    		 * for the client to send us strings which we read until we get a "Bye." string
    	 	 * from the client.
		 	 *****************************************************************************/
 	    	System.out.println ("Waiting for input.....");
 	    	try
 	    	{
 	    		while ((inputLine = in.readLine()) != null)
    			{
      				System.out.println ("Client Message: " + inputLine);

      	 	    	try
      	 	    	{
//     	 				System.out.println( "Sending message to client...." );
      	 				
//     	 				String sendMsgPacket = inputLine + "\n";
      	 				
//      	   				out.write( sendMsgPacket, 0, sendMsgPacket.length() );
      	   				//out.write( serverMsg[0], 0, serverMsg[0].length() );
      				} catch (Exception e) {

      					System.err.println("write failed::");
      	        		//System.exit(1);

      				}
   				} // while

			} catch (Exception e) {

				System.err.println("readLine failed::");
        		//System.exit(1);

			}
	    	
	    	in.close();
 	    	out.close();

   		 	clientSocket.close();
	    	serverSocket.close();

			System.out.println ( "\n.........................\n" );

    	} // while loop
   	} // main
} // class