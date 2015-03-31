package com.bitlove.mw.service;

import android.os.AsyncTask;
import android.view.View;

/**
 * 加载图书内容
 * */
public class LoadBookPage extends AsyncTask<String, Integer, View> {
	View bookPage;
	AsynTaskListener mListener;
	@Override
	protected View doInBackground(String... params) {
		
		if(mListener!=null){
			bookPage = mListener.doInBackground(params);
		};
		
		return bookPage;
	}
	@Override
	protected void onPostExecute(View result) {
		super.onPostExecute(result);
		if(mListener!=null){
			mListener.onPostExecute(result);
		}
	}
	
	public void setOnPostListener(AsynTaskListener listener){
		mListener = listener;
	}
	
	public interface AsynTaskListener{
		public void onPostExecute(View result);
		public View doInBackground(String... params);
	}

}
