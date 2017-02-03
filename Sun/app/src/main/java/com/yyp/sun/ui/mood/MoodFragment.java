package com.yyp.sun.ui.mood;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import com.yyp.sun.base.BaseFragment;
import com.yyp.sun.config.SunInfo;
import com.yyp.sun.dao.MoodDiaryDataDao;
import com.yyp.sun.db.DB;
import com.yyp.sun.entity.MoodDiaryData;
import com.yyp.sun.ui.mood.adapter.MoodDiaryAdapter;
import com.yyp.sun.util.AuthUtil;
import com.yyp.sun.view.AutoLoadRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MoodFragment extends BaseFragment {

    MoodDiaryAdapter moodDiaryAdapter;
    MoodDiaryDataDao moodDiaryDataDao;
    List<MoodDiaryData> list;

    @Override
    public void initView() {
        super.initView();

        initData();

        recyclerView.setLoadMoreListener(new AutoLoadRecyclerView.LoadMoreListener() {
            @Override
            public void loadMore() {

            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadLocalData();
            }
        });
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        super.initData();

        list = new ArrayList<>();
        moodDiaryDataDao = DB.getInstance(getContext(), SunInfo.DB_NAME_MOOD_DIARY).getMoodDiaryDataDao();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        moodDiaryAdapter = new MoodDiaryAdapter(getActivity());
        recyclerView.setAdapter(moodDiaryAdapter);
        loadLocalData();
    }

    /**
     * 加载本地数据库的数据
     */
    public void loadLocalData(){
        // 每次加载时，先清空数据
        list.clear();
        showLoading();
        if(moodDiaryDataDao.count() != 0){
            for(MoodDiaryData data : moodDiaryDataDao.loadAll()){
                // 将和当前用户匹配的数据进行显示
                if(data.getAuthorID().equals(AuthUtil.getCurrentUser().getObjectId())){
                    list.add(data);
                }
            }
        }
        moodDiaryAdapter.replaceData(list);
        showLoadingIndicator(false);
        hideLoading();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null && data.getExtras().getBoolean("isUpdated")){
            switch (requestCode){
                case SunInfo.CODE_IN_PUBLISH_MOOD:
                case SunInfo.CODE_IN_MOOD_DETAIL:
                    loadLocalData();
                    break;
            }
        }
    }
}

