package com.yyp.xrecyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yyp.xrecyclerview.R;
import com.yyp.xrecyclerview.model.Animals;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        
    private Context context;
    private ArrayList<Animals> data;

    public MainAdapter(Context context, ArrayList<Animals> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.main_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            ((MyViewHolder) holder).name.setText(data.get(position).getName());
            ((MyViewHolder) holder).weight.setText(String.valueOf(data.get(position).getWeight()));
            ((MyViewHolder) holder).gender.setText(data.get(position).getGender());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * 刷新列表
     * @param data 数据列表
     */
    public void refreshData(ArrayList<Animals> data){
        this.data = data;
        notifyDataSetChanged();
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView weight;
        TextView gender;

        MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            weight = itemView.findViewById(R.id.weight);
            gender = itemView.findViewById(R.id.gender);
        }
    }
}