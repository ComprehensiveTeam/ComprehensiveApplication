package com.example.comprehensiveapplication.Utils;

public class RequestStringsUtils {
    StringBuilder requestCouple = new StringBuilder("");

    public void addRequestCouple(String key, String value) {
        if (!"".equals(requestCouple.toString())) requestCouple.append("&");
        requestCouple.append(key + "=" + value);
    }

    private void addRequestCouple(Object key, Object value) {
        if (!"".equals(requestCouple.toString())) requestCouple.append("&");
        requestCouple.append(key + "=" + value);
    }

    public String getRequestCouples() {
        return requestCouple.toString();
    }

    public void setRequestType(int value) {
        addRequestCouple("requestType", value);
    }

    public void setAccountType(int value) {
        addRequestCouple("accountType", value);
    }

    public void setAccount(String value) {
        addRequestCouple("account", value);
    }

    public void setPassword(String value) {
        addRequestCouple("password", value);
    }

    public void setResult(int value) {
        addRequestCouple("result", value);
    }

    public void setMsg(String value) {
        addRequestCouple("message", value);
    }


    public static void main(String[] args) {
        RequestStringsUtils rst = new RequestStringsUtils();
        rst.addRequestCouple("requestType", 1);
        rst.addRequestCouple("result", 2);
        System.out.println(rst.getRequestCouples());
    }
}

