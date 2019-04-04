package com.jal.www.cathy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MusicAdapter extends BaseAdapter {
    private Context context;
    private List<Music>musicList;
    private int positionOfPlaying = -1;
    public MusicAdapter() {
    }
    public MusicAdapter(Context context, List<Music> musicList, int positionOfPlaying){
        this.context = context;
        this.musicList = musicList;
        this.positionOfPlaying = positionOfPlaying;
    }
    @Override
    public int getCount() {
        return musicList.size();
    }

    @Override
    public Object getItem(int position) {
        return musicList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.music_item,null);
        if (position == positionOfPlaying){
            convertView = LayoutInflater.from(context).inflate(R.layout.music_item2,null);
        }
        Music m = musicList.get(position);
        TextView textMusicName = convertView.findViewById(R.id.music_item_name);
        textMusicName.setText(m.getName());
        TextView textMusicSinger = convertView.findViewById(R.id.music_item_singer);
        textMusicSinger.setText(m.getSinger());
        TextView textMusicTime = convertView.findViewById(R.id.music_item_time);
        textMusicTime.setText(toTime((int) m.getTime()));
        return convertView;
    }
    public String toTime(int time) {
        time /= 1000;
        int minute = time / 60;
        int hour = minute / 60;
        int second = time % 60;
        minute %= 60;
        return String.format("%02d:%02d", minute, second);
    }
}
