package top.caoxuan.comprehensiveapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import top.caoxuan.comprehensiveapplication.base.BaseInterface;
import top.caoxuan.comprehensiveapplication.utils.ActivityCollector;

public class BaseActivity extends AppCompatActivity implements BaseInterface {

    /*DownloadIntentService.DownloadBinder downloadBinder ;
    UpdateDialogReceiver updateDialogReceiver;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("top.caoxuan.comprehensiveapplication.UPDATE");
        updateDialogReceiver = new UpdateDialogReceiver();
        registerReceiver(updateDialogReceiver,intentFilter);*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
    /*class UpdateDialogReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
            Log.d("cxDebug","收到广播");
            AlertDialog.Builder dialog = new AlertDialog.Builder(BaseActivity.this);
            dialog.setTitle("版本更新");
            dialog.setMessage("检测到新版本\n是否更新？");
            dialog.setCancelable(true);
            dialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {



                }
            });
            dialog.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.show();

        }
    }*/
}
