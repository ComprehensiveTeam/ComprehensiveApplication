package top.caoxuan.comprehensiveapplication.data.bean;

public class IError {
    private int type;
    private String causeBy;

    public IError(int type, String causeBy) {
        this.type = type;
        this.causeBy = causeBy;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCauseBy() {
        return causeBy;
    }

    public void setCauseBy(String causeBy) {
        this.causeBy = causeBy;
    }
}
