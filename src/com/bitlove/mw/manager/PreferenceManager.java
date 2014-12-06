package com.bitlove.mw.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPreference数据管理
 * */
public class PreferenceManager {

	private static PreferenceManager mPreference;
	private SharedPreferences mSharedPreferences;
	private Editor mPreferenceEditor;
	private Context mContext;
	private String bookPreferenceName = "martialWorld";
	
	public static PreferenceManager getInstance(Context context){
		if(mPreference==null){
			mPreference = new PreferenceManager();
		}
		mPreference.mContext = context;
		if(mPreference.mSharedPreferences==null){
			mPreference.mSharedPreferences = context.getSharedPreferences(mPreference.bookPreferenceName, context.MODE_PRIVATE);
			mPreference.mPreferenceEditor = mPreference.mSharedPreferences.edit();
		}
		
		return mPreference;
	}
	/**
	 * 存储信息
	 * */
	public void record(String key,String value){
		mPreferenceEditor.putString(key, value);
		mPreferenceEditor.commit();
	}
	/**
	 * 读取信息
	 * */
	public String read(String key,String dValue){
		return mSharedPreferences.getString(key, dValue);
	}
}
