package org.gtq.vhubs.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 
 * @author baozi
 * 
 */
public class MainActivity extends Activity {

	// 地址
	private String dl = "http://ftp-apk.pconline.com.cn/85cafce5e1a734c38ee97a0d4ddd2963/pub/download/201010/Root_Explorer_v4.0.apk";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//		Function_Utility.setAppContext(getApplicationContext());
//
//		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Uri uri = Uri.parse(dl);
//				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//				startActivity(intent);
//			}
//		});
//		findViewById(R.id.button2).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				try {
//					download(dl);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
	}

	private void download(String dl) throws Exception {
			Intent service = new Intent(this, DownloadService.class);
			service.putExtra(DownloadService.INTENT_URL, dl);
			startService(service);


	}
}
