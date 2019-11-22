package top.caoxuan.comprehensiveapplication.task;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import top.caoxuan.comprehensiveapplication.SingleSocket;
import top.caoxuan.comprehensiveapplication.data.bean.IContent;
import top.caoxuan.comprehensiveapplication.listener.MessageListener;
import top.caoxuan.comprehensiveapplication.utils.RequestStringsUtils;

/**
 * SendMessageTask会先将数据存库，再用socket发送
 */
public class SendMessageTask extends AsyncTask<Object, Integer, Boolean> implements BaseTask {


    private MessageListener messageListener;

    public SendMessageTask(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("cxDebug", "onPreExecute");
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Log.d("cxDebug", "onCancelled");

    }

    @Override
    protected void onCancelled(Boolean aBoolean) {
        super.onCancelled(aBoolean);
        Log.d("cxDebug", "onCancelled2");

    }

    @Override
    protected Boolean doInBackground(Object[] objects) {
        Log.d("cxDebug", "doInBackground");
        boolean sendResult = false;
        IContent iContent = (IContent) objects[0];
        //SaveMessageTask.save(iContent,messageListener);
        //SaveMessageTask.save(iContent);//在发送后直接保存
        ISender iSender = new ISender();
        sendResult = iSender.send(iContent);
        return sendResult;
    }

    @Override
    protected void onPostExecute(Boolean sendResult) {
        messageListener.onSend();
    }

    private class ISender {
        boolean send(IContent iContent) {

            try {
                Socket socket = SingleSocket.getSocket();
                /**暂时弃用，改用HashMap*/
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
           /* while ((len = in.read(bytes)) != -1) {
                //注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
                sb.append(new String(bytes, 0, len,"UTF-8"));
            }*/
                RequestStringsUtils rsu = new RequestStringsUtils();
                rsu.setRequestType(TYPE_RELAY);
                rsu.setUid(iContent.getUid());
                rsu.setReceiver(iContent.getReceiver());
                rsu.setMessage(iContent.getMessage());
                dataOutputStream.writeUTF(rsu.getRequestCouples());
                /*HashMap<String ,Object> map = new HashMap<>();
                map.put(REQUEST_TYPE,TYPE_RELAY);
                map.put(UID,iContent.getUid());
                map.put(RECEIVER,iContent);
                map.put(MESSAGE,iContent.getMessage());
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(map);*/
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}
