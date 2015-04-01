package com.bitlove.mw.activity;

import com.bitlove.mw.R;
import com.bitlove.mw.R.anim;
import com.bitlove.mw.event.BookEvent;
import com.bitlove.mw.event.BookEvent.ClickEventListener;
import com.bitlove.mw.manager.BookPageManager;
import com.bitlove.mw.service.LoadBookPage;
import com.bitlove.mw.service.LoadBookPage.AsynTaskListener;
import com.bitlove.mw.view.Progress;

import android.R.animator;
import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * 阅读书
 * */
public class BookActivity extends BaseActivity {
	private BookPageManager mPageManager;
	private ViewGroup readLayout;	//阅读文字界面
	private Progress bookProgress;		//进度
	private String bookName;
	private ImageView loadingImg;

	ViewGroup page1;
	ViewGroup page2;
	ViewGroup page3;

	View prePage;
	View curPage;
	View nextPage;
	private int mCurPos=2;	//1:pre,2:cur,3:next
	public boolean lazyLoad = false;

	Animation toRightAnimation;
	Animation toLeftAnimation;
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
		startLoadingAnim();
		LoadBookPage();
		loadPageNext();
		loadPagePre();
	}
	@Override
	protected void onPause() {
		super.onPause();
		stopLoadingAnim();
		mPageManager.recordHis();
	}

	private void init(){

		loadingImg = (ImageView) findViewById(R.id.animLoadingImg);
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
		
		toRightAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_out_to_right);
		toLeftAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_out_to_left);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if(keyCode==KeyEvent.KEYCODE_VOLUME_UP){
			showPrePage();
			return true;
		}else if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN){
			showNextPage();

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
		}else{

			LoadBookPage loadBookPage = new LoadBookPage();
			loadBookPage.setOnPostListener(new AsynTaskListener() {

				@Override
				public void onPostExecute(View result) {
					curPage = result;
					ViewGroup cur = getPageLayout(getPos(2));
					cur.removeAllViews();
					cur.addView(curPage);
					stopLoadingAnim();
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
		changeLayoutVisible(3);

		mCurPos = mCurPos+1>3?1:mCurPos+1;
		loadPageNext();
	}

	private void changeLayoutVisible(int type){
		ViewGroup target = getPageLayout(getPos(type));
		target.setVisibility(View.VISIBLE);
		final ViewGroup cur = getPageLayout(getPos(2));
		System.out.println("pre = " + getPos(1) );
		System.out.println("cur = " + getPos(2) );
		System.out.println("next = " + getPos(3) );
		ViewGroup hide = null;
		Animation animation = null;
		
		switch (type) {
		case 1:
			animation = toRightAnimation;
			hide = getPageLayout(getPos(3));
			
			break;
		case 3:
			hide = getPageLayout(getPos(1));
			animation = toLeftAnimation;
			break;
		default:
			hide = getPageLayout(getPos(1));
			animation = toLeftAnimation;
			break;
		}
		
		hide.setVisibility(View.INVISIBLE);
		
		cur.setAnimation(animation);
		animation.start();
		animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				cur.setVisibility(View.INVISIBLE);
			}
		});
		
	}

	/**
	 * 显示上一页
	 * */
	private void showPrePage(){
		lazyLoad = false;
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
	
	private void startLoadingAnim(){
		AnimationDrawable animationDrawable = (AnimationDrawable) loadingImg.getDrawable();  
        animationDrawable.start();
	}
	private void stopLoadingAnim(){
		AnimationDrawable animationDrawable = (AnimationDrawable) loadingImg.getDrawable();  
        animationDrawable.stop();
	}

}
