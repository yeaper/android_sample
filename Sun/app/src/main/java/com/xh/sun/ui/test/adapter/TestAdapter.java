package com.xh.sun.ui.test.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xh.sun.R;
import com.xh.sun.ui.test.data.Test;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Test> mData;
    private Context mContext;

    private int lastPosition = -1; //最后一个item的下标

    public TestAdapter(Context context){
        mData = new ArrayList<>();
        this.mContext = context;
    }

    /**
     * 创建 ViewHolder
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_test, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Uri uri;
        Holder holder = (Holder) viewHolder;
        // 适配测试类型的图片
        switch (position){
            case 0:
                uri = Uri.parse("res://com.yyp.sun/" + R.drawable.test_type_mental_health);
                break;
            case 1:
                uri = Uri.parse("res://com.yyp.sun/" + R.drawable.test_type_character);
                break;
            case 2:
                uri = Uri.parse("res://com.yyp.sun/" + R.drawable.test_type_person);
                break;
            default:
                uri = Uri.parse("res://com.yyp.sun/" + R.drawable.test_type_person);
                break;
        }
        holder.testTypeView.setImageURI(uri);
        holder.testTypeTile.setText(mData.get(position).getTest_type());
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
    public void addData(Test test){
        mData.add(test);
        notifyDataSetChanged();
    }

    /**
     * 替换全部数据
     * @param data
     */
    public void replaceData(List<Test> data){
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

    class Holder extends RecyclerView.ViewHolder{

        @BindView(R.id.test_cardview)
        CardView cardView;
        @BindView(R.id.test_type_view)
        SimpleDraweeView testTypeView;
        @BindView(R.id.test_type_title)
        TextView testTypeTile;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
