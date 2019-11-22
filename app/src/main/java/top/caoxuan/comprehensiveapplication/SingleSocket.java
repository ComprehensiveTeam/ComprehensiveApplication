package top.caoxuan.comprehensiveapplication;

import android.util.Log;

import java.io.IOException;
import java.net.Socket;

public class SingleSocket {
    private static Socket socket;
    private static boolean isFinished = false;
    private final static String host = "192.168.0.102";
    private final static int port = 10443;
    private static void cs() {
        isFinished = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("cxDebug", "正在连接服务器");
                    socket = new Socket(host, port);
                    Log.d("cxDebug", "连接服务器成功");
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
