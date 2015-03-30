package com.bitlove.mw;

import android.app.Application;
/**
 * 自己的application类
 * */

public class MyApplication extends Application {
	private static MyApplication mApplication = null;
	public static MyApplication getInstance(){
		return mApplication;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = this;
	}
}
