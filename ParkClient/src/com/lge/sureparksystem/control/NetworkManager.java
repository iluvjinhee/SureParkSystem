package com.lge.sureparksystem.control;

import android.os.Handler;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class NetworkManager {

    private static final String TAG = "CommunicationManager";
    private static String IP = "192.168.0.1";
    public static final int PORT = 9898;
    private Runnable mRunnable;
    private Socket socket;
    private DataInputStream is;
    private DataOutputStream os;
    private String readMsg;
    private Handler handler;

    public NetworkManager() {
        mRunnable = new Runnable() {
            public void run() {
                Log.d(TAG, "");
                try {
                    socket = new Socket(InetAddress.getByName(IP), PORT);
                    is = new DataInputStream(socket.getInputStream());
                    os = new DataOutputStream(socket.getOutputStream());
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                while (true) {
                    try {
                        readMsg = is.readUTF();
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                Log.d(TAG, "수신 데이타 : " + readMsg);
                            }
                        });
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    } finally {
                        Log.d(TAG, "");
                    }
                }
            }
        };
        Thread mThread = new Thread(mRunnable);
        mThread.start();
    }

}
