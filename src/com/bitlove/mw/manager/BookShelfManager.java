package com.bitlove.mw.manager;

import java.io.File;
import java.util.ArrayList;

import com.bitlove.mw.R;
import com.bitlove.mw.Util;
import com.bitlove.mw.activity.BookActivity;
import com.bitlove.mw.pojo.Book;
import com.bitlove.mw.remind.ToastReminder;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 书架管理器
 * */
public class BookShelfManager {
	private Context mContext;
	private LayoutInflater mInflater;
	private SQLiteManager mSqLiteManager;
	//书架管理器
	private static BookShelfManager mManager;
	//书架
	private GridLayout bookShelf;	
	//是否出于删除状态
	public boolean isInDel = false;
	
	private BookShelfManager(){}
	public static BookShelfManager getInstance(Context context){
		if(mManager==null){
			mManager = new BookShelfManager();
			mManager.mContext = context;
			mManager.initBookName();
		}
		mManager.mContext = context;
		return mManager;
	};
	//绑定要管理的书架对象
	public void attachBookShelf(GridLayout bookShelf){
		this.bookShelf = bookShelf;
	}
	/**
	 * 获取书架书籍
	 * */
	public ArrayList<Book> getBooks(){
		ArrayList<Book> books = new ArrayList<Book>();
		String files[]=null;
		
		//sqlite
		mSqLiteManager = getSqLiteManager();
		SQLiteDatabase db = mSqLiteManager.getSqLiteDatabase();
		String[] cols = new String[]{"book_name","book_imgid", "book_show_name","book_path","is_init"};
		Cursor cursor=db.query(mSqLiteManager.getTableBookShelfName(), cols, "", null, "", "","");
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			
			do{
				Book tl = new Book();
				tl.setBookName(cursor.getString(cursor.getColumnIndex("book_name")));
				tl.setBookShowName(cursor.getString(cursor.getColumnIndex("book_show_name")));
				tl.setBookPath(cursor.getString(cursor.getColumnIndex("book_path"))); 
				tl.setIsInit(cursor.getString(cursor.getColumnIndex("is_init")));
				tl.setBookImgId(cursor.getInt(cursor.getColumnIndex("book_imgid")));
				books.add(tl);
				cursor.moveToNext();
			}while(!cursor.isAfterLast());
		}
		
		cursor.close();
		
