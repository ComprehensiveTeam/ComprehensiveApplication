package com.example.comprehensiveapplication.task;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.example.comprehensiveapplication.SingleSocket;
import com.example.comprehensiveapplication.Utils.AnalyseReturnData;
import com.example.comprehensiveapplication.Utils.RequestStringsUtils;
import com.example.comprehensiveapplication.data.bean.User;
import com.example.comprehensiveapplication.listener.SignInListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SignInTask extends AsyncTask<String, Integer, Integer> {
    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    Socket socket;
    SignInListener listener;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public SignInTask(SignInListener listener) {
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        socket = SingleSocket.getSocket();
        User user = new User(strings[0], strings[1]);
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            RequestStringsUtils rsu = new RequestStringsUtils();
            rsu.setRequestType(1);
            rsu.setAccount(strings[0]);
            rsu.setPassword(strings[1]);
            out.writeUTF(rsu.getRequestCouples());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            String resultData = in.readUTF();

            String requestType = AnalyseReturnData.opt(resultData, "requestType");
            String result = AnalyseReturnData.opt(resultData, "result");
            String error = AnalyseReturnData.opt(resultData, "error");
            if ("1".equals(requestType)) {
                if ("1".equals(result) && "99999".equals(error)) {


                    return TYPE_SUCCESS;
                }
            } else if ("4".equals(requestType)) {
                return TYPE_FAILED;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return TYPE_FAILED;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        switch (integer) {
            case TYPE_SUCCESS:
                listener.onSuccess();
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            default:
                break;
        }
    }

}
