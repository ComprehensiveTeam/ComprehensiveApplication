package top.caoxuan.comprehensiveapplication.task;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import top.caoxuan.comprehensiveapplication.SingleSocket;
import top.caoxuan.comprehensiveapplication.data.bean.IContent;
import top.caoxuan.comprehensiveapplication.listener.MessageListener;
import top.caoxuan.comprehensiveapplication.utils.AnalyseReturnData;
import top.caoxuan.comprehensiveapplication.utils.RequestStringsUtils;

public class SyncMessageListTask extends AsyncTask<Object, Integer, Boolean> implements BaseTask {


    SharedPreferences pref;
    private MessageListener messageListener;

    public SyncMessageListTask(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    @Override
    protected Boolean doInBackground(Object[] objects) {
        pref = (SharedPreferences) objects[0];
        int self = pref.getInt("Self", 0);
        int cursor = (int) objects[1];
        try {
            RequestStringsUtils rsu = new RequestStringsUtils();
            rsu.setRequestType(TYPE_SYNC);
            rsu.setUid(self);
            rsu.setReceiver(1);
            rsu.setCursor(cursor);
            DataOutputStream dataOutputStream = new DataOutputStream(SingleSocket.getSocket().getOutputStream());
            dataOutputStream.writeUTF(rsu.getRequestCouples());
            ObjectInputStream objectInputStream = new ObjectInputStream(SingleSocket.getSocket().getInputStream());
            try {
                List<String> chatRecordList;
                chatRecordList = (ArrayList<String>) objectInputStream.readObject();
                Log.d("cxDebug", "chatRecordList" + chatRecordList);
                List<IContent> iContentList = new ArrayList<>();
                IContent iContent;
                for (String s : chatRecordList
                ) {
                    int uid = Integer.parseInt(AnalyseReturnData.opt(s, "uid"));
                    int receiver = Integer.parseInt(AnalyseReturnData.opt(s, "receiver"));
                    String message = AnalyseReturnData.opt(s, "message");
                    iContent = new IContent(uid, receiver, message);
                    SaveMessageTask.save(iContent);
                    iContentList.add(iContent);
                }
                return true;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result)
            messageListener.onSync();

    }
}