		return books;
	}
	
	/**
	 * 初始化资源书的名称
	 * */
	private void initBookName(){
		
		ArrayList<ContentValues> cvs = new ArrayList<ContentValues>();
		ContentValues cv = new ContentValues();
		cv.put("book_name", "bmxxf.txt");
		cv.put("book_show_name", "白马啸西风");
		cv.put("book_imgId", R.drawable.bmxxf);
		cv.put("is_init", "1");
		cvs.add(cv);
		
		ContentValues cv1 = new ContentValues();
		cv1.put("book_name", "bxj.txt");
		cv1.put("book_show_name", "碧血剑");
		cv1.put("book_imgId", R.drawable.bxj);
		cv1.put("is_init", "1");
		cvs.add(cv1);
		
		ContentValues cv2 = new ContentValues();
		cv2.put("book_name", "fhwz.txt");
		cv2.put("book_show_name", "飞狐外传");
		cv2.put("book_imgId", R.drawable.fhwz);
		cv2.put("is_init", "1");
		cvs.add(cv2);
		
		ContentValues cv3 = new ContentValues();
		cv3.put("book_name", "lcj.txt");
		cv3.put("book_show_name", "连城诀");
		cv3.put("book_imgId", R.drawable.lcj);
		cv3.put("is_init", "1");
		cvs.add(cv3);
		
		ContentValues cv4 = new ContentValues();
		cv4.put("book_name", "ldj.txt");
		cv4.put("book_show_name", "鹿鼎记");
		cv4.put("book_imgId", R.drawable.ldj);
		cv4.put("is_init", "1");
		cvs.add(cv4);
		
		ContentValues cv5 = new ContentValues();
		cv5.put("book_name", "sdxl.txt");
		cv5.put("book_show_name", "神雕侠侣");
		cv5.put("book_imgId", R.drawable.sdxl);
		cv5.put("is_init", "1");
		cvs.add(cv5);
		
		ContentValues cv6 = new ContentValues();
		cv6.put("book_name", "sdyxz.txt");
		cv6.put("book_show_name", "射雕英雄传");
		cv6.put("book_imgId", R.drawable.sdyxz);
		cv6.put("is_init", "1");
		cvs.add(cv6);
		
		ContentValues cv7 = new ContentValues();
		cv7.put("book_name", "sjecl.txt");
		cv7.put("book_show_name", "书剑恩仇录");
		cv7.put("book_imgId", R.drawable.sjecl);
		cv7.put("is_init", "1");
		cvs.add(cv7);
		
		ContentValues cv8 = new ContentValues();
		cv8.put("book_name", "tlbb.txt");
		cv8.put("book_show_name", "天龙八部");
		cv8.put("book_imgId", R.drawable.tlbb);
		cv8.put("is_init", "1");
		cvs.add(cv8);
		
		ContentValues cv9 = new ContentValues();
		cv9.put("book_name", "xajh.txt");
		cv9.put("book_show_name", "笑傲江湖");
		cv9.put("book_imgId", R.drawable.xajh);
		cv9.put("is_init", "1");
		cvs.add(cv9);
		
		ContentValues cv10 = new ContentValues();
		cv10.put("book_name", "xkx.txt");
		cv10.put("book_show_name", "侠客行");
		cv10.put("book_imgId", R.drawable.xkx);
		cv10.put("is_init", "1");
		cvs.add(cv10);
		
		ContentValues cv11 = new ContentValues();
		cv11.put("book_name", "xsfh.txt");
		cv11.put("book_show_name", "雪山飞狐");
		cv11.put("book_imgId", R.drawable.fhwz);
		cv11.put("is_init", "1");
		cvs.add(cv11);
		
		ContentValues cv12 = new ContentValues();
		cv12.put("book_name", "ynj.txt");
		cv12.put("book_show_name", "越女剑");
		cv12.put("book_imgId", R.drawable.ynj);
		cv12.put("is_init", "1");
		cvs.add(cv12);
		
		ContentValues cv13 = new ContentValues();
		cv13.put("book_name", "yttlj.txt");
		cv13.put("book_show_name", "倚天屠龙记");
		cv13.put("book_imgId", R.drawable.yttlj);
		cv13.put("is_init", "1");
		cvs.add(cv13);
		
		ContentValues cv14 = new ContentValues();
		cv14.put("book_name", "yyd.txt");
		cv14.put("book_show_name", "鸳鸯刀");
		cv14.put("book_imgId", R.drawable.yyd);
		cv14.put("is_init", "1");
		cvs.add(cv14);
		
		mSqLiteManager = getSqLiteManager();
		mSqLiteManager.initShelfDB(cvs);
		
	}
	/**
	 * 刷新书架
	 * */
	public void refreshShelf(){
		if(bookShelf==null){
			ToastReminder.showToast(mContext, "加载书籍失败", Toast.LENGTH_SHORT);
			return;
		}
		bookShelf.removeAllViews();
		initBookShelf();
	}
	/**
	 * 初始化书架书籍
	 * */
	public void initBookShelf(){
		if(bookShelf==null){
			ToastReminder.showToast(mContext, "加载书籍失败", Toast.LENGTH_SHORT);
			return;
		}
		mInflater = LayoutInflater.from(mContext);
		ArrayList<Book> books = getBooks();
		for(int i=0;i<books.size();i++){
			final Book book = books.get(i);
			final RelativeLayout layout = (RelativeLayout) mInflater.inflate(R.layout.view_book, null);
			TextView tvBookName = (TextView) layout.findViewById(R.id.bookName);
			tvBookName.setText(book.getBookShowName());
			
			ImageView img = (ImageView) layout.findViewById(R.id.bookImg);
			
			Drawable da = null;
			if(book.getBookImgId()!=0){
				da=mContext.getResources().getDrawable(book.getBookImgId());
			}
			if(da!=null){
				img.setImageDrawable(da);
			}
			final ImageView btnDel = (ImageView) layout.findViewById(R.id.btnDel);
			final View layoutImg = layout.findViewById(R.id.layoutImg);
			
			layoutImg.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(isInDel){
						btnDel.setTag(null);
						mManager.remoceFromShelf(layout,book.getBookName());
						isInDel = false;
					}else{
						Intent intent = new Intent(mContext,BookActivity.class);
						intent.putExtra("bookName", book.getBookName());
						mContext.startActivity(intent);
					}
				}
			});
			layoutImg.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					isInDel = true;
					Util.Vibrator();
					showDelIcon(btnDel);
					return true;
				}
			});
			
			
			bookShelf.addView(layout);
		}
	}
	private String getBookName(String bookeFileName){
		String bookName ="";
		ArrayList<Book> books = getBooks();
		Book book = null;
		
		for(int i=0;i<books.size();i++){
			if(books.get(i).getBookName().equals(bookeFileName)){
				book = books.get(i);
				bookName = book.getBookShowName();
			}
		}
		
		return bookName;
	}
	/**
	 * 根据书名获取书籍信息
	 * */
	public Book getBookByName(String bookName){
		ArrayList<Book> books = getBooks();
		Book book = null;
		
		for(int i=0;i<books.size();i++){
			if(books.get(i).getBookName().equals(bookName)){
				book = books.get(i);
			}
		}
		return book;
	}
	/**
	 * 添加本地文件至书架
	 * */
	public int addLocalFileToShelf(File file){
		
		mSqLiteManager = getSqLiteManager();
		int state = mSqLiteManager.addLocalFileToShelf(file);
		
		return state;
	}
	/**
	 * 从书架移除指定书籍
	 * */
	public void remoceFromShelf(View bookLayout,String bookName){
		mSqLiteManager = getSqLiteManager();
		mSqLiteManager.removeBook(bookName);
		bookShelf.removeView(bookLayout);
	}
	private SQLiteManager getSqLiteManager(){
		if(mSqLiteManager==null){
			mSqLiteManager = SQLiteManager.getInstance(mContext);
		}
		return mSqLiteManager;
	}
	/**
	 * 显示删除按钮
	 * */
	private void showDelIcon(ImageView btnDel){
		btnDel.setTag("deleTing");
		btnDel.setVisibility(View.VISIBLE);
	}
	/**
	 * 取消删除状态
	 * */
	public void cancelDel(){
		isInDel = false;
		View btnDel = bookShelf.findViewWithTag("deleTing");
		btnDel.setVisibility(View.INVISIBLE);
		btnDel.setTag(null);
	}
	
}
