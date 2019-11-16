package com.example.comprehensiveapplication.ui;

import com.example.comprehensiveapplication.Utils.AnalyseReturnData;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Receiver {
    String account;
    String msg;

    public Receiver() {

    }

    public String getMsg() {
        return msg;
    }

    public boolean receive(Socket socket) {
        try {

            DataInputStream in = new DataInputStream(socket.getInputStream());

            /*while ((len = in.read(bytes)) != -1) {
                //注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
                sb.append(new String(bytes, 0, len,"UTF-8"));
            }*/
            String returnData = in.readUTF();

            msg = AnalyseReturnData.opt(returnData, "message");
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
