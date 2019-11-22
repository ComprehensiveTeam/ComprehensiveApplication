package top.caoxuan.comprehensiveapplication.task;

import android.util.Log;

import top.caoxuan.comprehensiveapplication.data.bean.GroupChatRecord;
import top.caoxuan.comprehensiveapplication.data.bean.IContent;

/**
 * 该Task并未继承AsyncTask，使用 saveAsync().listen(new SaveCallback()监听返回结果，成功后通知Service接收
 */
public class SaveMessageTask {
    /*@Override
    protected Boolean doInBackground(Object[] objects) {
        Boolean saveResult = false;
        IContent iContent = (IContent) objects[0];
        int receiver = iContent.getReceiver();
        if ( receiver == 0) {
            Log.d("cxDebug", "uid = " + receiver);
        } else if (receiver < Double.valueOf(Math.pow(10, 9)).intValue()) {
            GroupChatRecord group = new GroupChatRecord(iContent);
            saveResult = group.save();
            //打印结果显示是否存储成功
            List<GroupChatRecord> groupChatRecords = LitePal.findAll(GroupChatRecord.class);
            for (GroupChatRecord groupChatRecord : groupChatRecords) {
                Log.d("cxDebug", "groupId:" + groupChatRecord.getUid());
                Log.d("cxDebug", "account:" + groupChatRecord.getGroupId());
                Log.d("cxDebug", "message:" + groupChatRecord.getMessage());
                Log.d("cxDebug", "分割线--------------------------");

            }
        } else if (receiver > Double.valueOf(Math.pow(2, 32)).intValue()){
            Log.d("cxDebug", "receiver id十亿");
            //当receiver id >= 十亿时，存储到用户表
        } else {
            Log.d("cxDebug", "receive id 大于2^32");
        }
        return saveResult;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        //日后再写
    }*/

    /**
     * 暂时不反馈存储结果
     */
    /*public static void save(IContent iContent, final MessageListener messageListener){
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
    public static void save(IContent iContent) {
        int receiver = iContent.getReceiver();
        if (receiver == 0) {
            Log.d("cxDebug", "uid = " + receiver);
        } else if (receiver < Double.valueOf(Math.pow(10, 9)).intValue()) {
            GroupChatRecord groupChatRecord = new GroupChatRecord(iContent.getUid(), iContent.getReceiver(), iContent.getMessage());
            /*groupChatRecord.saveAsync().listen(new SaveCallback() {
                @Override
                public void onFinish(boolean success) {
                    Log.d("DataBase","结果："+success);
                }
            });*/
            groupChatRecord.save();
        } else {
            Log.d("cxDebug", "receiver id十亿");
            //当receiver id >= 十亿时，存储到用户表
        }
    }
}
