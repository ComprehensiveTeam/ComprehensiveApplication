package top.caoxuan.comprehensiveapplication.data.bean;


/**
 * 用于存储和传输的信息
 */
public class IContent {
    int uid;
    int receiver;
    String message;

    /**
     * uid：发送者
     * receiver：接收者
     * message：消息文本
     **/
    public IContent(int uid, int receiver, String message) {
        this.uid = uid;
        this.receiver = receiver;
        this.message = message;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
