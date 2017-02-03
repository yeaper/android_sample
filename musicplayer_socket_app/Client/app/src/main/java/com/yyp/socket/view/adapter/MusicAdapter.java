package com.yyp.socket.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyp.socket.R;
import com.yyp.socket.bean.music.Music;

import java.util.List;

/**
 * Created by yyp on 2016/12/10.
 */

public class MusicAdapter extends BaseAdapter {

    List<Music> data;
    Context context;

    public MusicAdapter(List<Music> data, Context context){
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void addItem(Music music){
        data.add(music);
        notifyDataSetChanged();
    }

    public void clearAll(){
        data.clear();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.music_item, null);
            holder.musicName = (TextView) view.findViewById(R.id.music_item_music_name);
            holder.playStop = (ImageView) view.findViewById(R.id.music_item_music_start_stop);

            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        holder.musicName.setText(data.get(i).getMusicName());

        return view;
    }

    private static class ViewHolder{
        TextView musicName;
        ImageView playStop;
    }
}
