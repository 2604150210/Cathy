package com.jal.www.cathy;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private MyConnection myConnection;
    private TextView music_title;
    private List<Music>musicList;
    private Button btn_pre, btn_play_pause, btn_next;
    private MusicService.MyBinder myBinder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = new Intent(this, MusicService.class);
        intent.putExtras(getIntent().getExtras());
        startService(intent);
        myConnection = new MyConnection();
        bindService(intent,myConnection, BIND_AUTO_CREATE);
        myBinder = myConnection.myBinder;
        bindView();
    }

    private void bindView() {
        musicList = MusicList.getMusicList(this);
        music_title = findViewById(R.id.music_title);
        btn_pre = findViewById(R.id.btn_pre);
        btn_play_pause = findViewById(R.id.btn_play_pause);
        btn_next = findViewById(R.id.btn_next);
        Bundle bundle = getIntent().getExtras();
        String title = musicList.get(bundle.getInt("position")).getTitle();
        music_title.setText(title);
        btn_pre.setOnClickListener(this);
        btn_play_pause.setOnClickListener(this);
        btn_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_pre:
                myBinder.pre();
                break;
            case R.id.btn_play_pause:
                myBinder.play_pause();
                break;
            case R.id.btn_next:
                myBinder.next();
                break;
        }
    }
}
