package top.caoxuan.comprehensiveapplication.utils;

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

    public void setRequestType(int requestType) {
        addRequestCouple("requestType", requestType);
    }

    public void setAccountType(int accountType) {
        addRequestCouple("accountType", accountType);
    }

    public void setUid(int uid) {
        addRequestCouple("uid", uid);
    }

    public void setReceiver(int receiver) {
        addRequestCouple("receiver", receiver);
    }

    public void setAccount(String account) {
        addRequestCouple("account", account);
    }

    public void setPassword(String password) {
        addRequestCouple("password", password);
    }

    public void setResult(int result) {
        addRequestCouple("result", result);
    }

    public void setMessage(String message) {
        addRequestCouple("message", message);
    }

    public void setCursor(int cursor) {
        addRequestCouple("cursor", cursor);
    }
    /*public static void main(String[] args) {
        RequestStringsUtils rst = new RequestStringsUtils();
        rst.addRequestCouple("requestType", 1);
        rst.addRequestCouple("result", 2);
        System.out.println(rst.getRequestCouples());
    }*/
}

