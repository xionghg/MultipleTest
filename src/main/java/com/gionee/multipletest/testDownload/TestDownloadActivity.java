package com.gionee.multipletest.testDownload;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gionee.multipletest.R;

public class TestDownloadActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "TestDownloadActivity";
    private static final String PAUSE = "Pause Download";
    private static final String CONTINUE = "Continue Download";
    private static final String URL = "https://raw.githubusercontent.com/guolindev/eclipse/master/eclipse-inst-win64.exe";

    private Button startDownload;
    private static Button pauseDownload;
    private Button cancelDownload;

    private static boolean isCheck = false;

    public boolean isChecking() {
        return isCheck;
    }

    public static void setCheck(boolean flag) {
        isCheck = flag;
        if (isCheck) {
            Log.d(TAG, "setCheck: pauseDownload.setEnabled(false)");
            pauseDownload.setEnabled(false);
            return;
        }
        Log.d(TAG, "setCheck: pauseDownload.setEnabled(true)");
        pauseDownload.setEnabled(true);
    }

    private DownloadService.DownloadBinder mDownloadBinder;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: ");
            mDownloadBinder = (DownloadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_download);

        startDownload = (Button) findViewById(R.id.start_download);
        startDownload.setOnClickListener(this);
        pauseDownload = (Button) findViewById(R.id.pause_download);
        pauseDownload.setOnClickListener(this);
        cancelDownload = (Button) findViewById(R.id.cancel_download);
        cancelDownload.setOnClickListener(this);

        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
        bindService(intent, mConnection, BIND_AUTO_CREATE);

    }

    @Override
    public void onClick(View v) {
        if (mDownloadBinder == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.start_download:
                Log.d(TAG, "onClick: Start Download");
                setCheck(true);
                mDownloadBinder.startDownload(URL);
                break;
            case R.id.pause_download:
                if (pauseDownload.getText().equals(PAUSE)) {
                    Log.d(TAG, "onClick: Pause Download");
                    setCheck(true);
                    mDownloadBinder.pauseDownload();
                    pauseDownload.setText(CONTINUE);
                } else {
                    Log.d(TAG, "onClick: Continue Download");
//                    setCheck(true);
                    mDownloadBinder.startDownload(URL);
                    pauseDownload.setText(PAUSE);
                }
                break;
            case R.id.cancel_download:
                mDownloadBinder.cancelDownload();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        unbindService(mConnection);
    }
}
