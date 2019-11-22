package top.caoxuan.comprehensiveapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import top.caoxuan.comprehensiveapplication.adapter.MessageAdapter;
import top.caoxuan.comprehensiveapplication.data.bean.GroupChatRecord;
import top.caoxuan.comprehensiveapplication.data.bean.IContent;
import top.caoxuan.comprehensiveapplication.data.bean.IMessage;
import top.caoxuan.comprehensiveapplication.listener.ChatGroupUIListener;
import top.caoxuan.comprehensiveapplication.listener.CoreListener;
import top.caoxuan.comprehensiveapplication.service.CoreService;
import top.caoxuan.comprehensiveapplication.task.SaveMessageTask;

public class ChatGroupUIActivity extends BaseActivity implements View.OnClickListener {
    EditText inputText;
    private static List<IMessage> iMessageList = new ArrayList<>();
    private static List<IMessage> subIMessageList = new ArrayList<>();
    private RecyclerView msgRecyclerView;
    private MessageAdapter messageAdapter;
    //private MsgAdapter adapter;
    private SharedPreferences pref;
    private static int self;
    private static int token;
    private static int groupId;
    private final static int MAX_MESSAGE = 10;

    ChatGroupUIListener chatGroupUIListener = new ChatGroupUIListener() {
        @Override
        public void onReceive() {
            //明天写
        }
    };

