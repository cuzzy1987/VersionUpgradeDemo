package com.me.versionupdatedemo.ui;

import android.Manifest;
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
import com.yanzhenjie.permission.AndPermission;

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

//		initReceiver();
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
				/*System.out.println("是否有写入权限 另"+AndPermission.hasPermissions(this,Manifest.permission.WRITE_EXTERNAL_STORAGE));
				AndPermission.with(this)
						.runtime()
						.permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
						.onDenied(deny->showToast("has refused"))
						.onGranted(grant->showToast("has granted"))
						.start();*/

				if (AndPermission.hasPermissions(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
					updateUtils.download(name,path,"",this);
				}else AndPermission.with(this)
							.runtime()
							.permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
							.onDenied(deny->showToast("has refused"))
							.onGranted(grant->updateUtils.download(name,path,"",this))
							.start();
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

	private int i= 0;
	private void showProgressDialog(DownloadTask download) {
		showToast("进度 "+ (i+=10));
	}

	private void showToast(String msg) {
		Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
	}
}
