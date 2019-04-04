package com.jal.www.cathy;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.List;

public class MusicService extends Service {
    private static final String TAG = "LogMusicService";
    private MediaPlayer player;
    private Music music;
    private List<Music>musicList;
    private int position;
    public MusicService() {
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        super.onCreate();
        musicList = MusicList.getMusicList(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        position = intent.getExtras().getInt("position");
        playIndex(position);
        return super.onStartCommand(intent, flags, startId);
    }

    public class MyBinder extends Binder {

        public boolean isPlaying(){
            return player.isPlaying();
        }
        public boolean isNullOfPlayer(){
            return player == null;
        }
        public void play_pause() {
            if (player.isPlaying()) {
                player.pause();
                Log.i(TAG, "Play stop");
            } else {
                player.start();
                Log.i(TAG, "Play start");
            }
        }

        public void pre(){
            position = (position - 1 + musicList.size()) % musicList.size();
            playIndex(position);
        }

        public void next(){
            position = (position + 1) % musicList.size();
            playIndex(position);
        }

        public int getPosition(){
            return position;
        }
        //Returns the length of the music in milliseconds
        public int getDuration(){
            return player.getDuration();
        }

        //Return the name of the music
        public String getName(){
            return music.getName();
        }

        //Returns the current progress of the music in milliseconds
        public int getCurrenPostion(){
            return player.getCurrentPosition();
        }

        //Set the progress of music playback in milliseconds
        public void seekTo(int mesc){
            player.seekTo(mesc);
        }
    }

    private void playIndex(int position) {
        if (null == player){
            player = new MediaPlayer();
        }
        if (music == musicList.get(position)){
            return;// continue play this music.
        }

        player.reset();
        music = musicList.get(position);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            player.setDataSource(music.getUrl());
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.start();
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
}
