package com.example.comprehensiveapplication.task;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
import android.widget.Toast;

import com.example.comprehensiveapplication.Utils.RequestStringsUtils;
import com.example.comprehensiveapplication.data.bean.Registrant;
import com.example.comprehensiveapplication.listener.SignUpListener;
import com.example.comprehensiveapplication.SingleSocket;
import com.example.comprehensiveapplication.Utils.AnalyseReturnData;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SignUpTask extends AsyncTask<Object, Integer, List<Object>> {
    final static int SIGN_UP = 1;
    final static int SEND = 2;
    final static int SIGN_UP_BY_PHONE = 1;
    final static int SIGN_UP_BY_EMAIL = 2;
    final static int SEND_SMS = 1;
    final static int SEND_EMAIL = 2;

    SignUpListener signUpListener;
    Socket socket = SingleSocket.getSocket();
    RequestStringsUtils rsu = new RequestStringsUtils();

    public SignUpTask(SignUpListener signUpListener) {
        this.signUpListener = signUpListener;
    }

    @Override
    protected List<Object> doInBackground(Object... objects) {
        boolean executeResult = false;
        List<Object> results = new ArrayList<>();
        rsu.setRequestType(2);
        int executeType = (int) objects[0];
        switch (executeType) {
            case 1:
                executeType = SIGN_UP;
                executeResult = signUp(objects);
                break;
            case 2:
                executeType = SEND;
                executeResult = sendVCode(executeType, objects[2].toString());

            default:
                break;
        }
        results.add(executeType);
        results.add(executeResult);
        return results;
    }

    private boolean sendVCode(int type, String account) {
        switch (type) {
            case 1://短信
                sendSms(account);
                break;
            case 2://邮箱
                sendEmail(account);
                break;
        }
        return waitSendingResult();
    }

    private void sendSms(String phoneNumber) {
        try {
            //rsu.setAccountType(1);
            rsu.setAccount(phoneNumber);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(rsu.getRequestCouples());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendEmail(String emailAddress) {
        try {
            //rsu.setAccountType(2);
            rsu.setAccount(emailAddress);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(rsu.getRequestCouples());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    boolean waitSendingResult() {
        Socket socket = SingleSocket.getSocket();
        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            String returnData = dataInputStream.readUTF();
            String requestType = AnalyseReturnData.opt(returnData, "requestType");
            String result = AnalyseReturnData.opt(returnData, "result");
            String error = AnalyseReturnData.opt(returnData, "error");
            if ("2".equals(requestType) && "1".equals(result) && "99999".equals(error)) return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    boolean waitSignUpResult() {
        Socket socket = SingleSocket.getSocket();
        try {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            String returnData = in.readUTF();
            String requestType = AnalyseReturnData.opt(returnData, "requestType");
            String result = AnalyseReturnData.opt(returnData, "result");
            String error = AnalyseReturnData.opt(returnData, "error");
            if ("2".equals(requestType) && "1".equals(result) && "99999".equals(error)) return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    protected void onPostExecute(List<Object> objects) {


        switch ((int) objects.get(0)) {
            case SIGN_UP:
                if ((boolean) objects.get(1)) {
                    signUpListener.onSuccess();
                } else {
                    signUpListener.onFailed();
                }
                break;
            case SEND:
                if ((boolean) objects.get(1)) {
                    signUpListener.onSendSuccess();
                } else {
                    signUpListener.onSendFailed();
                }
                break;
        }


    }

    boolean signUp(Object[] objects) {
        Registrant registrant = (Registrant) objects[2];
        String account = registrant.getAccount();
        String vCode = registrant.getvCode();
        String password = registrant.getPassword();
        socket = SingleSocket.getSocket();
        try {
            RequestStringsUtils rsu = new RequestStringsUtils();
            rsu.setRequestType(2);

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            /*switch ((int)(objects[1])) {
                case 1:
                    rsu.setAccount(account);
                    rsu.addRequestCouple("phoneVCode", vCode);
                    break;
                case 2:
                    rsu.setAccount(account);
                    rsu.addRequestCouple("emailVCode", vCode);
                    break;
            }*/
            rsu.setAccount(account);
            rsu.addRequestCouple("verificationCode", vCode);
            rsu.setPassword(password);
            out.writeUTF(rsu.getRequestCouples());
            return waitSignUpResult();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
