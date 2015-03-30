package com.bitlove.mw;

import android.app.Service;
import android.os.Vibrator;

/**
 * 工具类
 * */
public class Util {
	/**
	 * 震动一下
	 * */
	public static void Vibrator(){
		Vibrator vibrator = (android.os.Vibrator) MyApplication.getInstance().getSystemService(Service.VIBRATOR_SERVICE);
		vibrator.vibrate(100);
	}
}
