package top.caoxuan.comprehensiveapplication.ui;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import top.caoxuan.comprehensiveapplication.SingleSocket;
import top.caoxuan.comprehensiveapplication.utils.RequestStringsUtils;

@Deprecated
public class Sender {

    private String msg;
    private String account;
    private int uid;

    public Sender(String msg, String account) {
        this.msg = msg;
        this.account = account;
    }

    public Sender(String msg, int uid) {
        this.msg = msg;
        this.uid = uid;
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
            Socket socket = SingleSocket.getSocket();
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            RequestStringsUtils rsu = new RequestStringsUtils();
            rsu.setRequestType(3);
            rsu.setAccount(account);
            rsu.addRequestCouple("receiver", "1");
            rsu.setMessage(msg);
            out.writeUTF(rsu.getRequestCouples());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}



