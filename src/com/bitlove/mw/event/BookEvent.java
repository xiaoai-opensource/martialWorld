package com.bitlove.mw.event;

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;

public class BookEvent {
	private static BookEvent mBookEvent;
	private MotionEvent mEvent;	//事件
	private Display mDisplay;			//事件发生对象
	private float mStartX=0,mEndX=0,mStartY=0,mEndY=0;	//事件触发是的开始和结束位置
	private int mSlideMax=10;	//认为为滑动的最大值

	int mTargetWidth=0;
	float mRightPoint=0;
	float mLeftPoint=0;
	

	private BookEvent(){}
	public static BookEvent getInstance(MotionEvent event,Display display){
		if(mBookEvent==null){
			mBookEvent = new BookEvent();
		}
		mBookEvent.mEvent = event;
		mBookEvent.mDisplay = display;
		mBookEvent.init();
		return mBookEvent;
	}
	public void init(){
		DisplayMetrics dm = new DisplayMetrics();
		mDisplay.getMetrics(dm);
		//计算事件源的大小，将其左右分为三份，分别对应左、中、右
		mTargetWidth = dm.widthPixels;
		mRightPoint = (float) ((mTargetWidth * 3.0) / 5);
		mLeftPoint = (float) ((mTargetWidth * 2.0) / 5);
	}
	/**
	 * 分发屏幕点击事件
	 * */
	public void dispatchClickEvent(ClickEventListener evts){
		int action = mEvent.getAction();
		//事件源的X位置
		float evtX;

		if(action==MotionEvent.ACTION_DOWN){
			mStartX = mEvent.getX();
		}else if(action == MotionEvent.ACTION_UP){
			mEndX = mEvent.getX();
			//认为为滑动
			if(Math.abs(mEndX - mStartX)>mSlideMax){
				return;
			}
			evtX = mEvent.getX();			
			if(evtX>mRightPoint){
				evts.onRightClick();
			}else if(evtX<mLeftPoint){
				evts.onLeftClick();
			}else{
				evts.onCenterClick();
			}
		}
	}

	/**分发屏幕滑动事件
	 * 
	 * */
	public void dispatchSlideEvent(SlideEventLister evts){
		int action = mEvent.getAction();
		//事件源的X位置
		float evtX;

		if(action==MotionEvent.ACTION_DOWN){
			mStartX = mEvent.getX();
			mStartY = mEvent.getY();
		}else if(action == MotionEvent.ACTION_UP){
			mEndX = mEvent.getX();
			mEndY = mEvent.getY();
			float xLen = mEndX - mStartX;
			float yLen = mEndY - mStartY;
			//认为为 点击
			if(Math.abs(xLen)<mSlideMax && Math.abs(yLen)<mSlideMax){
				return;
			}
			//处理左右滑动
			if(xLen>0 && Math.abs(yLen)<mSlideMax){
				evts.onSlideRight();
				return;
			}else if(xLen<0 && Math.abs(yLen)<mSlideMax){
				evts.onSlideLeft();
				return;
			}
			//处理上下滑动
			if(yLen>0 && Math.abs(xLen)<mSlideMax){
				evts.onSlideDown();
			}else if(yLen<0 && Math.abs(xLen)<mSlideMax){
				evts.onSlideUp();
			}
			
			
		}
	}
	/**
	 * 屏幕点击事件
	 * */
	public interface ClickEventListener {
		public void onLeftClick();
		public void onRightClick();
		public void onCenterClick();
	}
	/**
	 * 滑动事件
	 * */
	public interface SlideEventLister{
		public void onSlideLeft();
		public void onSlideRight();
		public void onSlideUp();
		public void onSlideDown();
	}
}

