package top.caoxuan.comprehensiveapplication.data.bean;

@Deprecated
public class Msg {

    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SENT = 1;
    private String message;
    private int type;
    private String account;
    private int uid;
    private int groupId;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Deprecated
    public Msg(String message, int type) {
        this.message = message;
        this.type = type;
    }

    public Msg(String message, int type, int uid) {
        this.message = message;
        this.type = type;
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public int getType() {
        return type;
    }
}
