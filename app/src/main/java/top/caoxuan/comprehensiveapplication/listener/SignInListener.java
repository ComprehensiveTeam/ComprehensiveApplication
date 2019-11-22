package top.caoxuan.comprehensiveapplication.listener;

import top.caoxuan.comprehensiveapplication.data.bean.IError;

public interface SignInListener {
    void onSuccess(int self, int token);

    void onFailed(IError iError);
}
