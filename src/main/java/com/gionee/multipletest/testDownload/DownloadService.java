package com.gionee.multipletest.testDownload;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.gionee.multipletest.R;

import java.io.File;

public class DownloadService extends Service {
    private static final String TAG = "DownloadService";

    private DownloadTask mDownloadTask;

    private String downloadUrl;

    private DownloadListener mListener = new DownloadListener() {
        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1, getNotification("Downloading...", progress));
        }

        @Override
        public void onSuccess() {
            Log.d(TAG, "DownloadListener onSuccess: set mDownloadTask = null");
            mDownloadTask = null;

            stopForeground(true);
            getNotificationManager().notify(1, getNotification("Download Success", -1));
            Toast.makeText(DownloadService.this, "Download Success", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed() {
            Log.d(TAG, "DownloadListener onFailed: set mDownloadTask = null");
            mDownloadTask = null;

            stopForeground(true);
            getNotificationManager().notify(1, getNotification("Download Failed", -1));
            Toast.makeText(DownloadService.this, "Download Failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPaused() {
            Log.d(TAG, "DownloadListener onPaused: set mDownloadTask = null");
            mDownloadTask = null;
            TestDownloadActivity.setCheck(false);
            Toast.makeText(DownloadService.this, "Paused", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCanceled() {
            Log.d(TAG, "DownloadListener onCanceled: set mDownloadTask = null");
            mDownloadTask = null;
            stopForeground(true);
            Toast.makeText(DownloadService.this, "Canceled", Toast.LENGTH_SHORT).show();
        }
    };

    public DownloadService() {
    }

    private DownloadBinder mBinder = new DownloadBinder();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    class DownloadBinder extends Binder{
        public void startDownload(String url){
            if (mDownloadTask == null) {
                Log.d(TAG, "DownloadBinder startDownload");
                downloadUrl = url;
                mDownloadTask = new DownloadTask(mListener);
                mDownloadTask.execute(downloadUrl);
                startForeground(1, getNotification("Download...", 0));
                Toast.makeText(DownloadService.this, "Download...", Toast.LENGTH_SHORT).show();
            }
        }

        public void pauseDownload(){
            if (mDownloadTask != null) {
                Log.d(TAG, "DownloadBinder pauseDownload");
                mDownloadTask.pauseDownload();
            } else {
                Log.d(TAG, "pauseDownload: cancel check cause mDownloadTask is null");
                TestDownloadActivity.setCheck(false);
            }
        }

        public void cancelDownload(){
            if (mDownloadTask != null) {
                Log.d(TAG, "DownloadBinder cancelDownload");
                mDownloadTask.cancelDownload();
            } else {
                if (downloadUrl != null) {
                    String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                    String directory = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS).getPath();
                    File file = new File(directory + fileName);
                    if (file.exists()){
                        Log.d(TAG, "DownloadBinder cancelDownload: delete file");
                        file.delete();
                    }
                    getNotificationManager().cancel(1);
                    stopForeground(true);
                    Toast.makeText(DownloadService.this, "Canceled", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private Notification getNotification(String title, int progress){
        Intent intent = new Intent(this, TestDownloadActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentIntent(pi);
        builder.setContentTitle(title);
        if (progress > 0) {
            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, false);
        }
        return builder.build();
    }
}
