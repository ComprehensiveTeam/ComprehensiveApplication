package com.example.comprehensiveapplication;

import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Receiver {
    String account;
    String msg;

    public Receiver() {

    }

    public boolean receive(Socket socket) {
        try {

            DataInputStream in = new DataInputStream(socket.getInputStream());
            StringBuilder sb = new StringBuilder();
            /*while ((len = in.read(bytes)) != -1) {
                //注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
                sb.append(new String(bytes, 0, len,"UTF-8"));
            }*/
            sb.append(in.readUTF());
            if (sb.toString() != "")
                account = "account";
            msg = sb.toString();
            Log.d("cxdebug", "sb:" + sb.toString());
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
