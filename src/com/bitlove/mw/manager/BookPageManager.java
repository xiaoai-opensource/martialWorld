package com.bitlove.mw.manager;


import java.io.UnsupportedEncodingException;
import java.util.Vector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.bitlove.mw.view.BookPageView;

/**
 * 翻页控制器
 * */
public class BookPageManager {
	private static BookPageManager mPageManager;
	private Context mContext;
	private WindowManager mWindowManager;
	private String mBookName ="";
	private PreferenceManager mPreference;

	private BookPageView prePage;		//上一页
	private BookPageView curPage;		//当前页
	private BookPageView nextPage;		//下一页
	private String state="-1";				//状态，首页0，尾页1

	public int screenWidth;
	public int screenHeight;
	public int mWidth;
	public int mHeight;
	public Paint mPaint;
	public int m_textColor = Color.BLACK;// 字体颜色
	public int m_backColor = 0xffff9e85; // 背景颜色
	public int marginWidth = 10; // 左右与边缘的距离
	public int marginHeight = 10; // 上下与边缘的距离

	public int mLineMargin = 10;//行间距
	public float mVisibleHeight; // 绘制内容的高
	public float mVisibleWidth; // 绘制内容的宽
	public int m_fontSize = 36;// 字体大小

	public Vector<String> mLines = new Vector<String>();;
	public int mLineCount; // 每页可以显示的行数
	public int mPageNum=0;		//当前页数
	public int mPos=0;
	public int mLineWords=0;
	public int mLineKey=0;
	private String tag="martialWorld";

	public static BookPageManager getInstance(Context context,WindowManager wm,String bookName){
		if(mPageManager==null){
			mPageManager = new BookPageManager();
			mPageManager.mContext = context;
			mPageManager.mWindowManager = wm;
			
			mPageManager.setScreenInfo();
			mPageManager.initBookPage();
		}
		mPageManager.mPreference = PreferenceManager.getInstance(context);
		if(!mPageManager.mBookName.equals(bookName)){
			mPageManager.mBookName = bookName;
			mPageManager.resetRecord();
		}
		
		
		return mPageManager;
	}
	private BookPageManager(){}

	/**
	 * 读取书籍记录信息
	 * */
	private void resetRecord(){
		mPageNum=Integer.parseInt(mPreference.read(mBookName+"PageNum", "0"));
		mLineWords=Integer.parseInt(mPreference.read(mBookName+"LineWords", "0"));
		mLineKey=Integer.parseInt(mPreference.read(mBookName+"LineKey", "0"));
		mPos = 0;
		mLines.removeAllElements();
	}
	/**
	 * 设置书籍记录信息
	 * */
	public void recordHis(){
		mPreference.record(mBookName+"PageNum", mPageNum+"");
		mPreference.record(mBookName+"LineWords", mLineWords+"");
		mPreference.record(mBookName+"LineKey", mLineKey+"");
	}
	/**
	 * 计算字符字节长度
	 * */
	private int getStringByteLen(String str){
		int len=0;
		if(str!=null){
			str = str.trim().replaceAll("[^\\x00-\\xff]" ,"**");
			len = str.length();
		}
		return len;
	}
	/**
	 * 获取当前进度
	 * */
	public String getPercent(){
		if("1".equals(state)){
			return "100.00";
		}
		FileManager fm = FileManager.getInstance(mContext);
		int maxLine = mPageNum*mLineCount<mLines.size()?mPageNum*mLineCount:mLines.size();
		float percent = 0;
		if(mLineWords==0){
			for(int i=0;i<maxLine;i++){
				mLineWords += getStringByteLen(mLines.get(i));
			}
			
		}else{
			if(mLineKey!=maxLine){
				if(mLineKey>maxLine){
					for(int i=mLineKey;i>=maxLine;i--){
						mLineWords -= getStringByteLen(mLines.get(i-1));
					}
				}else{
					for(int i=mLineKey;i<=maxLine;i++){
						mLineWords += getStringByteLen(mLines.get(i-1));
					}
				}	
			};
		}
		mLineKey = maxLine;
		if(mLineWords>=fm.getBufLen()){
			percent=1;
		}else{
			percent = ((float)(mLineWords))/fm.getBufLen();
		}
		percent *= 100;
		
		String result = String.format("%.2f",percent);
		return result;
	}
	public BookPageView nextPage(){

		return nextPage;
	}
	/**
	 * 获取下一页对象
	 * */
	public BookPageView getNextPage(){

		int maxLine = (mPageNum+1) * mLineCount;
		if(maxLine>mLines.size()){
			readNextPageContext();
		}
		Bitmap bitmap = Bitmap.createBitmap(screenWidth, screenHeight,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawColor(0xFFAAAAAA);
		int y = marginHeight;
		int beginLineNo = (mPageNum*mLineCount);
		int maxLines = beginLineNo+mLineCount<mLines.size()?beginLineNo+mLineCount:mLines.size();
		//到尾页了
		if(beginLineNo>=maxLines){
			state="1";
			if(curPage!=null){
				return curPage;
			}
		}else{
			state="-1";
		}
		
		for(int i=beginLineNo;i<maxLines;i++){
			y += mPaint.getTextSize()+mLineMargin;
			canvas.drawText(mLines.get(i), marginWidth, y, mPaint);
		}

		canvas.save();
		mPageNum++;	
		
		prePage = curPage;
		curPage = new BookPageView(mContext,bitmap);
		
		return curPage;
	}
	/**
	 * 获取下一页内容
	 * */
	public void readNextPageContext(){
		FileManager fm = FileManager.getInstance(mContext);
		for(int i=0;i<(mPageNum+1)*mLineCount;i++){
			byte[] buf = fm.readContext(mBookName, mPos);
			if(buf.length==0){
				state = "1";
				break;
			}
			mPos += buf.length;
			try {
				String strParagraph = new String(buf, fm.m_strCharsetName);
				while (strParagraph.length() > 0) {
					int nSize = mPaint.breakText(strParagraph, true, mVisibleWidth,
							null);
					mLines.add(strParagraph.substring(0, nSize));
					strParagraph = strParagraph.substring(nSize);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		}
	}
	/**
	 * 当前页
	 * */
	public BookPageView getCurrentPage(){
		if(mPageNum>0){
			mPageNum--;
		}
		BookPageView cur = getNextPage();
		return cur;
	}
	/**
	 * 上一页
	 * */
	public BookPageView getPrePage(){
		if(mPageNum<2){
			mPageNum=1;
			return curPage;
		}else{
			mPageNum = mPageNum-2;
		}
		return getNextPage();
	}
	/**
	 * 是否为首页
	 * */
	private boolean isFirstPage(){

		boolean isFirstPage = false;
		if(mPageNum==0){
			isFirstPage = true;
		}
		return isFirstPage;

	}
	/**
	 * 设置屏幕信息
	 * */
	public void setScreenInfo(){
		DisplayMetrics dm = new DisplayMetrics();
		mWindowManager.getDefaultDisplay().getMetrics(dm);
		
		screenHeight = dm.heightPixels;
		screenWidth = dm.widthPixels;
	}
	/**
	 * 初始化页面参数
	 * */
	private void initBookPage(){

		mWidth = screenWidth;
		mHeight = screenHeight;

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setTextAlign(Align.LEFT);
		mPaint.setColor(m_textColor);
		mPaint.setTextSize(m_fontSize);
		mVisibleWidth = mWidth - marginWidth * 2;
		mVisibleHeight = mHeight - marginHeight * 4;
		mLineCount = (int) (mVisibleHeight / (m_fontSize + mLineMargin)); // 可显示的行数
		
	}

}
