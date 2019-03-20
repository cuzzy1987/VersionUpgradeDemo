package com.me.versionupdatedemo.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.me.versionupdatedemo.R;
import com.me.versionupdatedemo.callback.OnNegativeClickListener;
import com.me.versionupdatedemo.callback.OnPositiveClickListener;

/**
 * Created by cs on 2019/3/14.
 */
public class UpgradeDialog extends AlertDialog{

	private View view;
	private boolean goNext = false;
	private Context mContext;
	private TextView vTv,sTv,cTv;
	private Button negativeTv,positiveTv;
	private ProgressBar mProgressbar;
	private OnPositiveClickListener mPositiveClickListener;
	private OnNegativeClickListener mNegativeClickListener;

	/* 生命周期  最后才走到 onCreate */

	public UpgradeDialog(Context context) {
		super(context,false,null);
		mContext = context;
		view =LayoutInflater.from(mContext).inflate(R.layout.layout_upgrate_dialog,null);
		mProgressbar = view.findViewById(R.id.progressbar);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(view);
	}

	public UpgradeDialog build(){
		vTv = view.findViewById(R.id.vTv);
		sTv = view.findViewById(R.id.sTv);
		cTv = view.findViewById(R.id.cTv);
		negativeTv = view.findViewById(R.id.negativeTv);
		positiveTv = view.findViewById(R.id.positiveTv);

		view.findViewById(R.id.positiveTv).setOnClickListener(v -> {
			System.out.println("state up => "+goNext);
			mPositiveClickListener.onPositiveClick();
			System.out.println("state down => "+goNext);
			if (goNext){
				positiveTv.setText("正在下载");
				negativeTv.setEnabled(false);
				positiveTv.setEnabled(false);
			}
		});
		view.findViewById(R.id.negativeTv).setOnClickListener(v -> mNegativeClickListener.OnNegativeClickListener());
		return this;
	}


	public UpgradeDialog setTv(String versionCode,String size,String content){
			vTv.setText(versionCode);
			sTv.setText(size);
			cTv.setText(content);
		return this;
	}

	public UpgradeDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public UpgradeDialog setOnPositiveClickListener(OnPositiveClickListener clickListener){
		this.mPositiveClickListener = clickListener;
		return this;
	}

	public UpgradeDialog setOnNegativeClickListener(OnNegativeClickListener clickListener){
		this.mNegativeClickListener = clickListener;
		return this;
	}

	public void setProgressBar(int progress){
//		int percent = (int) (progress*100);
		System.out.println("set progress "+progress);
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
			mProgressbar.setProgress(progress,true);
		}else {
			mProgressbar.setProgress(progress);
		}
	}

	public void setState(boolean b) {
		goNext = b;
	}

	public void setEnable() {
		positiveTv.setText("正在下载");
		negativeTv.setEnabled(false);
		positiveTv.setEnabled(false);
	}
}
