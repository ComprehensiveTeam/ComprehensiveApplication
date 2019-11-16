package com.example.comprehensiveapplication.ui;

import android.util.Log;

import com.example.comprehensiveapplication.Utils.RequestStringsUtils;

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
            RequestStringsUtils rsu = new RequestStringsUtils();
            rsu.setRequestType(3);
            rsu.setAccount(account);
            rsu.addRequestCouple("receiver", "1");
            rsu.setMsg(msg);
            out.writeUTF(rsu.getRequestCouples());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}



