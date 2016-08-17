package com.yyp.sun.ui.mood.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yyp.sun.R;
import com.yyp.sun.entity.MoodDiaryData;
import com.yyp.sun.ui.mood.MoodDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yyp on 2016/8/17.
 */
public class MoodDiaryAdapter extends RecyclerView.Adapter<MoodDiaryAdapter.Holder> {

    private List<MoodDiaryData> mData;
    private Context mContext;

    private int lastPosition = -1; //最后一个item的下标

    public MoodDiaryAdapter(Context context){
        mData = new ArrayList<>();
        this.mContext = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_mood_diary, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        String image1 = mData.get(position).getImageUrl1();
        String image2 = mData.get(position).getImageUrl2();
        // 设置图片
        if(TextUtils.isEmpty(image1)){
            holder.moodDiaryImage1.setVisibility(View.GONE);
            holder.moodDiaryImage2.setVisibility(View.GONE);
        }else {
            if(TextUtils.isEmpty(image2)){
                holder.moodDiaryImage1.setImageURI("file://" + mData.get(position).getImageUrl1());
            }else{
                holder.moodDiaryImage1.setImageURI("file://" + mData.get(position).getImageUrl1());
                holder.moodDiaryImage2.setImageURI("file://" + mData.get(position).getImageUrl2());
            }
        }
        holder.moodDiaryContent.setText(mData.get(position).getContent());
        holder.moodDiaryDate.setText(mData.get(position).getCreateDate());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goMoodDetail = new Intent(mContext, MoodDetailActivity.class);
                mContext.startActivity(goMoodDetail);
            }
        });

        setAnimation(holder.cardView, position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 添加一条数据
     * @param test
     */
    public void addData(MoodDiaryData moodDiaryData){
        mData.add(moodDiaryData);
        notifyDataSetChanged();
    }

    /**
     * 替换全部数据
     * @param data
     */
    public void replaceData(List<MoodDiaryData> data){
        mData = data;
        notifyDataSetChanged();
    }

    /**
     * 为每个 item 设置动画
     * @param viewToAnimate
     * @param position
     */
    protected void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.item_bottom_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.mood_diary_cardview)
        CardView cardView;
        @BindView(R.id.mood_diary_image1)
        SimpleDraweeView moodDiaryImage1;
        @BindView(R.id.mood_diary_image2)
        SimpleDraweeView moodDiaryImage2;
        @BindView(R.id.mood_diary_content)
        TextView moodDiaryContent;
        @BindView(R.id.mood_diary_date)
        TextView moodDiaryDate;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
