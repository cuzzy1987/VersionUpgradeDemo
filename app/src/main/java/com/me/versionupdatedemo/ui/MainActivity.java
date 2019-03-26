package com.me.versionupdatedemo.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.arialyy.aria.core.download.DownloadTask;
import com.me.versionupdatedemo.R;
import com.me.versionupdatedemo.callback.DownloadResultCallback;
import com.me.versionupdatedemo.receiver.ScreenStatusReceiver;
import com.me.versionupdatedemo.utils.DownloadUtils;
import com.me.versionupdatedemo.utils.NetworkUtils;
import com.me.versionupdatedemo.utils.UpdateUtils;
import com.me.versionupdatedemo.view.UpgradeDialog;
import com.yanzhenjie.permission.AndPermission;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,DownloadResultCallback {

	private DownloadUtils download;
	private UpdateUtils updateUtils;
	private String name = "app-debug.apk";
	private String path = "http://192.168.0.52:8000/app-debug.apk";

	private ScreenStatusReceiver mScreenStatusReceiver;
	private UpgradeDialog dialog;
	private AlertDialog.Builder msgDialogBuilder;

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

	private int percent=0;
	private void initView() {
		dialog = new UpgradeDialog(this);
		msgDialogBuilder = new AlertDialog.Builder(this);
		findViewById(R.id.btn).setOnClickListener(this);
		// 倒计时总时间 间隔时间

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.btn:
				showDialog();
				break;
			default:
				break;
		}
	}

	CountDownTimer countDownTimer;
	public void showDialog(){
		dialog.build()
				.setTv("版本：1.100.1","版本大小：25M","更新内容：有什么可说的")
				.setOnPositiveClickListener(() ->{if (checkNetworkState()==NetworkUtils.NETWORK_WIFI)dialog.setState(false);})
				.setOnNegativeClickListener(() -> dialog.dismiss())
				.show();
	}

	private int checkNetworkState() {
		switch (NetworkUtils.getNetWorkType(this)){
			case NetworkUtils.NETWORK_INVALID:
				showToast("当前网络不可用");

				break;
			case NetworkUtils.NETWORK_WAP:
				showToast("检测到您正在使用流量 是否使用流量下载?");// 继续弹窗 继续选
				msgDialogBuilder
						.setTitle("检测到您正在使用流量,是否使用继续使用流量下载更新")
						.setNegativeButton("取消",(dialog1, which) -> {dialog1.dismiss();showToast("取消了");})
						.setPositiveButton("使用流量下载",(dialog1, which) -> {
							dialog1.dismiss();showToast("使用流量下载");
							dialog.setEnable();
						})
						.show();
				break;
			case NetworkUtils.NETWORK_WIFI:
//				upgrade();
				tikTok();
				break;
		}
		return NetworkUtils.getNetWorkType(this);
	}

	private void tikTok(){
		countDownTimer = new CountDownTimer(10000,1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				System.out.println("onTick "+millisUntilFinished);
				percent += 10;
				dialog.setProgressBar(percent);
			}

			@Override
			public void onFinish() {
				System.out.println("timer finish");
				dialog.setProgressBar(100);
			}
		};

		countDownTimer.start();
	}

	private void upgrade() {

		if (AndPermission.hasPermissions(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
			updateUtils.download(name,path,"",this);
		}else AndPermission.with(this)
				.runtime()
				.permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
				.onDenied(deny->showToast(""))
				.onGranted(grant->updateUtils.download(name,path,"",this))
				.start();

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
