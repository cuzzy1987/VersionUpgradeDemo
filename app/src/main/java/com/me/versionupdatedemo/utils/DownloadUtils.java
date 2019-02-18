package com.me.versionupdatedemo.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import java.io.File;

/**
 * Created by ${cs} on 2019/2/14.
 * 适用于6.0  不支持9.0版本 无法下载||下载返回失败
 * 8.0 华为手机可下载 无法安装
 */
public class DownloadUtils {

	private DownloadManager downloadManager;
	private Context mContext;
	private long downloadId;
	private String filePath;

	public DownloadUtils(Context mContext) {
		this.mContext = mContext;
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			checkStatus();
		}
	};

	public void download(String path, String name) {
		System.out.println("-=start download=-");
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse(path));
		request.setAllowedOverRoaming(false);
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
		request.setTitle("Version_Update");
		request.setDescription("new Version downloading..");
		request.setVisibleInDownloadsUi(true);
		// set file path
		File file = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),name);
		request.setDestinationUri(Uri.fromFile(file));
		filePath = file.getAbsolutePath();
		// get manager
		if (downloadManager==null) downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
		downloadId = downloadManager.enqueue(request);
		//fit
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
			request.setRequiresDeviceIdle(false);
			request.setRequiresCharging(false);
		}
		// register BroadcastReceiver
		mContext.registerReceiver(receiver,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}

	private void checkStatus() {
		System.out.println("-=checkStatus=-");
		DownloadManager.Query query = new DownloadManager.Query();
		// find via query
		query.setFilterById(downloadId);
		Cursor cursor = downloadManager.query(query);
		if (cursor.moveToFirst()){
			int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
			switch (status){
				case DownloadManager.STATUS_PAUSED:
					break;
				case DownloadManager.STATUS_PENDING:
					break;
				case DownloadManager.STATUS_RUNNING:
					break;
				case DownloadManager.STATUS_SUCCESSFUL:
					installApk();
					cursor.close();
					break;
					case DownloadManager.STATUS_FAILED:
						Toast.makeText(mContext,"download failed",Toast.LENGTH_LONG).show();
						cursor.close();
						mContext.unregisterReceiver(receiver);
						break;
			}
			System.out.println("-=status code=- ==> "+status);
		}
	}


	private void installApk() {
		setPermission(filePath);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (Build.VERSION.SDK_INT >= 24){
			File file = new File(filePath);
			Uri uri = FileProvider.getUriForFile(mContext,"com.me.versionupdatedemo.fileprovider",file);
			// authority app the uri presented file
			intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			intent.setDataAndType(uri,"application/vnd.android.package-archive");
		}else {
			intent.setDataAndType(Uri.fromFile(new File(filePath)),"application/vnd.android.package-archive");
		}
		mContext.startActivity(intent);
	}


	private void setPermission(String filePath) {
		String command = "chmod" +"777" +" "+filePath;
		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec(command);
		}catch (Exception e){
			e.printStackTrace();
			System.out.println("downloadManager ==> "+e.getMessage());
		}

	}

}
