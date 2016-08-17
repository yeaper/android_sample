package com.yyp.sun.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.victor.loading.rotate.RotateLoading;
import com.yyp.sun.R;
import com.yyp.sun.base.interfaces.SunView;
import com.yyp.sun.view.AutoLoadRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yyp on 2016/8/12.
 */
public class BaseFragment extends Fragment implements SunView {

    @BindView(R.id.recyclerView)
    public AutoLoadRecyclerView recyclerView;
    @BindView(R.id.rotateLoading)
    public RotateLoading loadingView;
    @BindView(R.id.refreshLayout)
    public SwipeRefreshLayout refreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_base, container, false);
        ButterKnife.bind(this, root);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
    }

    public void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 设置SwipeRefreshLayout的加载圈的颜色
        refreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
    }

    @Override
    public void showLoadingIndicator(boolean isLoading) {
        // 显示下拉加载圈
        refreshLayout.setRefreshing(isLoading);
    }

    @Override
    public void showLoading() {
        // 显示数据加载圈
        loadingView.start();
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        recyclerView.setVisibility(View.VISIBLE);
        // 隐藏数据加载圈
        if (loadingView.isStart()) {
            loadingView.stop();
        }
    }
}
