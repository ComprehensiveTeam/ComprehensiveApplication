package top.caoxuan.comprehensiveapplication.utils;

import android.util.Log;

import top.caoxuan.comprehensiveapplication.data.bean.Output;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONUtil {
    public static List<Output> parseWithJSONObject(String json) {
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
            Log.d("cxDebug", "未封装的code:" + "" + apkDataJSONObject.optLong("versionCode"));
            Log.d("cxDebug", "apkData.get:" + "" + apkData.getVersionCode());
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
            Log.d("cxDebug", "out.get.get:" + "" + output.getApkData().getVersionCode());
            //封装成OutputList对象
            outputList.add(output);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return outputList;
    }
}
