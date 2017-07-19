package com.yyp.xrecyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yyp.xrecyclerview.adapter.MainAdapter;
import com.yyp.xrecyclerview.model.Animals;
import com.yyp.xrecyclerview.widget.XRecyclerView;
import com.yyp.xrecyclerview.widget.interfaces.XRecyclerViewLoadingListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView.LayoutManager layoutManager;
    private MainAdapter adapter;
    private ArrayList<Animals> data = new ArrayList<>();

    int count = 1;

    XRecyclerView xRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    public void initView(){
        xRecyclerView= (XRecyclerView) findViewById(R.id.mainXRecyclerView);
        // 先添加数据
        for(;count<=10;){
            data.add(new Animals(count, "name"+count, count, "F"));
            count++;
        }
        adapter = new MainAdapter(this, data);
        layoutManager = new GridLayoutManager(this, 3);

        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setPullRefreshEnabled(true); // 开启下拉刷新
        xRecyclerView.setLoadingMoreEnabled(true); // 开启上拉加载
        xRecyclerView.setLoadingListener(new XRecyclerViewLoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        count = 1;
                        data.clear();
                        for(;count<=10;){
                            data.add(new Animals(count, "name"+count, count, "F"));
                            count++;
                        }
                        adapter.refreshData(data);
                        xRecyclerView.reset();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        for(int i=0;i<10;i++,count++){
                            if(count <= 40) {
                                data.add(new Animals(count, "name"+count, count, "F"));
                                adapter.refreshData(data);
                                xRecyclerView.loadMoreComplete();
                            }else{
                                xRecyclerView.setNoMore(true);
                                break;
                            }
                        }
                    }
                }, 2000);
            }
        });

        for(int i=1;i<3;i++){
            initHeaderView(i);
        }
    }

    /**
     * 初始化头部布局
     */
    public void initHeaderView(int i){
        View headerView = LayoutInflater.from(this).inflate(R.layout.xrecyclerview_header_view, xRecyclerView, false);
        TextView headerText = headerView.findViewById(R.id.header_text);
        headerText.setText("头部布局"+i);
        xRecyclerView.addHeaderView(headerView);
    }
}
