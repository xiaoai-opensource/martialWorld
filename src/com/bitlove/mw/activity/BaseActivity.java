package com.bitlove.mw.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;

public class BaseActivity extends Activity {
	protected Context mContext = this;

	protected void startActivity(Class cls){
		Intent intent = new Intent(mContext, cls);
		startActivity(intent);
	}
	/**
	 * 显示上一级按钮
	 * */
	protected void showUpAction(){
		 ActionBar actionBar = getActionBar();
		 actionBar.setDisplayHomeAsUpEnabled(true);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}
}
