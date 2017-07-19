package com.yyp.xrecyclerview.widget.interfaces;

public interface BaseRefreshHeader {

	int STATE_NORMAL = 0; // 正常状态
    int STATE_RELEASE_TO_REFRESH = 1; // 释放刷新（下拉）状态
	int STATE_REFRESHING = 2; // 刷新状态
	int STATE_DONE = 3; // 刷新完成状态

	void onMove(float delta);

	boolean releaseAction();

	void refreshComplete();

}