package com.yyp.sun.ui.mood;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import com.yyp.sun.base.BaseFragment;
import com.yyp.sun.config.SunInfo;
import com.yyp.sun.dao.MoodDiaryDataDao;
import com.yyp.sun.db.DB;
import com.yyp.sun.ui.mood.adapter.MoodDiaryAdapter;
import com.yyp.sun.view.AutoLoadRecyclerView;

/**
 *
 */
public class MoodFragment extends BaseFragment {

    MoodDiaryAdapter moodDiaryAdapter;
    MoodDiaryDataDao moodDiaryDataDao;

    @Override
    public void initView() {
        super.initView();

        moodDiaryDataDao = DB.getInstance(getContext(), SunInfo.DB_NAME_MOOD_DIARY).getMoodDiaryDataDao();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        moodDiaryAdapter = new MoodDiaryAdapter(getContext());
        recyclerView.setAdapter(moodDiaryAdapter);
        loadLocalData();

        recyclerView.setLoadMoreListener(new AutoLoadRecyclerView.LoadMoreListener() {
            @Override
            public void loadMore() {

            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showLoading();
                loadLocalData();
            }
        });
    }

    /**
     * 加载本地数据库的数据
     */
    public void loadLocalData(){
        if(moodDiaryDataDao.count() != 0){
            moodDiaryAdapter.replaceData(moodDiaryDataDao.loadAll());
        }
        showLoadingIndicator(false);
        hideLoading();
    }
}

