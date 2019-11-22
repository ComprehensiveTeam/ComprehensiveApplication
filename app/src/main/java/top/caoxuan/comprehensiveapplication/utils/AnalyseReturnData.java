package top.caoxuan.comprehensiveapplication.utils;


public class AnalyseReturnData {
    public static String opt(String returnData, String key) {
        return findInCp(detachData(returnData), key);
    }

    private static String[] detachData(String returnData) {
        String[] cp = returnData.split("&");

        return cp;
    }

    private static String findInCp(String[] cp, String key) {
        String value = "";
        for (int i = 0; i < cp.length; i++) {

            if (key.equals(cp[i].split("=")[0])) value = cp[i].split("=")[1];
        }
        return value;
    }


}