package com.me.versionupdatedemo.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.download.DownloadTask;
import com.me.versionupdatedemo.callback.DownloadResultCallback;

import java.io.File;

import static com.me.versionupdatedemo.callback.DownloadResultCallback.Failed;
import static com.me.versionupdatedemo.callback.DownloadResultCallback.Running;
import static com.me.versionupdatedemo.callback.DownloadResultCallback.completed;

/**
 * Created by ${cs} on 2019/2/14.
 */
public class UpdateUtils {

	private File file;
	private String url="";
	private Context mContext;
	private DownloadResultCallback downloadResultCallback;


	// registered @ onCreate
	public UpdateUtils(Context mContext) {
		this.mContext = mContext;
		Aria.download(this).register();
	}

	public void download(String name,String url,String desc,DownloadResultCallback downloadResultCallback){
		this.url = url;
		this.downloadResultCallback = downloadResultCallback;
		file = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),name);

		Aria.download(mContext)
				.load(url)
				.setFilePath(file.getAbsolutePath())
				.start();
	}

	@Download.onTaskRunning
	protected void running(DownloadTask task){
		if (downloadResultCallback!=null)downloadResultCallback.downloadResult(Running,task);
	}

	@Download.onTaskFail
	protected void onFail(DownloadTask task){
		downloadResultCallback.downloadResult(Failed,task);
	}

	@Download.onTaskComplete
	protected void completed(DownloadTask task){
		System.out.println("download complete ==> "+task.getDownloadPath()+" url ==>"+url);
		Toast.makeText(mContext,"下载完成，准备安装",Toast.LENGTH_SHORT).show();
		if (downloadResultCallback!=null)downloadResultCallback.downloadResult(completed,task);
	}

	public void installApk() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (Build.VERSION.SDK_INT >= 24){

			Uri uri = FileProvider.getUriForFile(mContext,mContext.getPackageName()+".fileProvider",file);
			intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			intent.setDataAndType(uri,"application/vnd.android.package-archive");
		}else {
			intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
		}
		mContext.startActivity(intent);
	}

}
