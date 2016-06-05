package com.lge.sureparksystem.parkview.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

public class ClientSocket extends AsyncTask<Void, String, Void> {
	public static final String IP_ADDRESS = "192.168.1.183";
	public static final int PORT = 9898;

	String dstAddress;
	int dstPort;
	TextView textResponse;
	
	private Socket socket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;
	
	public ClientSocket(String addr, int port, TextView textResponse) {
		dstAddress = addr;
		dstPort = port;
		this.textResponse = textResponse;
	}
	
	@Override
    protected void onProgressUpdate(String... response) {
		textResponse.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 50);
		textResponse.setText(response[0]);
    }

	@Override
	protected Void doInBackground(Void... arg0) {
		try {
			socket = new Socket(dstAddress, dstPort);

			if(socket.isConnected()) {			
			    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			    out = new PrintWriter(socket.getOutputStream(), true);
			    
			    String response = "";		    
			    while(true) {
			    	response = in.readLine();
			    	if(!response.equals("")) {
						publishProgress(response);
			    	}
			    	
			    	try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block					
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {		
		super.onPostExecute(result);
	}
	
	public void sendMsg(String msg) {
		try {
			if(socket.isConnected()) {
				out.println(msg);
			}
		} catch (Exception e) {
	        Log.i("ClientSocket", "Socket is NULL!!!");
	    }
	}
}