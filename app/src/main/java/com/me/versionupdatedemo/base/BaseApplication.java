package com.me.versionupdatedemo.base;

import android.app.Application;

import com.arialyy.aria.core.Aria;

/**
 * Created by ${cs} on 2019/2/15.
 */
public class BaseApplication  extends Application {


	@Override
	public void onCreate() {
		super.onCreate();
		initAria();
	}

	private void initAria() {
		Aria.init(this);
	}
}
