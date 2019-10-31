package com.example.comprehensiveapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprehensiveapplication.adapter.MsgAdapter;
import com.example.comprehensiveapplication.data.bean.Msg;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static com.example.comprehensiveapplication.data.bean.Msg.TYPE_RECEIVED;

public class ChatUIActivity extends AppCompatActivity {
    Socket socket;
    int r = 0;
    int d = 0;
    /*class CreateSocket{
        private Socket socket = null;

        public Socket get(){
            if(socket==null){
                try {
                    socket = new Socket("100.64.240.76",10443);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return socket;
        }
    }*/
    private List<Msg> msgList = new ArrayList<>();
    EditText inputText;
    private Button send;
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;
    final static int messageTimeDelay = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_ui);

        /*for (int i = 0;i<msgList.size();i++){
            Log.d("cxdebug","Content:"+msgList.get(i).getContent()+"\ttype:"+msgList.get(i).getType());
        }*/
        inputText = findViewById(R.id.input_text);
        send = findViewById(R.id.send);
        msgRecyclerView = findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
        final Receiver receiver = new Receiver();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    socket = SingleSocket.getSocket();
                    if (receiver.receive(socket)) {
                        Log.d("cxdebug", "receiving" + (++r));
                        try {
                            Thread.sleep(messageTimeDelay);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Msg msg = new Msg(receiver.msg, TYPE_RECEIVED);
                        msgList.add(msg);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Log.d("cxdebug", "drawing" + (++d));
                                /*adapter.notifyItemInserted(msgList.size() - 1);
                                msgRecyclerView.scrollToPosition(msgList.size() - 1);*/
                                draw();

                            }
                        });

                    }
                }

            }
        }).start();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content = inputText.getText().toString();
                if (!"".equals(content)) {
                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size() - 1);
                    msgRecyclerView.scrollToPosition(msgList.size() - 1);
                    inputText.setText("");

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Socket socket = SingleSocket.getSocket();
                            Sender s = new Sender(socket, content, "account");
                            s.sendMessage();
                        }
                    }).start();
                } else {
                    Toast.makeText(ChatUIActivity.this, "未输入内容", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private synchronized void draw() {
        adapter.notifyItemInserted(msgList.size() - 1);
        msgRecyclerView.scrollToPosition(msgList.size() - 1);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Sender sender = new Sender(SingleSocket.getSocket(), "exit", "1");
                sender.sendMessage();
            }
        }).start();

    }
}

