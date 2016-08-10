package com.zyq.testapp3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;

/**
 * Description:
 * <br/>site: <a href="http://www.crazyit.org">crazyit.org</a>
 * <br/>Copyright (C), 2001-2014, Yeeku.H.Lee
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @author  Yeeku.H.Lee kongyeeku@163.com
 * @version  1.0
 */
/**
 * Description:
 * <br/>site: <a href="http://www.crazyit.org">crazyit.org</a>
 * <br/>Copyright (C), 2001-2014, Yeeku.H.Lee
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @author  Yeeku.H.Lee kongyeeku@163.com
 * @version  1.0
 */
public class AlarmTest extends Activity {
	MediaPlayer alarmMusic;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alert);

		// 加载指定音乐，并为之创建MediaPlayer对象
		alarmMusic = MediaPlayer.create(AlarmTest.this, R.raw.alarm);
		alarmMusic.setLooping(true);
		// 播放音乐
		alarmMusic.start();
		// 创建一个对话框
		new AlertDialog.Builder(AlarmTest.this).setTitle("闹钟")
				.setMessage("闹钟响了,Go！Go！Go！")
				.setPositiveButton("停止", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 停止音乐
						if (alarmMusic != null) {
							alarmMusic.stop();
						}
						finish();
					}
				}).show();

	}
}