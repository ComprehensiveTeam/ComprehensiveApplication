package top.caoxuan.comprehensiveapplication.task;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import top.caoxuan.comprehensiveapplication.SingleSocket;
import top.caoxuan.comprehensiveapplication.data.bean.IError;
import top.caoxuan.comprehensiveapplication.listener.SignInListener;
import top.caoxuan.comprehensiveapplication.utils.AnalyseReturnData;
import top.caoxuan.comprehensiveapplication.utils.RequestStringsUtils;

public class AutoSignInTask extends AsyncTask<Object, Integer, Boolean> implements BaseTask {
    SignInListener signInListener;
    int self;
    int token;
    IError iError;

    /*public AutoSignInTask(SignInListener signInListener) {
        this.signInListener = signInListener;
    }*/

    public AutoSignInTask(SignInListener signInListener, int self, int token) {
        this.signInListener = signInListener;
        this.self = self;
        this.token = token;
    }

    @Override
    protected Boolean doInBackground(Object[] objects) {

        try {
            DataOutputStream dataOutputStream = new DataOutputStream(SingleSocket.getSocket().getOutputStream());
            RequestStringsUtils rsu = new RequestStringsUtils();
            rsu.setRequestType(TYPE_LOGIN_BY_TOKEN);
            rsu.setUid(self);
            rsu.setToken(token);
            dataOutputStream.writeUTF(rsu.getRequestCouples());
            while (true) {
                DataInputStream dataInputStream = new DataInputStream(SingleSocket.getSocket().getInputStream());
                String returnData = dataInputStream.readUTF();
                Log.d("cxDebug", "Auto:" + returnData);
                int requestType = Integer.parseInt(AnalyseReturnData.opt(returnData, "request_type"));
                if (requestType == TYPE_LOGIN_BY_TOKEN) {
                    int result = Integer.parseInt(AnalyseReturnData.opt(returnData, "result"));
                    if (result == TYPE_RESULT_SUCCESS) {
                        token = Integer.parseInt(AnalyseReturnData.opt(returnData, "token"));
                        return true;
                    } else {
                        String error = AnalyseReturnData.opt(returnData, "error");
                        iError = new IError(TYPE_LOGIN_BY_TOKEN, error);
                        return false;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean bool) {
        if (bool) {
            signInListener.onSuccess(self, token);
        } else {
            signInListener.onFailed(iError);
        }
    }
}
