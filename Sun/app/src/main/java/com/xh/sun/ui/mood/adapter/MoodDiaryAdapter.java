package com.xh.sun.ui.mood.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.xh.sun.R;
import com.xh.sun.config.SunInfo;
import com.xh.sun.entity.MoodDiaryData;
import com.xh.sun.ui.mood.MoodDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoodDiaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MoodDiaryData> mData;
    private Activity mActivity;

    private int lastPosition = -1; //最后一个item的下标

    public MoodDiaryAdapter(Activity activity){
        mData = new ArrayList<>();
        this.mActivity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case 1:
                view = LayoutInflater.from(mActivity).inflate(R.layout.item_mood_diary_no_image, parent, false);
                return new NoImageHolder(view);
            case 2:
                view = LayoutInflater.from(mActivity).inflate(R.layout.item_mood_diary, parent, false);
                return new Holder(view);
            default:
                view = LayoutInflater.from(mActivity).inflate(R.layout.item_mood_diary, parent, false);
                return new Holder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if(viewHolder instanceof NoImageHolder){
            NoImageHolder noImageHolder = (NoImageHolder) viewHolder;
            noImageHolder.content.setText(mData.get(position).getContent());
            noImageHolder.date.setText(mData.get(position).getCreateDate());
            // 进入详情页
            noImageHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent goMoodDetail = new Intent(mActivity, MoodDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("moodDiary", mData.get(position));
                    goMoodDetail.putExtras(bundle);
                    mActivity.startActivityForResult(goMoodDetail, SunInfo.CODE_IN_MOOD_DETAIL);
                }
            });
            setAnimation(noImageHolder.cardView, position);

        }else if(viewHolder instanceof Holder){
            Holder holder = (Holder) viewHolder;
            holder.moodDiaryContent.setText(mData.get(position).getContent());
            holder.moodDiaryDate.setText(mData.get(position).getCreateDate());
            // 设置图片
            holder.moodDiaryImage1.setImageURI("file://" + mData.get(position).getImageUrl1());
            holder.moodDiaryImage2.setImageURI("file://" + mData.get(position).getImageUrl2());
            // 进入详情页
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent goMoodDetail = new Intent(mActivity, MoodDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("moodDiary", mData.get(position));
                    goMoodDetail.putExtras(bundle);
                    mActivity.startActivityForResult(goMoodDetail, SunInfo.CODE_IN_MOOD_DETAIL);
                }
            });
            setAnimation(holder.cardView, position);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        String image1 = mData.get(position).getImageUrl1();
        String image2 = mData.get(position).getImageUrl2();
        if(TextUtils.isEmpty(image1) && TextUtils.isEmpty(image2)){
            return 1;
        }else{
            return 2;
        }
    }

    /**
     * 添加一条数据
     * @param moodDiaryData
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

    /**
     * 无图片
     */
    class NoImageHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.mood_diary_no_image_cardview)
        CardView cardView;
        @BindView(R.id.mood_diary_no_image_content)
        TextView content;
        @BindView(R.id.mood_diary_no_image_date)
        TextView date;

        public NoImageHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 有图片
     */
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
