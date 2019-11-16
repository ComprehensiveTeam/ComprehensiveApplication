package com.example.comprehensiveapplication;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.Socket;

public class SingleSocket {
    private static Socket socket;
    private static boolean isFinished = false;

    private static void cs() {
        isFinished = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("cxdebug", "正在连接服务器");
                    socket = new Socket("193.112.188.71", 10443);
                    Log.d("cxdebug", "连接服务器成功");
                    isFinished = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static Socket getSocket() {
        if (socket == null) {
            cs();
            while (!isFinished) {
            }
        }
        return socket;
    }


}
