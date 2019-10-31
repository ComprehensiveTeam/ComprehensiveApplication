package com.example.comprehensiveapplication;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.example.comprehensiveapplication.data.bean.Output;
import com.example.comprehensiveapplication.download.DownloadService;
import com.example.comprehensiveapplication.ui.login.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.IBinder;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    DownloadService.DownloadBinder downloadBinder;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            downloadBinder = (DownloadService.DownloadBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("cxdebug", "Main onCreate");
        /*创建下载通知频道，并开启服务*************************************************************/

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            String channelId = "download";
            String channelName = "下载";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);

        }
        Intent intent = new Intent(MainActivity.this, DownloadService.class);
        startService(intent);
        bindService(intent, connection, BIND_AUTO_CREATE);
        /*end**************************************************************************************/
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        checkPermission();
        //startActivity(new Intent(MainActivity.this, LoginActivity.class));
        //checkVersion();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void checkVersion() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PackageManager packageManager = getPackageManager();
                PackageInfo packageInfo;
                long currentVersionCode;
                long remoteVersionCode = getRemoteVersionInfo().get(0).getApkData().getVersionCode();
                try {
                    packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                        currentVersionCode = packageInfo.getLongVersionCode();
                        Log.d("cxdebug", "long");

                    } else {
                        currentVersionCode = packageInfo.versionCode;
                        Log.d("cxdebug", "int");

                    }
                    if (remoteVersionCode > currentVersionCode) {
                        Log.d("cxdebug", "update");
                        updateAlertDialog();

                    }
                    Log.d("cxdebug", "currentVersionCode:" + currentVersionCode);
                    Log.d("cxdebug", "remoteVersionCode:" + remoteVersionCode);

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private List<Output> getRemoteVersionInfo() {
        List<Output> outputList = null;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://app.caoxuan.top:10443/app/output.json")
                    .build();
            Response response = client.newCall(request).execute();
            String responseData = response.body().string();
            if (responseData != null) {
                Log.d("cxdebug", "rd:" + responseData);
                outputList = parseWithJSONObject(responseData);
            }
            Output output = outputList.get(0);//调试1
            Log.d("cxdebug", "code:" + output.getApkData().getVersionCode());//调试1
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputList;
    }

    private List<Output> parseWithJSONObject(String json) {
        List<Output> outputList = new ArrayList<>();

        Output output = new Output();
        try {
            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            /*解析其他*//*
            //第一个对象
            JSONObject outputType = jsonObject.getJSONObject("outputType");
            String type = outputType.optString("type");
            //第二个对象
            String type2 = apkData.optString("type");
            String splits = apkData.optString("splits");
            String versionName = apkData.optString("versionName");
            String enabled = apkData.optString("enabled");
            String outputFile = apkData.optString("outputFile");
            String fullName = apkData.optString("fullName");
            String baseName = apkData.optString("baseName");
            //第三个对象和第四个对象
            String path = jsonObject.optString("path");
            String properties = jsonObject.optString("properties");*/
            //封装---------------------------------------------------------------
            //封装第一个内部类对象OutputType
            Output.OutputType outputType = output.new OutputType();
            JSONObject outputTypeJSONObject = jsonObject.getJSONObject("outputType");
            outputType.setType(outputTypeJSONObject.optString("type"));
            //封装第二个内部类对象ApkData
            Output.ApkData apkData = output.new ApkData();
            JSONObject apkDataJSONObject = jsonObject.getJSONObject("apkData");
            apkData.setType(apkDataJSONObject.optString("type"));
            apkData.setSplits(apkDataJSONObject.optString("splits"));
            apkData.setVersionCode(apkDataJSONObject.optLong("versionCode"));
            Log.d("cxdebug","未封装的code:"+""+apkDataJSONObject.optLong("versionCode"));
            Log.d("cxdebug","apkData.get:"+""+apkData.getVersionCode());
            apkData.setVersionName(apkDataJSONObject.optString("versionName"));
            apkData.setEnabled(apkDataJSONObject.optBoolean("enabled"));
            apkData.setFullName(apkDataJSONObject.optString("fullName"));
            apkData.setBaseName(apkDataJSONObject.optString("baseName"));
            //封装三、四（值类型）
            output.setPath(jsonObject.optString("path"));
            output.setProperties(jsonObject.optString("properties"));
            //封装Output对象（把除了值类型的另外两个对象OutputType和ApkData封装）
            output.setOutputType(outputType);
            output.setApkData(apkData);
            Log.d("cxdebug","out.get.get:"+""+ output.getApkData().getVersionCode());
            //封装成OutputList对象
            outputList.add(output);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return outputList;
    }

    /*private Version parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();
        Version version = gson.fromJson(jsonData, Version.class);
        return version;
    }*/

    private void updateAlertDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("版本更新");
                dialog.setMessage("检测到新版本\n是否更新？");
                dialog.setCancelable(true);
                dialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = "https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk";
                        if (downloadBinder == null) return;
                        downloadBinder.startDownload(url);

                    }
                });
                dialog.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {

        NotificationChannel channel ;

            channel = new NotificationChannel(channelId, channelName, importance);

        //channel.setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Bounce.ogg")), Notification.AUDIO_ATTRIBUTES_DEFAULT);
        //channel.setVibrationPattern(new long[]{0, 1000, 1000, 1000});
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);

    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }
}
