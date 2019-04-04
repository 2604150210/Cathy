package com.jal.www.cathy;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivityLog";
    private MyConnection myConnection;
    private MusicService.MyBinder myBinder;
    private List<Music>musicList;
    private ListView listView;
    private Context context;
    private int positionOfPlaying;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        requestPermission();
    }

    private void requestPermission() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!permissionList.isEmpty()){
            ActivityCompat.requestPermissions(this,permissionList.toArray(new String[permissionList.size()]),1);
        }else {
            initView();
        }
    }

    private void initView() {
        Intent intent = new Intent();
        intent.setClass(this, MusicService.class);
        myConnection = new MyConnection();
        bindService(intent, myConnection, BIND_AUTO_CREATE);
        myBinder = myConnection.myBinder;
        if (myBinder == null)Log.i(TAG, "myBinder is null");
        if (myBinder.isNullOfPlayer()){
            positionOfPlaying = -1;
        } else {
            positionOfPlaying = myBinder.getPosition();
        }
        listView = findViewById(R.id.listView);
        musicList = MusicList.getMusicList(this);
        MusicAdapter adapter = new MusicAdapter(this, musicList,positionOfPlaying);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                intent.putExtras(bundle);
                intent.setClass(MainActivity.this, DetailActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0){
                    for (int i = 0; i < grantResults.length; i++) {

                        int grantResult = grantResults[i];
                        if (grantResult == PackageManager.PERMISSION_DENIED){
                            String s = permissions[i];
                            Toast.makeText(this,s+getResources().getString(R.string.rejectPermission),Toast.LENGTH_SHORT).show();
                        }else{
                            initView();
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "MainActivity :: onDestroy()");
        super.onDestroy();
    }
}
