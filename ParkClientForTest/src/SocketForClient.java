import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.simple.JSONObject;

import com.lge.sureparksystem.parkclientfortest.message.MessageParser;
import com.lge.sureparksystem.parkclientfortest.message.MessageType;
import com.lge.sureparksystem.parkclientfortest.message.SocketMessage;

public class SocketForClient {
	String dstAddress;
	int dstPort;
	
	private Socket socket = null;
	private BufferedReader in = null;
	private PrintWriter out = null;
	
	private Thread socketThread = null;
	
	public SocketForClient(String addr, int port) {
		dstAddress = addr;
		dstPort = port;
	}
	
	public void connect() {
		if(socketThread == null) {
			socketThread = new Thread(new Runnable() {
			    @Override
			    public void run() {
			    	if(socket == null) {
				        try {
				        	socket = new Socket(dstAddress, dstPort);
				        	
				        	if(socket.isConnected()) {			
				    		    out = new PrintWriter(socket.getOutputStream(), true);
				    		    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
							    
							    String jsonMessage = "";		    
							    while(true) {
							    	jsonMessage = in.readLine();
							    	if(jsonMessage != null && !jsonMessage.equals("")) {
										System.out.println(jsonMessage);
							    	}
							    	
							    	JSONObject testOutput = MessageParser.makeJSONObject(
							    			new SocketMessage(MessageType.RESERVATION_NUMBER, "aaaaa"));
							    	
							    	out.println(testOutput.toJSONString());
							    }
				    		}
				        } 
				        catch (Exception e) {
				            e.printStackTrace();
				        }
			    	}
			    }
			});
		
			socketThread.start();	
		}
	}
	
	public void disconnect() {
		if(socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(socketThread != null && socketThread.isAlive()) {
			socketThread.interrupt();
		}
		socketThread = null;
	}
	
	public void send(JSONObject jsonObject) {
		out.println(jsonObject.toJSONString());
	}
}