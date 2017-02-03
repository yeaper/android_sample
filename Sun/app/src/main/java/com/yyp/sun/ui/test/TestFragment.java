package com.yyp.sun.ui.test;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;

import com.yyp.sun.base.BaseFragment;
import com.yyp.sun.ui.test.adapter.TestAdapter;
import com.yyp.sun.ui.test.data.Test;
import com.yyp.sun.view.AutoLoadRecyclerView;

/**
 *
 */
public class TestFragment extends BaseFragment {

    TestAdapter testAdapter;

    @Override
    public void initView() {
        super.initView();

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        testAdapter = new TestAdapter(getActivity());
        recyclerView.setAdapter(testAdapter);
        loadData();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showLoadingIndicator(false);
            }
        });

        recyclerView.setLoadMoreListener(new AutoLoadRecyclerView.LoadMoreListener() {
            @Override
            public void loadMore() {

            }
        });
    }

    /**
     * 加载数据
     */
    public void loadData(){

        testAdapter.addData(new Test("", "心理健康"));
        testAdapter.addData(new Test("", "个性倾向"));
        testAdapter.addData(new Test("", "人格特征"));
    }
}
