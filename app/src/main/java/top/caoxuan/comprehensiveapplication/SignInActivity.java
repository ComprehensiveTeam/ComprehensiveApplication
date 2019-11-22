package top.caoxuan.comprehensiveapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import top.caoxuan.comprehensiveapplication.data.bean.IError;
import top.caoxuan.comprehensiveapplication.data.bean.User;
import top.caoxuan.comprehensiveapplication.listener.SignInListener;
import top.caoxuan.comprehensiveapplication.task.AutoSignInTask;
import top.caoxuan.comprehensiveapplication.task.SignInTask;

public class SignInActivity extends BaseActivity {

    private EditText accountEdit;
    private EditText passwordEdit;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox rememberPass;
    SignInTask signInTask;
    SignInListener signInListener = new SignInListener() {
        @SuppressLint("ApplySharedPref")
        @Override
        public void onSuccess(int self, int token) {
            Log.d("cxDebug", "SignInActivity:" + token);
            SharedPreferences defaultSP = getSharedPreferences("UserProfile", MODE_PRIVATE);
            SharedPreferences.Editor defaultEditor = defaultSP.edit();
            defaultEditor.putString("Default", String.valueOf(self));
            defaultEditor.commit();
            pref = getSharedPreferences(defaultSP.getString("Default", ""), MODE_PRIVATE);
            editor = pref.edit();
            editor.putInt("Self", self);
            editor.putInt("Token", token);
            editor.putString("Account", accountEdit.getText().toString());
            editor.putBoolean("LoginStatus", true);
            //editor.putInt("self",);
            if (editor.commit()) {
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                //intent.putExtra("isLoggedIn", true);
                startActivity(intent);
                finish();
                Toast.makeText(SignInActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("cxDebug", "self未保存成功");
            }
        }

        @Override
        public void onFailed(IError iError) {
            Toast.makeText(SignInActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            if (iError.getType() == TYPE_LOGIN) {

            } else if (iError.getType() == TYPE_LOGIN_BY_TOKEN) {
                editor = pref.edit();
                editor.putBoolean("LoginStatus", false);
                editor.apply();
                Toast.makeText(SignInActivity.this, "身份信息失效，请重新登录", Toast.LENGTH_SHORT).show();

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String spName = getSharedPreferences("UserProfile", MODE_PRIVATE).getString("Default", "");
        pref = getSharedPreferences(spName, MODE_PRIVATE);
        //如果已登录过，则直接跳转至MainActivity
        if (pref.getBoolean("LoginStatus", false)) {
            int self = pref.getInt("Self", 0);
            int token = pref.getInt("Token", 0);
            if (!(self == 0 || token == 0)) {//当两者都不为0时
                AutoSignInTask autoSignInTask = new AutoSignInTask(signInListener, self, token);
                autoSignInTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
        setContentView(R.layout.activity_sign_in);
        requestPermission();
        accountEdit = findViewById(R.id.account);
        passwordEdit = findViewById(R.id.password);
        rememberPass = findViewById(R.id.remember);
        TextView signUp = findViewById(R.id.sign_up);
        boolean isRemember = pref.getBoolean("RememberPassword", false);
        Button signIn = findViewById(R.id.sign_in);
        String account = pref.getString("Account", "");
        accountEdit.setText(account);
        if (isRemember) {
            String password = pref.getString("Password", "");
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user = new User(accountEdit.getText().toString(), passwordEdit.getText().toString());
                verify(user);
                editor = pref.edit();
                editor.putString("Account", user.getAccount());
                if (rememberPass.isChecked()) {
                    editor.putBoolean("RememberPassword", true);
                    editor.putString("Password", user.getPassword());
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
        signInTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, account, password, pref);
    }

    /*public void checkPermission() {
        if (ContextCompat.checkSelfPermission(SignInActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SignInActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.LOCATION_HARDWARE}, 1);
        }
    }*/
    private void requestPermission() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(SignInActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(SignInActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(SignInActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(SignInActivity.this, permissions, 1);
        }
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "拒绝该权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "你必须统一所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                } else {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }
}

