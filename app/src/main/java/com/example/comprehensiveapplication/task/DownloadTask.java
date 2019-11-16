package com.example.comprehensiveapplication.task;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.example.comprehensiveapplication.listener.DownloadListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URI;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class DownloadTask extends AsyncTask<Object, Integer, Integer> {

    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_PAUSED = 2;
    public static final int TYPE_CANCELED = 3;

    private DownloadListener listener;
    private boolean isCanceled = false;
    private boolean isPaused = false;
    private int lastProgress;

    public DownloadTask(DownloadListener listener) {
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(Object... objects) {
        InputStream is = null;
        RandomAccessFile savedFile = null;
        File file = null;
        String para0 = objects[0].toString();
        Log.d("cxdebug", objects[1].toString());
        Context context = (Context) objects[1];
        try {
            long downloadedLength = 0;
            String downloadUrl = para0;
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            //String directory = Environment7y.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/.APP_Debug_Dir").getPath();
            //String directory = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getPath();

            ///Log.d("cxdebug","downloadDir:"+	DIRECTORY_DOWNLOADS);
            //String directory = Environment.getExternalStorageDirectory().getPath() + "/Test01";
            //String directory = "/storage/emulated/0/cxdebug";
            //String directory = "/storage/130B-0E0A/Download";
            //Log.d("cxdebug", "dir:" + directory);
            file = new File(context.getExternalFilesDir(DIRECTORY_DOWNLOADS), fileName);
            Log.d("cxdebug", "下载路径" + file);
            //下面开始判断是否使用断点续传
            if (file.exists()) {//当文件存在时（可能不完整）， 已下载长度=当前文件长度
                downloadedLength = file.length();
            }
            long contentLength = getContentLength(downloadUrl);
            if (contentLength == 0) {
                Log.d("cxdebug", "Content Length = 0");//因为网络文件长度=0，所以URL所对应的文件的无效的，直接返回失败
                return TYPE_FAILED;
            } else if (contentLength == downloadedLength) {
                Log.d("cxdebug", "Content Length = Downloaded Length");//已下载文件长度=网络文件长度，说明之前已经下载成功
                return TYPE_SUCCESS;
            }
            //下面使用OkHttp进行下载
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .addHeader("RANGE", "bytes=" + downloadedLength + "-")
                    .url(downloadUrl)
                    .build();
            Response response = client.newCall(request).execute();
            if (response != null) {
                is = response.body().byteStream();
                savedFile = new RandomAccessFile(file, "rw");
                savedFile.seek(downloadedLength);
                byte[] b = new byte[1024];
                int total = 0;
                int len;
                while ((len = is.read(b)) != -1) {
                    if (isCanceled) {
                        return TYPE_CANCELED;
                    } else if (isPaused) {
                        return TYPE_PAUSED;
                    } else {
                        total += len;
                        savedFile.write(b, 0, len);
                        int progress = (int) ((total + downloadedLength) * 100 / contentLength);
                        publishProgress(progress);
                    }
                }
                response.body().close();
                return TYPE_SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (savedFile != null) {
                    savedFile.close();
                }
                if (isCanceled && file != null) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return TYPE_FAILED;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if (progress > lastProgress) {
            listener.onProgress(progress);
            lastProgress = progress;
        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        switch (integer) {
            case TYPE_SUCCESS:
                listener.onSuccess();
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_PAUSED:
                listener.onPaused();
                break;
            case TYPE_CANCELED:
                listener.onCanceled();
                break;
            default:
                break;
        }
    }

    public void pausedDownload() {
        isPaused = true;
    }

    public void cancelDownload() {
        isCanceled = true;
    }

    private long getContentLength(String downloadUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        Response response = client.newCall(request).execute();
        if (response != null && response.isSuccessful()) {
            long contentLength = response.body().contentLength();
            response.body().close();
            return contentLength;
        }
        return 0;
    }

}
