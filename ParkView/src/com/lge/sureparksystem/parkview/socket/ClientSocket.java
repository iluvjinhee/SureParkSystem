package com.lge.sureparksystem.parkview.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import android.os.AsyncTask;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

public class ClientSocket extends AsyncTask<Void, String, Void> {
	public static final String IP_ADDRESS = "192.168.1.183";
	public static final int PORT = 9898;

	String dstAddress;
	int dstPort;
	String response = "";
	TextView textResponse;
	
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
		Socket socket = null;

		try {
			socket = new Socket(dstAddress, dstPort);

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
			byte[] buffer = new byte[1024];

			int bytesRead;
			InputStream inputStream = socket.getInputStream();			
			
			// notice: inputStream.read() will block if no data return			 
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				byteArrayOutputStream.write(buffer, 0, bytesRead);
				response += byteArrayOutputStream.toString("UTF-8");
				
				publishProgress(response);
				
				response = "";
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
			response = "UnknownHostException: " + e.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
			response = "IOException: " + e.toString();
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