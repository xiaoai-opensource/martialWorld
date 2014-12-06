package com.bitlove.mw.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import com.bitlove.mw.pojo.Book;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

public class FileManager {
	private Context mContext;
	private File mFile;
	private RandomAccessFile localFile = null;
	private MappedByteBuffer m_mbBuf = null;
	private static FileManager mFileManager;
	private SQLiteManager mSqLiteManager;
	private String mFileName="";
	
	public String m_strCharsetName = "GBK";
	private int m_mbBufLen = 0;
	
	private String tag="martialWorld";
	
	private FileManager(){};
	public static FileManager getInstance(Context context){
		if(mFileManager==null){
			mFileManager = new FileManager();
		}
		mFileManager.mContext = context;
		return mFileManager;
	}
	
	
	public RandomAccessFile getFile(String fileName){
		BookShelfManager mShelfManager = BookShelfManager.getInstance(mContext);
		Book book = mShelfManager.getBookByName(fileName);
		mFileName = fileName;
		try {
			String localFilePath = "";
			if(book.getBookPath()==null || "".equals(book.getBookPath())){
				if("1".equals(book.getIsInit())){
					localFilePath = getFileFromAssets(book.getBookName());
					mSqLiteManager = SQLiteManager.getInstance(mContext);
					mSqLiteManager.updateBookPathByName(fileName, localFilePath);
				}else{
					System.out.println("未找到文件路径");
				}
			}else{
				localFilePath = book.getBookPath();
			}
			localFile = new RandomAccessFile(localFilePath, "r");
			m_mbBufLen = (int) localFile.length();
			m_mbBuf = localFile.getChannel().map(FileChannel.MapMode.READ_ONLY, 0,m_mbBufLen);
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}   
		
		return localFile;
	}
	public byte[] readContext(String fileName,int begin){
		if(localFile==null || !mFileName.equals(fileName)){
			getFile(fileName);
		}
		int nStart = begin;
		int i = nStart;
		byte b0, b1;
		// 根据编码格式判断换行
		if (m_strCharsetName.equals("UTF-16LE")) {
			while (i < m_mbBufLen - 1) {
				b0 = m_mbBuf.get(i++);
				b1 = m_mbBuf.get(i++);
				if (b0 == 0x0a && b1 == 0x00) {
					break;
				}
			}
		} else if (m_strCharsetName.equals("UTF-16BE")) {
			while (i < m_mbBufLen - 1) {
				b0 = m_mbBuf.get(i++);
				b1 = m_mbBuf.get(i++);
				if (b0 == 0x00 && b1 == 0x0a) {
					break;
				}
			}
		} else {
			while (i < m_mbBufLen) {
				b0 = m_mbBuf.get(i++);
				if (b0 == 0x0a) {
					break;
				}
			}
		}
		int nParaSize = i - nStart;
		byte[] buf = new byte[nParaSize];
		for (i = 0; i < nParaSize; i++) {
			buf[i] = m_mbBuf.get(begin + i);
		}
		
		
		return buf;
		
		
	}
	
	/**
	 * 从资源中读取文件
	 * */
	public String getFileFromAssets(String bookName){
		
		String localPath = "";
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			localPath = Environment.getExternalStorageDirectory().getPath() + "/MW";
			File dir = new File(localPath);
			if(!dir.exists()){
				dir.mkdirs();
			}
			localPath = localPath + "/" +bookName;
		}else{
			localPath = mContext.getFilesDir() + "/"+ bookName;
		}
		String assetPath = "books/";
		InputStream fileIS = null;
		FileOutputStream fos =null;
		byte bytes[] = new byte[102400];		
		
		File localFile = new File(localPath);
		
		if(localFile.exists()){
			return localPath;
		}
		AssetManager am = mContext.getResources().getAssets();
		try {
			
			fileIS = am.open(assetPath+bookName);
			fos = new FileOutputStream(localFile);
			int len=-1;
			while((len=fileIS.read(bytes))>0){
				fos.write(bytes,0,len);
			}
			
		} catch (IOException e) {
			localPath = "";
			e.printStackTrace();
		}finally{
			try {
				if(fos!=null){
					
					fos.close();
				}
				if(fileIS!=null){
					
					fileIS.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return localPath;		
	}
	/**
	 * 获取总长度
	 * */
	public int getBufLen(){
		return m_mbBufLen;
	}
	/**
	 * 格式化文件长度格式
	 * */
	public static String formatSize(long len){
		String size = "";
		String unit = "Byte";
		int radixLen = 1024;
		float tmpLen =0;
		float fLen = (float)len;
		if(len>radixLen*radixLen){
			tmpLen = fLen/(radixLen*radixLen);
			unit = "MB";
			size = String.format("%.1f",tmpLen);
		}else if(len>radixLen){
			tmpLen = fLen/radixLen;
			unit = "KB";
			size = String.format("%.1f",tmpLen);
		}else{
			size = fLen+"";
		}
		
		size += unit;
		return size;
	}
}
