package com.bitlove.mw.manager;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;

/**
 * 手机文件路径管理
 * */
public class FilePathManager {

	private Context mContext;
	private FilePathManager(){};
	private static FilePathManager mFilePathManager;

	public static FilePathManager getInstance(Context context){
		if(mFilePathManager==null){
			mFilePathManager = new FilePathManager();
		}
		
		mFilePathManager.mContext = context;
		return mFilePathManager;
	}
	/**
	 * 获取SDCard根路径
	 * */
	public String getExRootPath(){
		String root="";
		File rootFile = Environment.getExternalStorageDirectory();
		if(rootFile!=null){
			root = rootFile.getPath();
		}
		return root;
	}
	/**
	 * 获取指定路径下的文件
	 * */
	public ArrayList<File> getTxtFiles(String path){
		File[] subFiles=null;
		ArrayList<File> fileArr = new ArrayList<File>();
		File file = new File(path);
		if(file!=null){
			subFiles = file.listFiles();
		}
		if(subFiles!=null){
			for(int i=0;i<subFiles.length;i++){
				File tmp = subFiles[i];
				if(tmp.isDirectory() || tmp.getName().lastIndexOf(".txt")>0){
					fileArr.add(tmp);
				}
			}
		}
		return fileArr;
	}
}
