package com.jal.www.cathy;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "JalLog::DetailActivity";
    private MyConnection mConnection;
    private TextView mMusicTitle;
    private List<Music> mMusicList;
    private Button mBtnPre, mBtnPlayPause, mBtnNext;
    private MusicService.MyBinder mBinder;

    class MyConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected");
            mBinder = (MusicService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected");
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "DetailActivity :: onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        bindMusicService();
        bindView();
    }
    private void bindMusicService() {
        Intent intent = new Intent();
        intent.setClass(this, MusicService.class);
        intent.putExtras(getIntent().getExtras());
        startService(intent);
        mConnection = new MyConnection();
        bindService(intent, mConnection, BIND_AUTO_CREATE);
        Log.i(TAG, "mBinder:"+ mBinder);
    }
    private void bindView() {
        mMusicList = MusicList.getMusicList(this);
        mMusicTitle = findViewById(R.id.music_title);
        mBtnPre = findViewById(R.id.btn_pre);
        mBtnPlayPause = findViewById(R.id.btn_play_pause);
        mBtnNext = findViewById(R.id.btn_next);
        Bundle bundle = getIntent().getExtras();
        String title = mMusicList.get(bundle.getInt("position")).getTitle();
        mMusicTitle.setText(title);
        mBtnPre.setOnClickListener(this);
        mBtnPlayPause.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_pre:
                mBinder.pre();
                break;
            case R.id.btn_play_pause:
                mBinder.play_pause();
                break;
            case R.id.btn_next:
                mBinder.next();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "DetailActivity :: onDestroy()");
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "DetailActivity :: onStart()");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "DetailActivity :: onStop()");
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "DetailActivity :: onPause()");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        Log.i(TAG, "DetailActivity :: onRestart()");
        super.onRestart();
    }
    @Override
    protected void onResume() {
        Log.i(TAG, "DetailActivity :: onResume()");
        super.onResume();
    }
}
