package com.bitlove.mw.activity;

import com.bitlove.mw.R;
import com.bitlove.mw.event.BookEvent;
import com.bitlove.mw.event.BookEvent.ClickEventListener;
import com.bitlove.mw.manager.BookPageManager;
import com.bitlove.mw.view.BookPageView;
import com.bitlove.mw.view.Progress;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

/**
 * 阅读书
 * */
public class BookActivity extends BaseActivity {
	private BookPageManager mPageManager;
	private RelativeLayout readLayout;	//阅读文字界面
	private BookPageView pageView;		//书页
	private Progress bookProgress;		//进度

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		requestWindowFeature(Window.FEATURE_NO_TITLE);//取消标题栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
				WindowManager.LayoutParams. FLAG_FULLSCREEN);//全屏
		*/
		setContentView(R.layout.activity_book);
		init();
	}
	@Override
	protected void onPause() {
		super.onPause();
		mPageManager.recordHis();
	}
	private void init(){
		String name = getIntent().getStringExtra("bookName");
		mPageManager = BookPageManager.getInstance(mContext,getWindowManager(),name);
		readLayout = (RelativeLayout)findViewById(R.id.readView);
		bookProgress = (Progress) findViewById(R.id.bookProgress);
		pageView = mPageManager.getCurrentPage();
		readLayout.removeAllViews();
		readLayout.addView(pageView);
		bookProgress.setProgress(mPageManager.getPercent());
		
		readLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				BookEvent evs = BookEvent.getInstance(event,getWindowManager().getDefaultDisplay());
				evs.dispatchClickEvent(new ClickEventListener() {

					@Override
					public void onRightClick() {
						pageNext();
					}

					@Override
					public void onLeftClick() {
						pagePre();
					}

					@Override
					public void onCenterClick() {
					}
				});

				return true;
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(keyCode==KeyEvent.KEYCODE_VOLUME_UP){
			pagePre();
			return true;
		}else if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN){
			pageNext();
			return true;
		}else{			
			return super.onKeyDown(keyCode, event);
		}
	}

	private void pageNext(){
		pageView = mPageManager.getNextPage();
		readLayout.removeAllViews();
		readLayout.addView(pageView);
		bookProgress.setProgress(mPageManager.getPercent());
	}
	private void pagePre(){
		pageView = mPageManager.getPrePage();
		readLayout.removeAllViews();
		readLayout.addView(pageView);
		bookProgress.setProgress(mPageManager.getPercent());
	}

}
