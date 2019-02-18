package com.me.versionupdatedemo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.arialyy.aria.core.download.DownloadTask;
import com.me.versionupdatedemo.R;
import com.me.versionupdatedemo.callback.DownloadResultCallback;
import com.me.versionupdatedemo.receiver.ScreenStatusReceiver;
import com.me.versionupdatedemo.utils.DownloadUtils;
import com.me.versionupdatedemo.utils.UpdateUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,DownloadResultCallback {

	private DownloadUtils download;
	private UpdateUtils updateUtils;
	private String name = "app-debug.apk";
	private String path = "http://192.168.0.52:8000/app-debug.apk";

	private ScreenStatusReceiver mScreenStatusReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initReceiver();
		initView();
		initData();
	}



	private void initData() {
		updateUtils = new UpdateUtils(this);
	}

	private void initView() {
		findViewById(R.id.btn).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.btn:
				Toast.makeText(this.getBaseContext(), "下载最新版本", Toast.LENGTH_SHORT).show();
				updateUtils.download(name,path,"",this);
				break;
			default:
				break;
		}
	}

	private void initReceiver() {
		mScreenStatusReceiver = new ScreenStatusReceiver().getInstance();
		registerReceiver(mScreenStatusReceiver,mScreenStatusReceiver.getScreenStatusFilter());
	}

	@Override
	public void downloadResult(int type,DownloadTask download) {
		switch (type){
			case Running:
				showProgressDialog(download);
				break;
			case completed:
				updateUtils.installApk();
				break;
			case Failed:
				showToast("下载失败请重试");
				break;
		}
	}

	private void showProgressDialog(DownloadTask download) {

	}

	private void showToast(String msg) {
		Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
	}
}
