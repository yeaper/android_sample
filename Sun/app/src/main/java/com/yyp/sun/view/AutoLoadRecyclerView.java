package com.yyp.sun.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 带上拉加载的RecyclerView
 *
 */
public class AutoLoadRecyclerView extends RecyclerView {

    private boolean mIsLoading = false;  // 是否正在加载
    private boolean isDragLoad = true;
    private boolean isSettlingLoad = false;

    private LoadMoreListener loadMoreListener;

    /**
     * 构造方法
     * @param context
     */
    public AutoLoadRecyclerView(Context context) {
        super(context);
    }

    public AutoLoadRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoLoadRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 加载更多的接口
     */
    public interface LoadMoreListener {
        void loadMore();
    }

    /**
     * 设置加载更多的监听器
     * @param loadMoreListener
     */
    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    /**
     * 拖动和滑动时是否加载图片
     * @param isDragLoad
     * @param isSettlingLoad
     */
    public void setLoadingConfig(boolean isDragLoad, boolean isSettlingLoad) {
        this.isDragLoad = isDragLoad;
        this.isSettlingLoad = isSettlingLoad;
    }

    /**
     * 没懂
     * @param dx
     * @param dy
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);

        if (!canScrollVertically(1)) {
            loadMoreListener.loadMore();
        }
    }
}
