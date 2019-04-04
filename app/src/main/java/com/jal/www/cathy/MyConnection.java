package com.jal.www.cathy;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

class MyConnection implements ServiceConnection {
        public MusicService.MyBinder myBinder;
        private static final String TAG = "LogMyConnection";
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected");
            myBinder = (MusicService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected");
        }
    }