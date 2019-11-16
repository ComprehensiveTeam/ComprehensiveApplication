package com.example.comprehensiveapplication.Utils;


public class AnalyseReturnData {
    public static String opt(String returnData, String key) {
        String value = findInCp(detachData(returnData), key);
        return value;
    }

    static String[] detachData(String returnData) {
        String[] cp = returnData.split("&");

        return cp;
    }

    static String findInCp(String[] cp, String key) {
        String value = "";
        for (int i = 0; i < cp.length; i++) {

            if (key.equals(cp[i].split("=")[0])) value = cp[i].split("=")[1];
        }
        return value;
    }


}