package org.gtq.vhubs.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.gtq.vhubs.R;

import support.ui.activity.VBaseActivity;

public class WebviewActivity extends VBaseActivity {

	View status_bar;
	private WebView webview;
	private ProgressBar loadingProgress;

	String url;
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		url=getIntent().getStringExtra("url");
		String title =getIntent().getStringExtra("title");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle(title);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		webview = (WebView) findViewById(R.id.webview);

		loadingProgress = (ProgressBar) findViewById(R.id.loadingProgress);// 此控件为一个进度条控件，属于自己添加的控件
		// 设置WebView属性，能够执行Javascript脚本
		webview.getSettings().setJavaScriptEnabled(true);
		// 加载需要显示的网页
		webview.loadUrl(url);
		// 此处能拦截超链接的url,即拦截href请求的内容.
		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {

				super.onReceivedTitle(view, title);
			}

			@Override
			public void onProgressChanged(WebView view, int progress) {

				loadingProgress.setVisibility(View.VISIBLE);
				loadingProgress.setProgress(progress * 100);
				if (progress == 100) {
					loadingProgress.setVisibility(View.GONE);
				}
				super.onProgressChanged(view, progress);
			}

		});
		webview.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
			webview.goBack(); // goBack()表示返回WebView的上一页面
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	public static void Launch(Activity activity,String title,String url) {
		Intent intent = new Intent(activity, WebviewActivity.class);
		intent.putExtra("title",title);
		intent.putExtra("url",url);
		activity.startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
