package top.caoxuan.comprehensiveapplication.data.bean;

import org.litepal.crud.LitePalSupport;

public class GroupChatRecord extends LitePalSupport {

    private int uid;
    private int groupId;
    private String message;

    public GroupChatRecord(int uid, int groupId, String message) {
        this.uid = uid;
        this.groupId = groupId;
        this.message = message;
    }

    public GroupChatRecord(IContent iContent) {
        this.uid = iContent.getUid();
        this.groupId = iContent.getReceiver();
        this.message = iContent.getMessage();
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
