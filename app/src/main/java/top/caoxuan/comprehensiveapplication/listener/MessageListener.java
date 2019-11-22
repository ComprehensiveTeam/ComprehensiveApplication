package top.caoxuan.comprehensiveapplication.listener;

public interface MessageListener {
    void onReceive();

    void onSend();

    void onSave();

    void onSync();
}
