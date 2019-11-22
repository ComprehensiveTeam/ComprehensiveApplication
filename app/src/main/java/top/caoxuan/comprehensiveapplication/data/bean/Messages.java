package top.caoxuan.comprehensiveapplication.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class Messages implements Serializable {

    private static ArrayList<String> messages = new ArrayList<>();

    public static void add(String message) {
        messages.add(message);
    }

    public static ArrayList<String> getMessages() {
        return messages;
    }
}
