package com.bitlove.mw.activity;

import com.bitlove.mw.R;
import com.bitlove.mw.event.BookEvent;
import com.bitlove.mw.event.BookEvent.ClickEventListener;
import com.bitlove.mw.manager.BookPageManager;
import com.bitlove.mw.service.LoadBookPage;
import com.bitlove.mw.service.LoadBookPage.AsynTaskListener;
import com.bitlove.mw.view.Progress;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

/**
 * 阅读书
 * */
public class BookActivity extends BaseActivity {
	private BookPageManager mPageManager;
	private ViewGroup readLayout;	//阅读文字界面
	private Progress bookProgress;		//进度
	private String bookName;

	ViewGroup page1;
	ViewGroup page2;
	ViewGroup page3;


	View prePage;
	View curPage;
	View nextPage;
	private int mCurPos=1;	//1:pre,2:cur,3:next
	public boolean lazyLoad = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		requestWindowFeature(Window.FEATURE_NO_TITLE);//取消标题栏
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
				WindowManager.LayoutParams. FLAG_FULLSCREEN);//全屏
		 */
		setContentView(R.layout.activity_book);
		bookName = getIntent().getStringExtra("bookName");
		init();
		lazyLoad = true;
		LoadBookPage();
		loadPageNext();
		loadPagePre();
	}
	@Override
	protected void onPause() {
		super.onPause();
		mPageManager.recordHis();
	}

	private void init(){

		mPageManager = BookPageManager.getInstance(mContext,getWindowManager(),bookName);
		readLayout = (ViewGroup)findViewById(R.id.readView);
		bookProgress = (Progress) findViewById(R.id.bookProgress);
		readLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				BookEvent evs = BookEvent.getInstance(event,getWindowManager().getDefaultDisplay());
				evs.dispatchClickEvent(new ClickEventListener() {

					@Override
					public void onRightClick() {
						System.out.println("onRightClick");
						showNextPage();
					}

					@Override
					public void onLeftClick() {
						showPrePage();
					}

					@Override
					public void onCenterClick() {
					}
				});

				return true;
			}
		});

		page1 = (ViewGroup) findViewById(R.id.page1);
		page2 = (ViewGroup) findViewById(R.id.page2);
		page3 = (ViewGroup) findViewById(R.id.page3);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if(keyCode==KeyEvent.KEYCODE_VOLUME_UP){
			//pagePre();
			return true;
		}else if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN){
			//pageNext();

			return true;
		}else{			
			return super.onKeyDown(keyCode, event);
		}
	}
	/**
	 * 设置书页内容
	 * */
	private void setBookPage(int type){

		readLayout.removeAllViews();
		switch (type) {
		case 0:
			readLayout.addView(prePage);
			break;
		case 1:
			readLayout.addView(curPage);
			break;
		case 2:
			readLayout.addView(nextPage);
			break;
		default:
			break;
		}
		bookProgress.setProgress(mPageManager.getPercent());
	}
	/**
	 * 加载当前页
	 * */
	private void LoadBookPage(){
		if(!lazyLoad){
			curPage = mPageManager.getCurrentPage();
			ViewGroup cur = getPageLayout(getPos(2));
			cur.removeAllViews();
			cur.addView(curPage);
			changeLayoutVisible(2);
		}else{

			LoadBookPage loadBookPage = new LoadBookPage();
			loadBookPage.setOnPostListener(new AsynTaskListener() {

				@Override
				public void onPostExecute(View result) {
					curPage = result;
					ViewGroup cur = getPageLayout(getPos(2));
					cur.removeAllViews();
					cur.addView(curPage);
					changeLayoutVisible(2);
				}

				@Override
				public View doInBackground(String... params) {
					return mPageManager.getCurrentPage();
				}
			});
			loadBookPage.execute(bookName);
		}

		

	}
	/**
	 * 加载下一页
	 * */
	private void loadPageNext(){
		if(!lazyLoad){
			nextPage = mPageManager.getNextPage();
			ViewGroup next = getPageLayout(getPos(3));
			next.removeAllViews();
			next.addView(nextPage);
		}else{
			LoadBookPage loadBookPage = new LoadBookPage();
			loadBookPage.setOnPostListener(new AsynTaskListener() {

				@Override
				public void onPostExecute(View result) {
					nextPage = result;

					ViewGroup next = getPageLayout(getPos(3));
					next.removeAllViews();
					next.addView(nextPage);

				}

				@Override
				public View doInBackground(String... params) {
					return mPageManager.getNextPage();
				}
			});
			loadBookPage.execute(bookName);
		}
	}
	/**
	 * 加载上一页
	 * */
	private void loadPagePre(){
		if(!lazyLoad){
			prePage = mPageManager.getPrePage();
			ViewGroup pre = getPageLayout(getPos(1));
			pre.removeAllViews();
			pre.addView(prePage);
		}else{
			LoadBookPage loadBookPage = new LoadBookPage();
			loadBookPage.setOnPostListener(new AsynTaskListener() {

				@Override
				public void onPostExecute(View result) {
					prePage = result;
					ViewGroup pre = getPageLayout(getPos(1));
					pre.removeAllViews();
					pre.addView(prePage);
				}

				@Override
				public View doInBackground(String... params) {
					return mPageManager.getPrePage();
				}
			});
			loadBookPage.execute(bookName);
		}
	}

	/**
	 * 显示下一页
	 * */
	private void showNextPage(){
		lazyLoad = false;
		System.out.println("showNextPage");
		changeLayoutVisible(3);

		mCurPos = mCurPos+1>3?1:mCurPos+1;
		loadPageNext();
	}

	private void changeLayoutVisible(int type){
		for(int i=1;i<=3;i++){
			ViewGroup group = getPageLayout(getPos(i));
			if(i!=type){
				group.setVisibility(View.INVISIBLE);
			}else{
				group.setVisibility(View.VISIBLE);
			}
		}
	}

	/**
	 * 显示上一页
	 * */
	private void showPrePage(){
		lazyLoad = false;
		System.out.println("showPrePage");
		changeLayoutVisible(1);
		mCurPos = mCurPos-1<1?3:mCurPos-1;
		loadPagePre();
	}
	/**
	 * 获取各个页面的位置
	 * */
	private int getPos(int type){
		int pos=0;
		//上一页
		if(type==1){
			int prePos = mCurPos - 1;
			pos = prePos>0?prePos:3;
		}
		//当前页页
		if(type==2){
			pos = mCurPos;
		}
		//下一页
		if(type==3){
			int prePos = mCurPos + 1;
			pos = prePos<=3?prePos:1;
		}

		return pos;
	}
	/**
	 * 根据位置返回容器
	 * */
	private ViewGroup getPageLayout(int pos){
		switch (pos) {
		case 1:
			return page1;
		case 2:
			return page2;
		case 3:
			return page3;
		default:
			return null;
		}
	}

}
