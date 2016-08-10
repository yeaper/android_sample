package com.zyq.testapp3;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

public class Tab3 extends Fragment {

	WebView mWebView;
	Button mBtn1;

	String url = "file:///android_asset/test.html";

	protected Activity a;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View tab3= inflater.inflate(R.layout.tab3, container,false);

		a = getActivity();
		Window w = a.getWindow();
		//全屏显示
		w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//高亮显示
		w.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		initView(tab3);

		return tab3;
		
	}

	@SuppressLint("JavascriptInterface")
	public void initView(View v){


		mWebView = (WebView) v.findViewById(R.id.webView);
		mBtn1 = (Button) v.findViewById(R.id.button);

		//设置编码
		mWebView.getSettings().setDefaultTextEncodingName("utf-8");
		//支持js
		mWebView.getSettings().setJavaScriptEnabled(true);
		//设置本地调用对象及其接口
		mWebView.addJavascriptInterface(new JavaScriptObject(a), "myObj");
		//载入js
		mWebView.loadUrl(url);

		//点击调用js中方法
		mBtn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 调用js的方法->  javascript:方法
				mWebView.loadUrl("javascript:funFromjs()");
			}
		});
	}

	public class JavaScriptObject {

		Context mContext;

		public JavaScriptObject(Context mContxt) {
			this.mContext = mContxt;
		}

		@JavascriptInterface
		public void fun1FromAndroid(String name) {
			Toast.makeText(mContext, name, Toast.LENGTH_LONG).show();
		}
	}
}