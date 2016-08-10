package com.zyq.testapp3;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class Tab5 extends Fragment {

	Button setTime;
	AlarmManager aManager;
	TextView time;

	private Calendar calendar = Calendar.getInstance();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View tab5= inflater.inflate(R.layout.tab5, container, false);

		// 获取程序界面的按钮
		setTime = (Button) tab5.findViewById(R.id.setTime);
		time = (TextView) tab5.findViewById(R.id.clock_time);
		// 获取AlarmManager对象
		aManager = (AlarmManager) getContext().getSystemService(Service.ALARM_SERVICE);
		// 为“设置闹钟”按钮绑定监听器
		setTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View source) {
				//设置当前时间
				calendar.setTimeInMillis(System.currentTimeMillis());
				//获取小时
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				//获取分钟
				int minute = calendar.get(Calendar.MINUTE);

				// 创建一个TimePickerDialog实例，并把它显示出来。
				new TimePickerDialog(getActivity(), minute,// 绑定监听器
						new TimePickerDialog.OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker tp, int hourOfDay, int minute) {
								// 根据用户选择时间来设置Calendar对象
								calendar.set(Calendar.HOUR, hourOfDay);
								calendar.set(Calendar.MINUTE, minute);

								// 指定启动AlarmActivity组件
								Intent intent = new Intent(getActivity(), AlarmTest.class);
								// 创建PendingIntent对象
								PendingIntent pi = PendingIntent.getActivity(getActivity(), 0, intent, 0);
								// 设置AlarmManager将在Calendar对应的时间启动指定组件
								aManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);

								time.setText(calendar.getTime().toString());
								// 显示闹铃设置成功的提示信息
								Toast.makeText(getActivity(), "闹种设置成功啦", Toast.LENGTH_SHORT).show();
							}
						}, hour, minute, false).show();
			}
		});

		return tab5;
	}
}
