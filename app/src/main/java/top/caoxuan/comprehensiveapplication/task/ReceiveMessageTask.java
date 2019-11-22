package top.caoxuan.comprehensiveapplication.task;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import top.caoxuan.comprehensiveapplication.SingleSocket;
import top.caoxuan.comprehensiveapplication.data.bean.IContent;
import top.caoxuan.comprehensiveapplication.listener.MessageListener;
import top.caoxuan.comprehensiveapplication.utils.AnalyseReturnData;

public class ReceiveMessageTask extends AsyncTask<Object, Integer, Integer> implements BaseTask {

    SharedPreferences pref;


    int self;
    MessageListener messageListener;

    public ReceiveMessageTask(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    /**
     * 先用iReceiver.receive()接收，再在ReceiveMessageTask中使用LitePal的save()存库
     */
    @Override
    protected Integer doInBackground(Object[] objects) {
        pref = (SharedPreferences) objects[0];
        self = pref.getInt("Self", 0);
        IReceiver iReceiver = new IReceiver();//因为IReceiver是内部类，receive()不能修饰为static，所以必须使用一个实例来调用
        while (true) {
            if (iReceiver.receive()) {
                /*if (save(iReceiver.iContent)) {
                    messageListener.onReceive();
                }*/
                //SaveMessageTask.save(iReceiver.iContent,messageListener);

            }
        }
    }


   /* boolean save(int receiver, Msg msg) {
        boolean saveResult = false;
        if (receiver == 0) {
            Log.d("cxDebug", "uid = " + receiver);
        } else if (receiver < Double.valueOf(Math.pow(10, 9)).intValue()) {
            GroupChatRecord group = new GroupChatRecord(receiver, msg.getAccount(), msg.getMessage());
            saveResult = group.save();
            List<GroupChatRecord> chatGroups = LitePal.findAll(GroupChatRecord.class);
            for (GroupChatRecord chatGroup : chatGroups) {
                Log.d("cxDebug", "groupId:" + chatGroup.getGroupId());
                Log.d("cxDebug", "account:" + chatGroup.getAccount());
                Log.d("cxDebug", "message:" + chatGroup.getMessage());
                Log.d("cxDebug", "分割线--------------------------");

            }
        } else {
            Log.d("cxDebug", "receiver id十亿");
            //当receiver id >= 十亿时，存储到用户表
        }

    }*/

    /**
     * 暂不使用SaveMessageTask，因为LitePal的save()本身异步的
     */
    /*boolean save(IContent iContent){
        SaveMessageTask saveMessageTask = new SaveMessageTask();
        boolean saveResult = false;
        int receiver = iContent.getReceiver();
        if ( receiver == 0) {
            Log.d("cxDebug", "uid = " + receiver);
        } else if (receiver < Double.valueOf(Math.pow(10, 9)).intValue()) {
            saveMessageTask.execute(iContent);

        } else {
            Log.d("cxDebug", "receiver id十亿");
            //当receiver id >= 十亿时，存储到用户表
        }
        return saveResult;
    }*/
    /*private void save(IContent iContent){
        boolean saveResult = false;
        int receiver = iContent.getReceiver();
        if ( receiver == 0) {
            Log.d("cxDebug", "uid = " + receiver);
        } else if (receiver < Double.valueOf(Math.pow(10, 9)).intValue()) {
            GroupChatRecord groupChatRecord = new GroupChatRecord(iContent.getUid(),iContent.getReceiver(),iContent.getMessage());
            groupChatRecord.saveAsync().listen(new SaveCallback() {
                @Override
                public void onFinish(boolean success) {
                    messageListener.onReceive();
                }
            });
        } else {
            Log.d("cxDebug", "receiver id十亿");
            //当receiver id >= 十亿时，存储到用户表
        }
    }*/

    private class IReceiver {
        private int requestType;
        private IContent iContent;

        boolean receive() {

            try {
                Socket socket = SingleSocket.getSocket();

                DataInputStream in = new DataInputStream(socket.getInputStream());

            /*while ((len = in.read(bytes)) != -1) {
                //注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
                sb.append(new String(bytes, 0, len,"UTF-8"));
            }*/
                while (true) {
                    String returnData = in.readUTF();
                    try {
                        System.err.println("returnData=" + returnData);

                        requestType = Integer.parseInt(AnalyseReturnData.opt(returnData, "request_type"));

                    } catch (Exception e) {
                        System.err.println("returnData=" + returnData);
                        System.err.println("requestType错误");
                        System.err.println(AnalyseReturnData.opt(returnData, "request_type"));
                        return false;
                    }
                    if (requestType == TYPE_RELAY) {
                        int uid = Integer.parseInt(AnalyseReturnData.opt(returnData, "uid"));
                        int receiver = Integer.parseInt(AnalyseReturnData.opt(returnData, "receiver"));
                        String message = AnalyseReturnData.opt(returnData, "message");
                        iContent = new IContent(uid, receiver, message);
                        Log.d("cxDebug", "rd:" + returnData);
                        SaveMessageTask.save(iContent);

                        messageListener.onReceive();
                        return true;
                    } else {
                        return false;
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }


}
