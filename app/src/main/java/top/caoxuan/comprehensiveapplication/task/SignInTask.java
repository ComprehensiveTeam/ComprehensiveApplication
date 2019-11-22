package top.caoxuan.comprehensiveapplication.task;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import top.caoxuan.comprehensiveapplication.SingleSocket;
import top.caoxuan.comprehensiveapplication.data.bean.IError;
import top.caoxuan.comprehensiveapplication.data.bean.User;
import top.caoxuan.comprehensiveapplication.listener.SignInListener;
import top.caoxuan.comprehensiveapplication.utils.AnalyseReturnData;
import top.caoxuan.comprehensiveapplication.utils.RequestStringsUtils;

public class SignInTask extends AsyncTask<Object, Integer, Integer> implements BaseTask {
    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    Socket socket;
    SignInListener listener;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private int self;
    private int token;
    IError iError;

    public SignInTask(SignInListener listener) {
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(Object... objects) {
        pref = (SharedPreferences) objects[2];
        editor = pref.edit();
        socket = SingleSocket.getSocket();
        User user = new User((String) objects[0], (String) objects[1]);
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            RequestStringsUtils rsu = new RequestStringsUtils();
            rsu.setRequestType(1);
            rsu.setAccount(user.getAccount());
            rsu.setPassword(user.getPassword());
            out.writeUTF(rsu.getRequestCouples());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            while (true) {
                String returnData = in.readUTF();
                Log.d("cxDebug", "SignInTask" + returnData);
                String requestType = AnalyseReturnData.opt(returnData, "request_type");
                if ("1".equals(requestType)) {
                    String result = AnalyseReturnData.opt(returnData, "result");
                    String error = AnalyseReturnData.opt(returnData, "error");
                    if ("1".equals(result) && "99999".equals(error)) {
                        self = Integer.parseInt(AnalyseReturnData.opt(returnData, "uid"));
                        token = Integer.parseInt(AnalyseReturnData.opt(returnData, "token"));
                        /**考虑到多用户登录问题，要在SignInActivity中创建一个与uid相关的SharePreference文件*/
                        /*editor.putInt("Self",uid);
                        Log.d("cxDebug","uid="+uid);
                        if (editor.commit())*/
                        return TYPE_SUCCESS;
                    }
                } else if ("4".equals(requestType)) {
                    iError = new IError(TYPE_LOGIN, AnalyseReturnData.opt(returnData, "error"));
                    return TYPE_FAILED;
                }
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
                Log.d("cxDebug", "SignInTask:TYPE_SUCCESS");
                listener.onSuccess(self, token);
                break;
            case TYPE_FAILED:
                listener.onFailed(iError);
                break;
            default:
                break;
        }
    }

}