    CoreService.CoreBinder coreBinder;

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            coreBinder = (CoreService.CoreBinder) iBinder;
            //coreBinder.startListening(coreListener);
            coreBinder.startReceiving(coreListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }

    };

    CoreListener coreListener = new CoreListener() {
        @Override
        public void onReceive() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int difference = LitePal.count(GroupChatRecord.class) - iMessageList.size();
                    Log.d("cxDebug", "difference=" + difference);
                    Log.d("cxDebug", "LitePal.count(GroupChatRecord.class)=" + LitePal.count(GroupChatRecord.class));
                    Log.d("cxDebug", "iMessageList.size()=" + iMessageList.size());
                    //这个值应该不会小于0，因为iMessageList就是在initMessageList()得到的
                    if (difference == 1) {
                        Log.d("cxDebug", "相差=" + difference);
                        GroupChatRecord groupChatRecord = LitePal.findLast(GroupChatRecord.class);
                        IMessage iMessage;
                        if (groupChatRecord.getUid() == self) {
                            iMessage = new IMessage(ACTION_MESSAGE_SEND, groupChatRecord.getMessage());
                        } else {
                            iMessage = new IMessage(ACTION_MESSAGE_RECEIVE, groupChatRecord.getMessage());
                        }
                        iMessageList.add(iMessage);
                        draw(iMessage);
                    } else if (difference > 1) {
                        List<GroupChatRecord> groupChatRecordList;
                        GroupChatRecord groupChatRecord;
                        groupChatRecordList = LitePal.findAll(GroupChatRecord.class);
                        for (int i = groupChatRecordList.size() - difference; i < groupChatRecordList.size(); i++) {
                            IMessage iMessage;
                            groupChatRecord = groupChatRecordList.get(i);
                            if (groupChatRecord.getUid() == self) {
                                iMessage = new IMessage(ACTION_MESSAGE_SEND, groupChatRecord.getMessage());
                            } else {
                                iMessage = new IMessage(ACTION_MESSAGE_RECEIVE, groupChatRecord.getMessage());
                            }
                            iMessageList.add(iMessage);
                            draw(iMessage);
                        }

                    }


                }
            });
        }

        @Override
        public void onSend() {

        }

        @Override
        public void onSyncMessageList() {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String spName = getSharedPreferences("UserProfile", MODE_PRIVATE).getString("Default", "");
        pref = getSharedPreferences(spName, MODE_PRIVATE);
        self = pref.getInt("Self", 0);
        token = pref.getInt("Token", 0);
        groupId = getIntent().getIntExtra("GroupId", 0);
        LitePal.initialize(this.getApplicationContext());
        setContentView(R.layout.activity_chat_group_ui);
        inputText = findViewById(R.id.input_text);
        Button send = findViewById(R.id.send);
        msgRecyclerView = findViewById(R.id.msg_recycler_view);
        initMessageList();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        messageAdapter = new MessageAdapter(subIMessageList, pref);
        msgRecyclerView.setAdapter(messageAdapter);


        /**绑定CoreService*/
        Intent intent = new Intent(this, CoreService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
        send.setOnClickListener(this);

    }

    private void initMessageList() {
        Cursor cursor = LitePal.findBySQL("select count(*)  from sqlite_master where type='table' and name = 'groupchatrecord';");
        boolean f = cursor.moveToFirst();
        int count = cursor.getInt(0);
        Log.d("cxDebug", "f=" + f);

        Log.d("cxDebug", "count=" + count);
        if (count > 0) {
            List<GroupChatRecord> groupChatRecordList = LitePal.findAll(GroupChatRecord.class);
            IMessage iMessage;
            for (GroupChatRecord groupChatRecord : groupChatRecordList) {
                if (groupChatRecord.getGroupId() == 1) {
                    if (groupChatRecord.getUid() == self) {
                        iMessage = new IMessage(ACTION_MESSAGE_SEND, groupChatRecord.getMessage());
                    } else {
                        Log.d("cxDebug", "发送者uid为：" + groupChatRecord.getUid());
                        iMessage = new IMessage(ACTION_MESSAGE_RECEIVE, groupChatRecord.getMessage());
                    }
                    iMessageList.add(iMessage);
                }
            }
            //这个if根本不会执行，因为如果iMessageList为空，就说明表为空。
            // 而表是用save()建立的，不可能为空。
            //除非表不存在，没有创建表
            if (iMessageList.isEmpty()) {

                Log.d("cxebug", "iMessageList为空");
            } else if (iMessageList.size() <= MAX_MESSAGE) {
                Log.d("cxebug", "iMessageList=" + iMessageList.size());

                subIMessageList = iMessageList;
            } else {
                Log.d("cxebug", "iMessageList=" + iMessageList.size());

                for (int i = iMessageList.size() - MAX_MESSAGE; i < iMessageList.size(); i++) {
                    subIMessageList.add(iMessageList.get(i));
                }
            }
            msgRecyclerView.scrollToPosition(subIMessageList.size() - 1);

        } else {
            GroupChatRecord groupChatRecord = new GroupChatRecord(10000, 1, "系统消息：欢迎！");
            Log.d("cxDebug", "系统消息");
            groupChatRecord.save();
        }
       /* LitePal.findAllAsync(GroupChatRecord.class).listen(new FindMultiCallback<GroupChatRecord>() {
            @Override
            public void onFinish(List<GroupChatRecord> list) {
                IMessage iMessage;
                for (GroupChatRecord groupChatRecord : list){
                    if (groupChatRecord.getGroupId()==1){
                        if (groupChatRecord.getUid() == self) {
                            iMessage = new IMessage(ACTION_MESSAGE_SEND,groupChatRecord.getMessage());
                        } else {
                            Log.d("cxDebug","发送者uid为：" + groupChatRecord.getUid());
                            iMessage = new IMessage(ACTION_MESSAGE_RECEIVE,groupChatRecord.getMessage());
                        }
                        iMessageList.add(iMessage);
                    }
                }
                if (iMessageList.isEmpty()) {
                    Log.d("cxebug", "iMessageList为空");
                }
                else if (iMessageList.size() <= MAX_MESSAGE) {
                    Log.d("cxebug", "iMessageList="+iMessageList.size());

                    subIMessageList = iMessageList;
                }else {
                    Log.d("cxebug", "iMessageList="+iMessageList.size());

                    for (int i = iMessageList.size()-MAX_MESSAGE; i < iMessageList.size(); i++) {
                        subIMessageList.add(iMessageList.get(i));
                    }
                }
            }
        });*/

    }

    synchronized void draw(IMessage IMessage) {
        subIMessageList.add(IMessage);
        //先插入
        messageAdapter.notifyItemInserted(subIMessageList.size() - 1);
        Log.d("cxDebug", "插入成功");
        //再判断是否滚动
        if (IMessage.getAction() == ACTION_MESSAGE_RECEIVE) {
            //获取可见的最后一个view
            View lastChildView = msgRecyclerView.getChildAt(
                    msgRecyclerView.getChildCount() - 1);
            //获取可见的最后一个view的位置
            int lastChildViewPosition = msgRecyclerView.getChildAdapterPosition(lastChildView);
            if (!(subIMessageList.size() - 1 - 1 - lastChildViewPosition >= 1))
                msgRecyclerView.scrollToPosition(subIMessageList.size() - 1);
        } else if (IMessage.getAction() == ACTION_MESSAGE_SEND) {
            msgRecyclerView.scrollToPosition(subIMessageList.size() - 1);
        }

    }
    /*synchronized void draw(List<IMessage> subIMessageList) {
        for ()

    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                toSend();
                break;
            default:
                break;
        }


    }

    private void toSend() {//发送前的准备
        String message = inputText.getText().toString();
        if (!"".equals(message)) {
            IMessage IMessage = new IMessage(ACTION_MESSAGE_SEND, message);
            IContent iContent = new IContent(self, 1, message, token);
            Log.d("cxDebug", "准备更新UI");
            inputText.setText("");
            insertItem(IMessage);
            SaveMessageTask.save(iContent);
            iMessageList.add(IMessage);
            sendMessage(iContent);
        } else {
            Toast.makeText(this, "未输入内容", Toast.LENGTH_SHORT).show();
        }

    }

    private void insertItem(final IMessage IMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("cxDebug", "准备draw");
                draw(IMessage);
            }
        });
    }

    private void sendMessage(IContent iContent) {
        String message = iContent.getMessage();
        if (!"".equals(message)) {
            coreBinder.startSending(iContent);
        }
    }
}
