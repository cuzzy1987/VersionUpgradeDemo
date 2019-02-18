package com.me.versionupdatedemo.callback;

import android.support.annotation.IntRange;

import com.arialyy.aria.core.download.DownloadTask;

/**
 * Created by ${cs} on 2019/2/15.
 */
public interface DownloadResultCallback {
	int Failed = -1;
	int Running = 0;
	int completed = 1;
	void downloadResult(@IntRange(from = Failed,to = completed) int type, DownloadTask path);
}
