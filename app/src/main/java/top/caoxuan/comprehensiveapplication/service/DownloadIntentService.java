package top.caoxuan.comprehensiveapplication.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import java.io.File;

import top.caoxuan.comprehensiveapplication.R;
import top.caoxuan.comprehensiveapplication.listener.DownloadListener;
import top.caoxuan.comprehensiveapplication.task.DownloadTask;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class DownloadIntentService extends IntentService implements BaseService {
    private DownloadTask downloadTask;
    private String downloadUrl;
    private DownloadListener listener = new DownloadListener() {
        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1, getNotification("Downloading...", progress));
        }

        @Override
        public void onSuccess() {
            downloadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1, getNotification("Download Success", -1));
            Toast.makeText(DownloadIntentService.this, "Download Success", Toast.LENGTH_SHORT).show();
            Intent toInstall = new Intent(Intent.ACTION_VIEW);
            toInstall.setFlags(FLAG_ACTIVITY_NEW_TASK);
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            File file = new File(getExternalFilesDir(DIRECTORY_DOWNLOADS) + fileName);
            //String directory = Environment.getDownloadCacheDirectory().getPath();
            Uri apkUri = FileProvider.getUriForFile(DownloadIntentService.this, "top.caoxuan.comprehensiveapplication.fileprovider", file);
            Log.d("cxDebug", "ur1:" + apkUri);
            Log.d("cxDebug", "读取路径:" + file);
            toInstall.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            toInstall.setDataAndType(apkUri, "application/vnd.android.package-archive");
            startActivity(toInstall);
        }

        @Override
        public void onFailed() {
            downloadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1, getNotification("Download failed", -1));
            Toast.makeText(DownloadIntentService.this, "Download failed", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onPaused() {
            downloadTask = null;
            Toast.makeText(DownloadIntentService.this, "Paused", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCanceled() {
            downloadTask = null;
            Toast.makeText(DownloadIntentService.this, "Canceled", Toast.LENGTH_SHORT).show();

        }
    };


    public class DownloadBinder extends Binder {

        public void startDownload(String url) {
            Log.d("cxDebug", "dT:" + downloadTask);
            if (downloadTask == null) {
                downloadUrl = url;
                downloadTask = new DownloadTask(listener);
                downloadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, downloadUrl, DownloadIntentService.this);
                startForeground(1, getNotification("Downloading...", 0));
                Toast.makeText(DownloadIntentService.this, "Downloading...", Toast.LENGTH_SHORT).show();
            }
        }

       /* public void pauseDownload() {
            if(downloadTask != null) {
                downloadTask.pausedDownload();
            }
        }

        public void cancelDownload() {
            if (downloadTask != null) {
                downloadTask.cancelDownload();
            }
            if (downloadUrl != null) {
                String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                File file = new File(directory + fileName);
                if (file.exists()) {
                    file.delete();
                }
                getNotificationManager().cancel(1);
                stopForeground(true);
                Toast.makeText(DownloadService.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }*/
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private Notification getNotification(String title, int progress) {
        //Intent intent = new Intent(this, MainActivity.class);//通知栏的点击事件
        //PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "download");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        //builder.setContentIntent(pi);
        builder.setContentTitle(title);
        if (progress >= 0) {
            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, false);
        }
        return builder.build();

    }

    public DownloadIntentService() {
        super("DownloadIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        DownloadBinder downloadBinder = new DownloadBinder();
        String url = "https://app.caoxuan.top:10443/app/outputs/apk/release/app-release.apk";
        downloadBinder.startDownload(url);
    }

}
