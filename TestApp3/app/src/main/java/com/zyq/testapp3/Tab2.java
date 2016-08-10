package com.zyq.testapp3;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

public class Tab2 extends Fragment {

	private EditText et_data;
	private TextView tv_data;
	protected Button btn_write;
	protected Button btn_read;

	final String FILE_NAME = "/diary.txt";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View tab2= inflater.inflate(R.layout.tab2, container,false);

		initView(tab2);
		return tab2;
	}

	public void initView(View v){
		et_data = (EditText) v.findViewById(R.id.write_data);
		tv_data = (TextView) v.findViewById(R.id.read_data);
		btn_write = (Button) v.findViewById(R.id.write);
		btn_read = (Button) v.findViewById(R.id.read);

		btn_write.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				write(et_data.getText().toString());
				et_data.setText("");
			}
		});
		btn_read.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				tv_data.setText(read());
			}
		});
	}

	private String read()
	{
		try {
			// 如果手机插入了SD卡，而且应用程序具有访问SD的权限
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				// 获取SD卡对应的存储目录
				File sdCardDir = Environment.getExternalStorageDirectory();
				// 获取指定文件对应的输入流
				FileInputStream fis = new FileInputStream(sdCardDir.getCanonicalPath() + FILE_NAME);
				// 将指定输入流包装成BufferedReader
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));
				StringBuilder sb = new StringBuilder("");
				String line;
				// 循环读取文件内容
				while ((line = br.readLine()) != null)
				{
					sb.append(line);
				}
				// 关闭资源
				br.close();
				return sb.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void write(String data){
		try {
			// 如果手机插入了SD卡，而且应用程序具有访问SD的权限
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				// 获取SD卡的目录
				File sdCardDir = Environment.getExternalStorageDirectory();
				File targetFile = new File(sdCardDir.getCanonicalPath() + FILE_NAME);
				// 以指定文件创建 RandomAccessFile对象
				RandomAccessFile raf = new RandomAccessFile(targetFile, "rw");
				// 将文件记录指针移动到最后
				raf.seek(targetFile.length());
				// 输出文件内容
				raf.write(data.getBytes());
				// 关闭RandomAccessFile
				raf.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}