package com.example.comprehensiveapplication.type;

public interface IType {

    //系统消息 系统->用户
    //send: requestType=0&account=1&msg=xxx
    //receive: requestType=0&account=username&msg=xxx
    String TYPE_SYSTEM = "0";

    //登录 用户->系统
    //send: requestType=1&account=username&password=pass
    //receive: requestType=1&msg=true/false
    String TYPE_LOGIN = "1";

    //注册 用户->系统
    //send: requestType=2&account=username&password=pass
    //receive: requestType=2&msg=true/false
    String TYPE_REGISTER = "2";

    //转发 用户->用户/用户群
    //根据转发目标判断是否需要群发
    //send: requestType=3&account=username&receiver=username/群ID&msg=xxx
    //receive: requestType=3&account=username&receiver=username/群ID&msg=xxx
    String TYPE_RELAY = "3";


    //消息对应的解析词
    //消息类型
    String TYPE = "requestType";

    //密码
    String PASSWORD = "password";

    //用户ID
    String ACCOUNT = "account";

    //接受用户
    String RECEIVE = "receive";

    //消息主体
    String MSG = "msg";

    //错误
    String ERR = "error";

    //返回类型
    String RESULT = "result";

    /**登录、注册、*/
    /*
    例：
    public String toLogin(){
        return TYPE+"="+type+"&"+MSG+"="+msg;
    }
     */
}
