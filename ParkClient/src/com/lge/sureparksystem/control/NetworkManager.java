package com.lge.sureparksystem.control;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.lge.sureparksystem.util.Utils;

import org.json.simple.JSONObject;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetworkManager {

    private static final String TAG = "NetworkManager";

    private Socket mSocket = null;
    private ReceiverAysnc mReceiver = null;
    private Thread mSocketThread;
    private InetAddress mDstAddress;
    private int mDstPort;
    private NetworkToActivity mNetworkToManager;
    private PrintWriter mPrintWriter;

    public NetworkManager(InetAddress addr, int port, NetworkToActivity ntm) {
        mDstAddress = addr;
        mDstPort = port;
        mNetworkToManager = ntm;
    }

    public void connect() {
        if (mSocketThread == null) {
            mSocketThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    if (mSocket == null) {
                        try {
                            Log.d(TAG, "mSocketThread run");
                            mSocket = new Socket(mDstAddress, mDstPort);
                            mReceiver = new ReceiverAysnc();
                            if (mSocket.isConnected()) {
                                Log.d(TAG, "mSocket.isConnected()");
                                mPrintWriter = new PrintWriter(mSocket.getOutputStream(), true);
                            }
                            mReceiver.execute(mSocket);
                        } catch (Exception e) {
                            Log.d(TAG, "Exception : " + e.toString());
                            e.printStackTrace();
                        }
                    }
                }
            });
            mSocketThread.start();
        }
    }

    public void disconnect() {
        if (mSocket != null) {
            try {
                mSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (mReceiver != null) {
            mReceiver.cancel(true);
        }
        if (mSocketThread != null && mSocketThread.isAlive()) {
            mSocketThread.interrupt();
        }
        mSocketThread = null;
    }

    public boolean sendMessage(JSONObject jsonObject) {
        Log.d(TAG, "sendMessage : " + jsonObject.toJSONString());
        if (mPrintWriter == null) {
            return false;
        }
        mPrintWriter.println(jsonObject.toJSONString());
        return true;
    }

    class ReceiverAysnc extends AsyncTask<Socket, String, Void> {

        private BufferedReader mBufferedReader = null;

        @Override
        protected Void doInBackground(Socket... params) {
            Socket socket = params[0];
            Log.d(TAG, "doInBackground");
            try {
                if (socket.isConnected()) {
                    mBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String jsonMessage = "";
                    while (true) {
                        jsonMessage = mBufferedReader.readLine();
                        if (jsonMessage != null && !jsonMessage.equals("")) {
                            Log.d(TAG, "recevied " + jsonMessage.toString());
                            publishProgress(jsonMessage);
                        }
                    }
                }
            } catch (UnknownHostException uhe) {
                uhe.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                try {
                    if (socket != null) {
                        socket.close();
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... jsonMessage) {
            mNetworkToManager.parseJSONMessage(jsonMessage[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}
