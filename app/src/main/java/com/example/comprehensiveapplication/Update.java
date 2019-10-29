/*
package com.example.comprehensiveapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.example.comprehensiveapplication.data.bean.Output;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Update {

    protected void checkVersion(final MainActivity activity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PackageManager packageManager = activity.this.getPackageManager();
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
            */
/*解析其他*//*
*/
/*
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
            String properties = jsonObject.optString("properties");*//*

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
}
*/
