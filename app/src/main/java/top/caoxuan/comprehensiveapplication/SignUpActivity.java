package top.caoxuan.comprehensiveapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import top.caoxuan.comprehensiveapplication.data.bean.Registrant;
import top.caoxuan.comprehensiveapplication.listener.SignUpListener;
import top.caoxuan.comprehensiveapplication.task.SignUpTask;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    private EditText phone;
    private EditText phoneVCode;
    private EditText email;
    private EditText emailVCode;
    private CheckBox accept;
    private EditText password;
    private TextView sign_up_by_phone;
    private TextView sign_up_by_email;
    private LinearLayout phone_input_box;
    private LinearLayout email_input_box;
    final static int SIGN_UP = 1;
    final static int SEND = 2;
    final static int SIGN_UP_BY_PHONE = 1;
    final static int SIGN_UP_BY_EMAIL = 2;
    final static int SEND_SMS = 1;
    final static int SEND_EMAIL = 2;
    private static int way = SIGN_UP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        phone = findViewById(R.id.phone);
        phoneVCode = findViewById(R.id.phone_v_code);
        email = findViewById(R.id.email);
        emailVCode = findViewById(R.id.email_v_code);
        accept = findViewById(R.id.accept);
        password = findViewById(R.id.password);
        sign_up_by_phone = findViewById(R.id.sign_up_by_phone);
        sign_up_by_email = findViewById(R.id.sign_up_by_email);
        phone_input_box = findViewById(R.id.phone_input_box);
        email_input_box = findViewById(R.id.email_input_box);
        Button sendEmailVCode = findViewById(R.id.send_email_v_code);
        Button sendSmsVCode = findViewById(R.id.send_sms_v_code);
        Button signUp = findViewById(R.id.sign_up);
        sign_up_by_phone.setOnClickListener(this);
        sign_up_by_email.setOnClickListener(this);
        sendEmailVCode.setOnClickListener(this);
        sendSmsVCode.setOnClickListener(this);
        signUp.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        SignUpTask signUpTask;
        switch (v.getId()) {
            case R.id.sign_up_by_phone:
                way = 1;
                email_input_box.setVisibility(View.INVISIBLE);
                phone_input_box.setVisibility(View.VISIBLE);
                break;
            case R.id.sign_up_by_email:
                way = 2;
                phone_input_box.setVisibility(View.INVISIBLE);
                email_input_box.setVisibility(View.VISIBLE);
                break;
            case R.id.sign_up:
                //findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                if (getCheckBoxStatus()) {
                    signUpTask = new SignUpTask(signUpListener);
                    Registrant registrant = new Registrant(password.getText().toString());
                    if (way == SIGN_UP_BY_PHONE) {
                        registrant.setAccount(phone.getText().toString());
                        registrant.setvCode(phone.getText().toString());
                        signUpTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, SIGN_UP, SIGN_UP_BY_PHONE, new Registrant(phone.getText().toString(), phoneVCode.getText().toString(), password.getText().toString()));
                    } else if (way == SIGN_UP_BY_EMAIL) {
                        registrant.setAccount(email.getText().toString());
                        registrant.setvCode(email.getText().toString());
                        signUpTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, SIGN_UP, SIGN_UP_BY_EMAIL, new Registrant(email.getText().toString(), emailVCode.getText().toString(), password.getText().toString()));
                    }
                }
                break;
            case R.id.send_sms_v_code:
                signUpTask = new SignUpTask(signUpListener);
                signUpTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, SEND, SEND_SMS, phone.getText().toString());
                break;
            case R.id.send_email_v_code:
                signUpTask = new SignUpTask(signUpListener);
                signUpTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, SEND, SEND_EMAIL, email.getText().toString());
                break;
            default:
                break;
        }

    }

    private boolean getCheckBoxStatus() {
        if (!accept.isChecked()) {
            Toast.makeText(SignUpActivity.this, "你必须同意服务协议和隐私政策才能注册", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    SignUpListener signUpListener = new SignUpListener() {
        @Override
        public void onSuccess() {
            findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent);
        }

        @Override
        public void onFailed() {
            findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
            Toast.makeText(SignUpActivity.this, "注册失败", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onSendSuccess() {
            findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
            Toast.makeText(SignUpActivity.this, "发送成功", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onSendFailed() {
            findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
            Toast.makeText(SignUpActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
        }
    };
}
