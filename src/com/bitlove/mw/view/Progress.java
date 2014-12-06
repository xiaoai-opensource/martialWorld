package com.bitlove.mw.view;

import com.bitlove.mw.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 进度条
 * */
public class Progress extends RelativeLayout {

	private RelativeLayout progressLayout=null;	//进度条容器
	private TextView tvProgress;				//进度
	private Context mContext;
	public Progress(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
		
		
	}
	private void init(){
		progressLayout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.view_progress, this);
		tvProgress = (TextView) progressLayout.findViewById(R.id.tvProgress);
	}
	/**
	 * 设置进度条刻度
	 * */
	public void setProgress(String percent){
		tvProgress.setText(percent+"%");
	}

}
