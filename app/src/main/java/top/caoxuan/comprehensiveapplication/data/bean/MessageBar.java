package top.caoxuan.comprehensiveapplication.data.bean;

public class MessageBar {
    private int viewType;
    private int viewId;

    private int profilePhotoId;
    private String avatar;
    private String messageOutline;
    private String receivedTime;
    private int messageNum;

    public MessageBar(int profilePhotoId, String avatar, String messageOutline, String receivedTime, int messageNum) {
        this.profilePhotoId = profilePhotoId;
        this.avatar = avatar;
        this.messageOutline = messageOutline;
        this.receivedTime = receivedTime;
        this.messageNum = messageNum;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public int getViewId() {
        return viewId;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getProfilePhotoId() {
        return profilePhotoId;
    }

    public void setProfilePhotoId(int profilePhotoId) {
        this.profilePhotoId = profilePhotoId;
    }

    public String getMessageOutline() {
        return messageOutline;
    }

    public void setMessageOutline(String messageOutline) {
        this.messageOutline = messageOutline;
    }

    public String getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(String receivedTime) {
        this.receivedTime = receivedTime;
    }

    public int getMessageNum() {
        return messageNum;
    }

    public void setMessageNum(int messageNum) {
        this.messageNum = messageNum;
    }
}
