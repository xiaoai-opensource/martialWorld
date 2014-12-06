package com.bitlove.mw.remind;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast提示
 * */
public class ToastReminder {
	private static Toast mToast;
	private static void init(Context context){
		if(mToast==null){
			mToast = new Toast(context);
		}
	}
	/**
	 * toast
	 * */
	public static void showToast(Context context,String text,int duration){
		init(context);
		mToast.cancel();
		mToast.makeText(context, text, duration).show();
	}
}
