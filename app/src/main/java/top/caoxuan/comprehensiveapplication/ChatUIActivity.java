/*
package top.caoxuan.comprehensiveapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import top.caoxuan.comprehensiveapplication.adapter.MsgAdapter;
import top.caoxuan.comprehensiveapplication.data.bean.Msg;

@Deprecated
public class ChatUIActivity extends BaseActivity {
    EditText inputText;
    private List<Msg> msgList = new ArrayList<>();
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;
    private SharedPreferences pref;
    static int self;
    static int uid;
    static int chatId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        */
/*super.onCreate(savedInstanceState);
        pref = getSharedPreferences("UserProfile", MODE_PRIVATE);
        self = pref.getInt("self", 0);
        groupId = getIntent().getIntExtra("groupId",0);
        LitePal.initialize(this.getApplicationContext());
        //LitePal.getDatabase();
        setContentView(R.layout.activity_chat_ui);
        inputText = findViewById(R.id.input_text);
        Button send = findViewById(R.id.send);
        msgRecyclerView = findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ReceiveUtil receiveUtil = new ReceiveUtil();
                while (true) {
                    if (receiveUtil.receive()) {
                        *//*
 */
/*try {
                            Thread.sleep(messageTimeDelay);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*//*
 */
/*
                        //final Msg msg = new Msg(receiveUtil.getMsg(), Msg.TYPE_RECEIVED);
                        final Msg msg = new Msg(receiveUtil.getMessage(), Msg.TYPE_RECEIVED, receiveUtil.getUid());
                        save(uid, msg);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("cxDebug", "msg:" + msg.getMessage());
                                draw(Msg.TYPE_RECEIVED, msg);
                            }
                        });
                    }
                }

            }
        }).start();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message = inputText.getText().toString();
                if (!"".equals(message)) {
                    //final Msg msg = new Msg(message, Msg.TYPE_SENT);
                    final Msg msg = new Msg(message, Msg.TYPE_SENT, selfUid);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            draw(Msg.TYPE_SENT, msg);
                        }
                    });
                    inputText.setText("");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            save(uid, msg);
                            Sender s = new Sender(message, selfUid);
                            s.sendMessage();
                        }
                    }).start();
                } else {
                    Toast.makeText(ChatUIActivity.this, "未输入内容", Toast.LENGTH_SHORT).show();
                }
            }
        });*//*

    }



    synchronized void draw(int type, Msg msg) {
        msgList.add(msg);
        adapter.notifyItemInserted(msgList.size() - 1);
        if (type == Msg.TYPE_SENT) {
            //adapter.notifyItemInserted(msgList.size() - 1);
            msgRecyclerView.scrollToPosition(msgList.size() - 1);
        } else if (type == Msg.TYPE_RECEIVED) {
            //adapter.notifyItemInserted(msgList.size() - 1);
            //获取可见的最后一个view
            View lastChildView = msgRecyclerView.getChildAt(
                    msgRecyclerView.getChildCount() - 1);
            //获取可见的最后一个view的位置
            int lastChildViewPosition = msgRecyclerView.getChildAdapterPosition(lastChildView);
            if (!(msgList.size() - 1 - 1 - lastChildViewPosition >= 1))
                msgRecyclerView.scrollToPosition(msgList.size() - 1);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        */
/*new Thread(new Runnable() {
            @Override
            public void run() {
                //Sender sender = new Sender(SingleSocket.getSocket(), "exit", "1");
                //sender.sendMessage();
            }
        }).start();*//*


    }
}

*/
