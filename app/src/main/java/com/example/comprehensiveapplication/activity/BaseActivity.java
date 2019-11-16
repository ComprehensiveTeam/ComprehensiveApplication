package com.example.comprehensiveapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.comprehensiveapplication.ActivityCollector;
import com.example.comprehensiveapplication.BaseClass;
import com.example.comprehensiveapplication.R;
import com.example.comprehensiveapplication.type.IType;

public class BaseActivity extends AppCompatActivity implements BaseClass {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
