package com.me.versionupdatedemo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by cs on 2019/3/14.
 */
public class NetworkUtils {

	public static final int NETWORK_INVALID = 0;
	public static final int NETWORK_WAP = 1;
	public static final int NETWORK_2G = 2;
	public static final int NETWORK_3G = 3;
	public static final int NETWORK_4G = 4;
	public static final int NETWORK_WIFI = 5;

	public static int getNetWorkType(Context context){
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info!=null && info.isConnected()){
			String type = info.getTypeName();
			if (type.equalsIgnoreCase("WIFI")){
				return NETWORK_WIFI;
			}else if (type.equalsIgnoreCase("mobile")){
				return NETWORK_WAP;
			}
			return NETWORK_INVALID;
		}else {
			return NETWORK_INVALID;
		}

	}

}
