package com.bitlove.mw.activity;

import java.io.File;
import java.util.ArrayList;

import com.bitlove.mw.R;
import com.bitlove.mw.manager.BookShelfManager;
import com.bitlove.mw.manager.FileManager;
import com.bitlove.mw.manager.FilePathManager;
import com.bitlove.mw.remind.ToastReminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 本地文件添加
 * */
public class LocalFileAddActivity extends BaseActivity {
	private FilePathManager mFilePathManager;
	private BookShelfManager mShelfManager;
	private ListView listFiles;
	private ArrayList<File> files;	//当前路径下的文件列表
	private TextView tvPath;		//路径
	private TextView btnUpLevel;	//上一级
	private BaseAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_local_file);
		showUpAction();
		init();
	}
	
	private void init(){
		mShelfManager = BookShelfManager.getInstance(mContext);
		mFilePathManager = FilePathManager.getInstance(mContext);
		tvPath = (TextView) findViewById(R.id.tvPath);
		btnUpLevel = (TextView)findViewById(R.id.btnUpLevel);
		
		listFiles = (ListView) findViewById(R.id.listFiles);
		
		adapter = (BaseAdapter)listFiles.getAdapter();
		updatePath(mFilePathManager.getExRootPath());
		
		//设置点击事件
		listFiles.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				File file = files.get(position);
				if(file.isDirectory()){
					String filePath = file.getPath();
					updatePath(filePath);
				}else{
					int state = mShelfManager.addLocalFileToShelf(file);
					if(state==1){
						ToastReminder.showToast(mContext, "添加文件至书架成功",Toast.LENGTH_SHORT); 
					}else if(state==-1){
						ToastReminder.showToast(mContext, "文件已在书架",Toast.LENGTH_SHORT); 
					}else{
						ToastReminder.showToast(mContext, "添加文件至书架失败",Toast.LENGTH_SHORT); 
					};
				}
				
			}
		});
		//设置数据
		listFiles.setAdapter(new BaseAdapter() {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				File file = files.get(position);
				RelativeLayout layout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.item_file_path, null);
				//名称
				TextView tv = (TextView) layout.findViewById(R.id.tvFileName);
				tv.setText(file.getName());
				//大小
				TextView tvSize = (TextView) layout.findViewById(R.id.tvFileSize);
				//图标
				ImageView img = (ImageView) layout.findViewById(R.id.imgIcon);
				if(file.isFile()){
					img.setImageDrawable(getResources().getDrawable(R.drawable.icon_txtfile));
					tvSize.setText(FileManager.formatSize(file.length()));
				}else{
					tvSize.setVisibility(View.GONE);
				}
				return layout;
			}
			
			@Override
			public long getItemId(int position) {
				return position;
			}
			
			@Override
			public Object getItem(int position) {
				return files.get(position);
			}
			
			@Override
			public int getCount() {
				return files.size();
			}
		});
		
		//上一级
		btnUpLevel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				upLevel();
			}
		});
	}
	/**
	 * 更新路径
	 * */
	private void updatePath(String path){
		getFiles(path);
		tvPath.setText(path);
		adapter = (BaseAdapter) listFiles.getAdapter();
		if(adapter!=null){
			adapter.notifyDataSetChanged();
		}
		
	}
	//返回上一级
	private void upLevel(){
		String path = (String) tvPath.getText();
		
		File file = new File(path);
		if(file!=null){
			File pfile = file.getParentFile();
			if(pfile!=null){
				updatePath(file.getParentFile().getPath());
			}
		}
	}
	//获取文件列表
	private void getFiles(String path){
		files=mFilePathManager.getTxtFiles(path);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			 Intent intent = new Intent(this, MainActivity.class);  
		     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
		     startActivity(intent);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
