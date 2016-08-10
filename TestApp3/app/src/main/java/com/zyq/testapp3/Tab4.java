package com.zyq.testapp3;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class Tab4 extends Fragment implements View.OnClickListener{

	// 获取界面中显示歌曲标题、作者文本框
	TextView status;
	// 播放/暂停、停止按钮
	ImageButton play, stop;
	MediaPlayer mp3;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)  {
		View tab4= inflater.inflate(R.layout.tab4, container,false);

		initView(tab4);
		// 拿到播放器资源
		mp3 = new MediaPlayer();
		//在res下新建一个raw文件夹把一首歌放到此文件夹中并用英文命名
		mp3 = MediaPlayer.create(getActivity(),R.raw.beautiful);

		return tab4;
	}

	public void initView(View v){
		// 获取程序界面界面中的两个按钮
		play = (ImageButton) v.findViewById(R.id.play);
		stop = (ImageButton) v.findViewById(R.id.stop);
		status = (TextView) v.findViewById(R.id.status);
		// 为两个按钮的单击事件添加监听器
		play.setOnClickListener(this);
		stop.setOnClickListener(this);
	}

	@Override
	public void onClick(View source) {
		switch (source.getId()) {
			// 按下播放/暂停按钮
			case R.id.play:
				try {
					if (mp3!=null) {
						mp3.stop();
						mp3.prepare();  //进入到准备状态
						mp3.start();  //开始播放
					}
					status.setText("Playing");  //改变输出信息为“Playing”，下同
				} catch (Exception e) {
					status.setText(e.toString());//以字符串的形式输出异常
					e.printStackTrace();  //在控制台（control）上打印出异常
				}
				break;
			// 按下停止按钮
			case R.id.stop:
				try {
					if (mp3!=null) {
						mp3.stop();
						status.setText("Stop");
					}
				} catch (Exception e) {
					status.setText(e.toString());
					e.printStackTrace();
				}
				break;
			default:break;
		}
	}
}