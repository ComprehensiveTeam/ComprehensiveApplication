package top.caoxuan.comprehensiveapplication.task;

import android.os.AsyncTask;

import org.litepal.LitePal;

import java.util.List;

import top.caoxuan.comprehensiveapplication.data.bean.GroupChatRecord;
import top.caoxuan.comprehensiveapplication.data.bean.IMessage;

public class AccessIMessageListTask extends AsyncTask implements BaseTask {
    @Override
    protected IMessage doInBackground(Object[] objects) {
        IMessage iMessage = new IMessage();
        List<GroupChatRecord> groupChatRecordList = LitePal.findAll(GroupChatRecord.class);
        for (GroupChatRecord groupChatRecord : groupChatRecordList) {
            if (groupChatRecord.getGroupId() == 1) {

            }
        }
        return null;
    }
}
