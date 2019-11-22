package top.caoxuan.comprehensiveapplication.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import top.caoxuan.comprehensiveapplication.data.bean.GroupChatRecord;
import top.caoxuan.comprehensiveapplication.data.bean.IContent;
import top.caoxuan.comprehensiveapplication.listener.CoreListener;
import top.caoxuan.comprehensiveapplication.listener.MessageListener;
import top.caoxuan.comprehensiveapplication.task.ReceiveMessageTask;
import top.caoxuan.comprehensiveapplication.task.SendMessageTask;
import top.caoxuan.comprehensiveapplication.task.SyncMessageListTask;

public class CoreService extends Service implements BaseService {

    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    private CoreListener coreListener;

    private MessageListener messageListener = new MessageListener() {
        @Override
        public void onReceive() {
            coreListener.onReceive();
            Log.d("cxDebug", "你收到了一条消息");
            //Toast.makeText(CoreService.this,"你收到了一条消息",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSend() {
            coreListener.onSend();
            Toast.makeText(CoreService.this, "你发送了一条消息", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSave() {

        }

        @Override
        public void onSync() {
            coreListener.onSyncMessageList();
            Log.d("cxDebug", "GroupChatRecordCount=" + LitePal.count(GroupChatRecord.class));
        }
    };

    public CoreService() {

    }

    CoreBinder coreBinder = new CoreBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return coreBinder;
    }

    public class CoreBinder extends Binder {
        public void startListening(CoreListener mCoreListener) {
            coreListener = mCoreListener;
        }

        public void startReceiving(CoreListener mCoreListener) {
            coreListener = mCoreListener;
            String spName = getSharedPreferences("UserProfile", MODE_PRIVATE).getString("Default", "");
            pref = getSharedPreferences(spName, MODE_PRIVATE);
            ReceiveMessageTask receiveMessageTask = new ReceiveMessageTask(messageListener);
            Executor executor = new ThreadPoolExecutor(15, 200, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
            receiveMessageTask.executeOnExecutor(executor, pref);


        }

        public void stopReceiving() {

        }

        public void startSending(IContent iContent) {
            String spName = getSharedPreferences("UserProfile", MODE_PRIVATE).getString("Default", "");
            pref = getSharedPreferences(spName, MODE_PRIVATE);
            SendMessageTask sendMessageTask = new SendMessageTask(messageListener);
            Executor executor = new ThreadPoolExecutor(15, 200, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
            sendMessageTask.executeOnExecutor(executor, iContent);

            //sendMessageTask.execute(iContent);

        }

        public void startSyncMessageList(CoreListener mCoreListener, int cursor) {
            coreListener = mCoreListener;
            String spName = getSharedPreferences("UserProfile", MODE_PRIVATE).getString("Default", "");
            pref = getSharedPreferences(spName, MODE_PRIVATE);
            SyncMessageListTask syncMessageListTask = new SyncMessageListTask(messageListener);
            Executor executor = new ThreadPoolExecutor(15, 200, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
            syncMessageListTask.executeOnExecutor(executor, pref, cursor);
        }
    }
}
