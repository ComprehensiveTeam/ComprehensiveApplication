package top.caoxuan.comprehensiveapplication.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import top.caoxuan.comprehensiveapplication.SingleSocket;

@Deprecated
public class Uid {
    /*int getUidWithPhone(String phone){
        return getUid(phone);
    }
    int getUidWithEmail(String Email){
        return getUid(phone);
    }
    int getUidWithUserName(String UserName){
        return getUid(phone);
    }*/
    public static int getUid(String account) {
        try {
            RequestStringsUtils requestStringsUtils = new RequestStringsUtils();
            requestStringsUtils.setRequestType(6);
            requestStringsUtils.setAccount(account);
            DataOutputStream dataOutputStream = new DataOutputStream(SingleSocket.getSocket().getOutputStream());
            dataOutputStream.writeUTF(requestStringsUtils.getRequestCouples());

            DataInputStream dataInputStream = new DataInputStream(SingleSocket.getSocket().getInputStream());
            String returnData = dataInputStream.readUTF(dataInputStream);
            String requestType = AnalyseReturnData.opt(returnData, "requestType");
            String uid = AnalyseReturnData.opt(returnData, "uid");
            if ("6".equals(requestType)) {
                return Integer.parseInt(uid);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
