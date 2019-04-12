package com.jal.www.cathy;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.List;

public class MusicService extends Service {
    private static final String TAG = "JalLog::MusicService";
    private MediaPlayer mPlayer;
    private Music mMusic;
    private List<Music> mMusicList;
    private int mPosition;
    public MusicService() {
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "MusicService :: onCreate()");
        super.onCreate();
        mMusicList = MusicList.getMusicList(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "MusicService :: onStartCommand()");
        mPosition = intent.getExtras().getInt("mPosition");
        playIndex(mPosition);
        return super.onStartCommand(intent, flags, startId);
    }

    public class MyBinder extends Binder {

        public boolean isPlaying(){
            return mPlayer.isPlaying();
        }
        public boolean isNullOfPlayer(){
            return mPlayer == null;
        }
        public void play_pause() {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                Log.i(TAG, "Play stop");
            } else {
                mPlayer.start();
                Log.i(TAG, "Play start");
            }
        }

        public void pre(){
            mPosition = (mPosition - 1 + mMusicList.size()) % mMusicList.size();
            playIndex(mPosition);
        }

        public void next(){
            mPosition = (mPosition + 1) % mMusicList.size();
            playIndex(mPosition);
        }

        public int getPosition(){
            return mPosition;
        }
        //Returns the length of the mMusic in milliseconds
        public int getDuration(){
            return mPlayer.getDuration();
        }

        //Return the name of the mMusic
        public String getName(){
            return mMusic.getName();
        }

        //Returns the current progress of the mMusic in milliseconds
        public int getCurrenPostion(){
            return mPlayer.getCurrentPosition();
        }

        //Set the progress of mMusic playback in milliseconds
        public void seekTo(int mesc){
            mPlayer.seekTo(mesc);
        }
    }

    private void playIndex(int position) {
        if (null == mPlayer){
            mPlayer = new MediaPlayer();
        }
        if (mMusic == mMusicList.get(position)){
            return;// continue play this mMusic.
        }

        mPlayer.reset();
        mMusic = mMusicList.get(position);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mPlayer.setDataSource(mMusic.getUrl());
            mPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPlayer.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "MusicService :: onBind()");
        return new MyBinder();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "MusicService :: onDestroy()");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "MusicService :: onUnbind()");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i(TAG, "MusicService :: onUnbind()");
        super.onRebind(intent);
    }

}
