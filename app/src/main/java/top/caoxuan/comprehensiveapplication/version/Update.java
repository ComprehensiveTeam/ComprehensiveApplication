package top.caoxuan.comprehensiveapplication.version;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import top.caoxuan.comprehensiveapplication.utils.JSONUtil;
import top.caoxuan.comprehensiveapplication.data.bean.Output;
import top.caoxuan.comprehensiveapplication.service.DownloadIntentService;

public class Update {
    //private DownloadService.DownloadBinder downloadBinder;
    private DownloadIntentService.DownloadBinder downloadBinder;
    private Activity activity;
    //private Context context;

  /*  public void checkVersion(final Activity activity, DownloadService.DownloadBinder downloadBinder) {
        this.activity = activity;
        Log.d("cxDebug", "dt=" + downloadBinder);
        this.downloadBinder = downloadBinder;
        new Thread(new Runnable() {
            @Override
            public void run() {
                PackageManager packageManager = activity.getPackageManager();
                PackageInfo packageInfo;
                long currentVersionCode;
                long remoteVersionCode = getRemoteVersionInfo().get(0).getApkData().getVersionCode();
                try {
                    packageInfo = packageManager.getPackageInfo(activity.getPackageName(), 0);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                        currentVersionCode = packageInfo.getLongVersionCode();
                        Log.d("cxDebug", "long");

                    } else {
                        currentVersionCode = packageInfo.versionCode;
                        Log.d("cxDebug", "int");

                    }
                    if (remoteVersionCode > currentVersionCode) {
                        Log.d("cxDebug", "update");
                        updateAlertDialog();

                    }
                    Log.d("cxDebug", "currentVersionCode:" + currentVersionCode);
                    Log.d("cxDebug", "remoteVersionCode:" + remoteVersionCode);

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }*/

    public void checkVersion(final Activity activity) {
        this.activity = activity;
        /*Log.d("cxDebug", "dt=" + downloadBinder);
        this.downloadBinder = downloadBinder;*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                PackageManager packageManager = activity.getPackageManager();
                PackageInfo packageInfo;
                long currentVersionCode;
                long remoteVersionCode = getRemoteVersionInfo().get(0).getApkData().getVersionCode();
                try {
                    packageInfo = packageManager.getPackageInfo(activity.getPackageName(), 0);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                        currentVersionCode = packageInfo.getLongVersionCode();
                        Log.d("cxDebug", "long");

                    } else {
                        currentVersionCode = packageInfo.versionCode;
                        Log.d("cxDebug", "int");

                    }
                    if (remoteVersionCode > currentVersionCode) {
                        Log.d("cxDebug", "update");
                        //Intent intent = new Intent("top.caoxuan.comprehensiveapplication.UPDATE");
                        updateAlertDialog();

                    }
                    Log.d("cxDebug", "currentVersionCode:" + currentVersionCode);
                    Log.d("cxDebug", "remoteVersionCode:" + remoteVersionCode);

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
                    .url("https://app.caoxuan.top:10443/app/outputs/apk/release/output.json")
                    .build();
            Response response = client.newCall(request).execute();
            String responseData = response.body().string();
            if (responseData != null) {
                Log.d("cxDebug", "rd:" + responseData);
                outputList = JSONUtil.parseWithJSONObject(responseData);
            }
            Output output = outputList.get(0);//调试1
            Log.d("cxDebug", "code:" + output.getApkData().getVersionCode());//调试1
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputList;
    }

    private void updateAlertDialog() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                dialog.setTitle("版本更新");
                dialog.setMessage("检测到新版本\n是否更新？");
                dialog.setCancelable(true);
                dialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(activity, DownloadIntentService.class);
                        activity.startService(intent);
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

}
