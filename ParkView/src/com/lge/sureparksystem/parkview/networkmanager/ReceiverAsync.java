package com.lge.sureparksystem.parkview.networkmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.lge.sureparksystem.parkview.controller.Controller;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

public class ReceiverAsync extends AsyncTask<Socket, String, Void> {
	private Controller controller = null;
	private BufferedReader in = null;
	
	public ReceiverAsync(Controller controller) {
		this.controller = controller;
	}
	
	@Override
	protected Void doInBackground(Socket... arg0) {
		Socket socket = arg0[0];
		try {
			if(socket.isConnected()) {			
			    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			    
			    String jsonMessage = "";		    
			    while(true) {
			    	jsonMessage = in.readLine();
			    	if(jsonMessage != null && !jsonMessage.equals("")) {
						publishProgress(jsonMessage);
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
    protected void onProgressUpdate(String... jsonMessage) {
		controller.parseJSONMessage(jsonMessage[0]);
    }

	@Override
	protected void onPostExecute(Void result) {		
		super.onPostExecute(result);
	}
}