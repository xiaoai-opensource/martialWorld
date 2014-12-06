package com.bitlove.mw.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SqLite管理
 * */
public class SQLiteManager {
	private Context mContext;
	private SQLiteDBHelper dbHelper;
	private SQLiteDatabase db;
	
	private final String DBNAME="bookShelf";
	private final String TABLE_BOOK_SHELF_NAME="bookShelf";
	
	private SQLiteManager(){};
	public static SQLiteManager mSqLiteManager;
	public static SQLiteManager getInstance(Context context) {
		if(mSqLiteManager==null){
			mSqLiteManager = new SQLiteManager();
		}
		mSqLiteManager.mContext = context;
		return mSqLiteManager;
	}
	public String getTableBookShelfName() {
		return TABLE_BOOK_SHELF_NAME;
	}
	/**
	 * 查询书架书籍
	 * */
	public Cursor queryShelfBooks(){
		Cursor cursor=null;
		
		return cursor;
	}
	/**
	 * 初始化书籍书架数据库
	 * */
	public void initShelfDB(ArrayList<ContentValues> cvs){
		if(dbHelper==null){
			dbHelper = new SQLiteDBHelper(mContext, DBNAME, null, 1);
		}
		if(cvs.size()==0){
			return ;
		}
		try{
			db = dbHelper.getWritableDatabase();
			db.beginTransaction();
			String[] cols = new String[]{"book_name"};
			Cursor cursor=db.query(mSqLiteManager.getTableBookShelfName(), cols, "", null, "", "","");
			if(cursor.getCount()>0){
				return;
			}
			for(int i=0;i<cvs.size();i++){
				db.insert(TABLE_BOOK_SHELF_NAME, null, cvs.get(i));
			}
			db.setTransactionSuccessful();
			
		}catch(Exception ex){
			
		}finally{
			if(db!=null){
				
				db.endTransaction();
			}
		}
	}
	/**
	 * 数据库
	 * */
	public SQLiteDatabase getSqLiteDatabase(){
		return db;
	}
	/**
	 * 更新指定书籍的路径
	 * */
	public void updateBookPathByName(String bookName,String path){
		try{
			db.beginTransaction();
			ContentValues cv = new ContentValues();
			cv.put("book_path", path);
			String[] whereArgs = new String[]{bookName};
			db.update(getTableBookShelfName(), cv, "book_name=?", whereArgs);
			db.setTransactionSuccessful();
		}catch(Exception ex){
			
		}finally{
			db.endTransaction();
		}
		
	}
	/**
	 * 添加本地数据至书架
	 * */
	public int addLocalFileToShelf(File file){
		int state =-1 ;
		if(dbHelper==null){
			dbHelper = new SQLiteDBHelper(mContext, DBNAME, null, 1);
		}
		try{
			db = dbHelper.getWritableDatabase();
			db.beginTransaction();
			
			String[] cols = new String[]{"book_name"};
			String[] selectArgs = new String[]{file.getPath()};
			Cursor cursor=db.query(mSqLiteManager.getTableBookShelfName(), cols, "book_path=?", selectArgs, "", "","");
			if(cursor.getCount()>0){
				state = -1;
				return state;
			}
			
			ContentValues cv = new ContentValues();
			String fileName = file.getName();
			cv.put("book_name", fileName);
			cv.put("book_show_name", fileName.substring(0, fileName.lastIndexOf(".")));
			cv.put("book_path", file.getPath());
			cv.put("is_init", "0");
			db.insert(TABLE_BOOK_SHELF_NAME, null, cv);
			db.setTransactionSuccessful();
			state = 1;
		}catch(Exception ex){
			state = 0;
			ex.printStackTrace();
		}finally{
			if(db!=null){
				
				db.endTransaction();
			}
		}
		
		return state;
	}
	/**
	 * 从书架移除指定书籍
	 * */
	public void removeBook(String bookName){
		int state =-1 ;
		if(dbHelper==null){
			dbHelper = new SQLiteDBHelper(mContext, DBNAME, null, 1);
		}
		try{
			db = dbHelper.getWritableDatabase();
			db.beginTransaction();
			
			String[] whereArgs = new String[]{bookName};
			db.delete(getTableBookShelfName(), "book_name=?", whereArgs);
			db.setTransactionSuccessful();
			state = 1;
		}catch(Exception ex){
			state = 0;
			ex.printStackTrace();
		}finally{
			if(db!=null){
				
				db.endTransaction();
			}
		}
	}
	
	class SQLiteDBHelper extends SQLiteOpenHelper{

		public SQLiteDBHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			StringBuilder sb = new StringBuilder();
			sb.append("create table [" + TABLE_BOOK_SHELF_NAME + "](");
			sb.append("[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,");
			sb.append("[book_name] TEXT,");
			sb.append("[book_show_name] TEXT,");
			sb.append("[book_imgid] INTEGER,");
			sb.append("[book_path] TEXT,");
			sb.append("[is_init] TEXT)");
			db.execSQL(sb.toString());
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
		
	}
}
