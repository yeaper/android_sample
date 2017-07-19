package com.xh.sun.ui.news;

import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.xh.sun.base.BaseFragment;
import com.xh.sun.view.AutoLoadRecyclerView;

public class NewsFragment extends BaseFragment {

    @Override
    public void initView() {
        super.initView();
        recyclerView.setLoadMoreListener(new AutoLoadRecyclerView.LoadMoreListener() {
            @Override
            public void loadMore() {
                Toast.makeText(getActivity(), "LoadMore", Toast.LENGTH_SHORT).show();
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getActivity(), "Refresh", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();

    }
}

