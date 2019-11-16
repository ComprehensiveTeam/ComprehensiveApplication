package com.example.comprehensiveapplication.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import com.example.comprehensiveapplication.listener.DownloadListener;
import com.example.comprehensiveapplication.R;
import com.example.comprehensiveapplication.task.DownloadTask;

import java.io.File;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class DownloadService extends Service {


    private DownloadTask downloadTask;
    private String downloadUrl;
    private DownloadListener listener = new DownloadListener() {
        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1,getNotification("Downloading...", progress));
        }

        @Override
        public void onSuccess() {
            downloadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("Download Success", -1));
            Toast.makeText(DownloadService.this, "Download Success", Toast.LENGTH_SHORT).show();
            Intent toInstall = new Intent(Intent.ACTION_VIEW);
            toInstall.setFlags(FLAG_ACTIVITY_NEW_TASK);
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            File file = new File(getExternalFilesDir(DIRECTORY_DOWNLOADS) + fileName);
            //String directory = Environment.getDownloadCacheDirectory().getPath();
            Uri apkUri = FileProvider.getUriForFile(DownloadService.this, "com.example.comprehensiveapplication.fileprovider", file);
            Log.d("cxdebug", "ur1:" + apkUri);
            Log.d("cxdebug", "读取路径:" + file);
            toInstall.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            toInstall.setDataAndType(apkUri, "application/vnd.android.package-archive");
            startActivity(toInstall);
        }

        @Override
        public void onFailed() {
            downloadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("Download failed",-1));
            Toast.makeText(DownloadService.this,"Download failed",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onPaused() {
            downloadTask = null;
            Toast.makeText(DownloadService.this,"Paused",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCanceled() {
            downloadTask = null;
            Toast.makeText(DownloadService.this, "Canceled", Toast.LENGTH_SHORT).show();

        }
    };

    private DownloadBinder mBinder = new DownloadBinder();
    public DownloadService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    public class DownloadBinder extends Binder {

        public void startDownload(String url) {
            Log.d("cxdebug", "dT:" + downloadTask);
            if (downloadTask == null) {
                downloadUrl = url;
                downloadTask = new DownloadTask(listener);
                downloadTask.execute(downloadUrl, DownloadService.this);
                startForeground(1, getNotification("Downloading...",0));
                Toast.makeText(DownloadService.this, "Downloading...", Toast.LENGTH_SHORT).show();
            }
        }

        public void pauseDownload() {
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
        }
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private Notification getNotification(String title, int progress) {
        //Intent intent = new Intent(this, MainActivity.class);//通知栏的点击事件
        //PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"download");
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


}
