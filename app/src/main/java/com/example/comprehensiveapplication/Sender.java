package com.example.comprehensiveapplication;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Sender {

    private String msg;
    private String account;
    Socket socket;

    public Sender(Socket socket, String msg, String account) {
        this.socket = socket;
        this.msg = msg;
        this.account = account;
    }

    public void sendMessage() {
        /*try {
            Socket socket = new Socket("100.64.178.154",8888);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            StringBuilder s = new StringBuilder();
            s.append("account=" + account);
            s.append("&msg=" + msg);
            Log.d("cxdebug", "msg:" + s.toString());
            out.writeUTF(s.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}



