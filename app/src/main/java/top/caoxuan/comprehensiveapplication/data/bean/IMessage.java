package top.caoxuan.comprehensiveapplication.data.bean;


/**
 * 用于UI操作的信息
 */
public class IMessage {
    final static int TYPE_RECEIVE = 0;
    final static int TYPE_SEND = 1;
    int action;
    String message;

    public IMessage() {
    }

    public IMessage(String message) {
        this.message = message;
    }

    public IMessage(int action, String message) {
        this.action = action;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
