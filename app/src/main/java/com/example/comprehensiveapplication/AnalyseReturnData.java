package com.example.comprehensiveapplication;


public class AnalyseReturnData {
    public String opt(String returnData, String key) {
        String value = findInCp(detachData(returnData), key);
        return value;
    }

    public String[] detachData(String returnData) {
        String[] cp = returnData.split("&");

        return cp;
    }

    public String findInCp(String[] cp, String key) {
        String value = "";
        for (int i = 0; i < cp.length; i++) {

            if (key.equals(cp[i].split("=")[0])) value = cp[i].split("=")[1];
        }
        return value;
    }


}