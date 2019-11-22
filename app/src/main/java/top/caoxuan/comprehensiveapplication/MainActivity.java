package top.caoxuan.comprehensiveapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.litepal.LitePal;

import top.caoxuan.comprehensiveapplication.data.bean.GroupChatRecord;
import top.caoxuan.comprehensiveapplication.listener.CoreListener;
import top.caoxuan.comprehensiveapplication.service.CoreService;
import top.caoxuan.comprehensiveapplication.version.Update;

public class MainActivity extends BaseActivity {

    CoreService.CoreBinder coreBinder;

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            coreBinder = (CoreService.CoreBinder) iBinder;
            SyncMessageList(LitePal.count(GroupChatRecord.class));

            //coreBinder.startReceiving();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }

    };
    CoreListener coreListener = new CoreListener() {
        @Override
        public void onReceive() {

        }

        @Override
        public void onSend() {

        }

        @Override
        public void onSyncMessageList() {

        }

    };
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private int uid;
    private AppBarConfiguration mAppBarConfiguration;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String spName = getSharedPreferences("UserProfile", MODE_PRIVATE).getString("Default", "");
        pref = getSharedPreferences(spName, MODE_PRIVATE);
        //判断是否登录
        if (!pref.getBoolean("LoginStatus", false)) {
            Intent intent1 = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent1);
            finish();
        }
        LitePal.initialize(this.getApplicationContext());
        setContentView(R.layout.activity_main);
        uid = pref.getInt("Uid", 0);
        Log.d("cxDebug", "Main onCreate");
        /**获取昵称*/
        if ("".equals(pref.getString("Nickname", ""))) {
            editor = pref.edit();
            editor.putString("Nickname", pref.getString("Account", ""));
            editor.apply();
        }
        /**创建下载通知频道，并开启服务************************************************************/

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            String channelId = "download";
            String channelName = "下载";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);

        }
        /**end*************************************************************************************/
        /**碎片布局开始*/
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
                R.id.nav_tools, R.id.nav_share, R.id.nav_send, R.id.nav_setting)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        /**碎片布局结束*/

        /**绑定CoreService*/
        Intent intent = new Intent(this, CoreService.class);
        startService(intent);
        bindService(intent, connection, BIND_AUTO_CREATE);


        //通过广播发送更新提示
        new Update().checkVersion(this);


    }

    private void SyncMessageList(int cursor) {
        coreBinder.startSyncMessageList(coreListener, cursor);
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


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {

        NotificationChannel channel;

        channel = new NotificationChannel(channelId, channelName, importance);

        //channel.setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Bounce.ogg")), Notification.AUDIO_ATTRIBUTES_DEFAULT);
        //channel.setVibrationPattern(new long[]{0, 1000, 1000, 1000});
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
