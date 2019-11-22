/*
package com.example.comprehensiveapplication.deprecated;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.comprehensiveapplication.R;
import User;
import MainActivity;

public class MyLoginActivity extends AppCompatActivity {

    private EditText accountEdit;
    private EditText passwordEdit;
    private Button login;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox rememberPass;
    */
/*LoginService.LoginBinder loginBinder;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            loginBinder = (LoginService.LoginBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }

    };*//*


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_login);
        pref = getSharedPreferences("loginUser", MODE_PRIVATE);
        accountEdit = findViewById(R.id.account);
        passwordEdit = findViewById(R.id.password);
        rememberPass = findViewById(R.id.remember);
        boolean isRemember = pref.getBoolean("remember_password", false);
        login = findViewById(R.id.login);
        */
/*Intent intent = new Intent(MyLoginActivity.this, LoginService.class);
        startService(intent);
        bindService(intent, connection, BIND_AUTO_CREATE);*//*

        if (isRemember) {
            String account = pref.getString("account", "");
            String password = pref.getString("password", "");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(accountEdit.getText().toString(),passwordEdit.getText().toString());

                loginBinder.verify(user,MyLoginActivity.this);
                editor = pref.edit();
                if (rememberPass.isChecked()) {
                    editor.putBoolean("remember_password", true);
                    editor.putString("account", user.getAccount());
                    editor.putString("password", user.getPassword());
                } else {
                    editor.clear();
                }
                editor.apply();
                Intent intent = new Intent(MyLoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                 */
/*else {
                    Toast.makeText(MyLoginActivity.this, "失败", Toast.LENGTH_SHORT).show();
                }*//*


            }
        });
        */
/*signIn.setOnClickListener(new View.OnClickListener() {
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
                            Log.d("cxDebug", "data" + returnData);
                            if ("1".equals(new AnalyseReturnData().opt(returnData, "request_type")) && "1".equals(new AnalyseReturnData().opt(returnData, "result"))) {
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
        });*//*


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
*/
