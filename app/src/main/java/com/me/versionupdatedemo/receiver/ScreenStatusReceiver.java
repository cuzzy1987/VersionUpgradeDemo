package com.me.versionupdatedemo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by ${cs} on 2019/2/18.
 */
public class ScreenStatusReceiver extends BroadcastReceiver {

	private ScreenStatusReceiver instance;
	private IntentFilter screenStatusFilter;


	public ScreenStatusReceiver getInstance() {
		return instance==null?new ScreenStatusReceiver():instance;
	}

	public IntentFilter getScreenStatusFilter(){
		if (screenStatusFilter != null) return screenStatusFilter;
		IntentFilter screenStatusFilter = new IntentFilter();
		screenStatusFilter.addAction(Intent.ACTION_SCREEN_ON);
		screenStatusFilter.addAction(Intent.ACTION_SCREEN_OFF);
		return screenStatusFilter;
	}

	final String SCREEN_ON = "android.intent.action.SCREEN_ON";
	final String SCREEN_OFF = "android.intent.action.SCREEN_OFF";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (SCREEN_ON.equals(intent.getAction())){
			System.out.println("-=SCREEN_ON=-");
		}else if (SCREEN_OFF.equals(intent.getAction())){
			System.out.println("-=SCREEN_OFF=-");
		}
	}
}
