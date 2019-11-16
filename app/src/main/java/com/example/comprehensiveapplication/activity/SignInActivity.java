package com.example.comprehensiveapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comprehensiveapplication.R;
import com.example.comprehensiveapplication.data.bean.User;
import com.example.comprehensiveapplication.listener.SignInListener;
import com.example.comprehensiveapplication.task.SignInTask;

public class SignInActivity extends BaseActivity {

    private EditText accountEdit;
    private EditText passwordEdit;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox rememberPass;
    private TextView signUp;
    SignInTask signInTask;
    SignInListener signInListener = new SignInListener() {
        @Override
        public void onSuccess() {
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            intent.putExtra("isLoggedIn", true);
            startActivity(intent);
            finish();
            Toast.makeText(SignInActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed() {
            Toast.makeText(SignInActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
        }
    };
    //DownloadService.DownloadBinder downloadBinder;


    /*ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            loginBinder = (LoginService.LoginBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }

    };*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        pref = getSharedPreferences("loginUser", MODE_PRIVATE);
        accountEdit = findViewById(R.id.account);
        passwordEdit = findViewById(R.id.password);
        rememberPass = findViewById(R.id.remember);
        signUp = findViewById(R.id.sign_up);
        boolean isRemember = pref.getBoolean("remember_password", false);
        Button signIn = findViewById(R.id.sign_in);
        if (isRemember) {
            String account = pref.getString("account", "");
            String password = pref.getString("password", "");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(accountEdit.getText().toString(), passwordEdit.getText().toString());
                verify(user);
                editor = pref.edit();
                if (rememberPass.isChecked()) {
                    editor.putBoolean("remember_password", true);
                    editor.putString("account", user.getAccount());
                    editor.putString("password", user.getPassword());
                } else {
                    editor.clear();
                }
                editor.apply();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSignUp = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(toSignUp);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unbindService(connection);
    }

    public void verify(User user) {
        String account = user.getAccount();
        String password = user.getPassword();
        signInTask = new SignInTask(signInListener);
        signInTask.execute(account, password);
    }
}

