package com.example.comprehensiveapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MyLoginActivity extends AppCompatActivity {

    EditText editAccount;
    EditText editPassword;
    Button signIn;
    Socket socket = SingleSocket.getSocket();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_login);
        editAccount = findViewById(R.id.account);
        editPassword = findViewById(R.id.password);
        signIn = findViewById(R.id.login);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String account = editAccount.getText().toString();
                final String password = editPassword.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            StringBuilder data = new StringBuilder();
                            data.append("requestType=1");
                            data.append("&");
                            data.append("account=" + account);
                            data.append("&");
                            data.append("password=" + password);
                            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                            out.writeUTF(data.toString());
                            String returnData = new DataInputStream(socket.getInputStream()).readUTF();
                            Log.d("cxdebug", "data" + returnData);
                            if ("1".equals(new AnalyseReturnData().opt(returnData, "requestType")) && "1".equals(new AnalyseReturnData().opt(returnData, "result"))) {
                                if (getIntent().getBooleanExtra("isLoggined", false))
                                    startActivity(new Intent(MyLoginActivity.this, MainActivity.class));
                                finish();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
}
