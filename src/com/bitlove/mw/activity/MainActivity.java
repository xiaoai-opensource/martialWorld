package com.bitlove.mw.activity;

import com.bitlove.mw.R;
import com.bitlove.mw.R.layout;
import com.bitlove.mw.R.menu;
import com.bitlove.mw.manager.BookShelfManager;
import com.bitlove.mw.manager.DialogManager;
import com.bitlove.mw.remind.ToastReminder;

import android.os.Bundle;
import android.provider.CalendarContract.Reminders;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.GridLayout;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
	private GridLayout bookShelf;	//书架
	private BookShelfManager mBookShelfManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}
	@Override
	protected void onResume() {
		super.onResume();
		mBookShelfManager = BookShelfManager.getInstance(mContext);
		mBookShelfManager.refreshShelf();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_file_add, menu);
		return true;
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_addLocalFile:
			//添加本地文件
			startActivity(LocalFileAddActivity.class);
			break;
		case R.id.menu_about:
			Activity activity = this;
			while(activity.getParent()!=null){
				activity = activity.getParent();
			}
			DialogManager mDialogManager = DialogManager.getInstance(activity);
			mDialogManager.showDialog(R.layout.about_dialog);
			break;
		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	private void init(){
		bookShelf = (GridLayout) findViewById(R.id.bookShelf);
		mBookShelfManager = BookShelfManager.getInstance(mContext);
		mBookShelfManager.attachBookShelf(bookShelf);
		mBookShelfManager.initBookShelf();
	}
	

}
