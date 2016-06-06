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

public class ClientSocketAsync extends AsyncTask<Socket, String, Void> {
	TextView textResponse;
	
	private BufferedReader in = null;
	
	public ClientSocketAsync(TextView textResponse) {
		this.textResponse = textResponse;
	}
	
	@Override
    protected void onProgressUpdate(String... response) {
		textResponse.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 50);
		textResponse.setText(response[0]);
    }

	@Override
	protected Void doInBackground(Socket... arg0) {
		Socket socket = arg0[0];
		try {
			if(socket.isConnected()) {			
			    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			    
			    String response = "";		    
			    while(true) {
			    	response = in.readLine();
			    	if(!response.equals("")) {
						publishProgress(response);
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
}