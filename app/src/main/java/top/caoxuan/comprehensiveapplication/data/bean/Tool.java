package top.caoxuan.comprehensiveapplication.data.bean;

public class Tool {
    private String toolName;
    private int toolImageId;

    public int getToolImageId() {
        return toolImageId;
    }

    public void setToolImageId(int toolImageId) {
        this.toolImageId = toolImageId;
    }

    public Tool(String toolName) {
        this.toolName = toolName;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }
}
