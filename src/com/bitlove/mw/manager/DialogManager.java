package com.bitlove.mw.manager;

import com.bitlove.mw.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * 模态框
 * */
public class DialogManager {
	private static DialogManager mDialogManager;
	private static Builder mDialog;
	private Context mContext;
	private DialogManager() {}
	public static DialogManager getInstance(Context context){
		if(mDialogManager==null){
			mDialogManager = new DialogManager();
		}
		mDialog = new AlertDialog.Builder(context);
		
		mDialogManager.mContext = context;
		return mDialogManager;
	}
	
	public void showDialog(int viewId){
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService
			      (Context.LAYOUT_INFLATER_SERVICE);
		View view=inflater.inflate(viewId, null);
		
		mDialog.setView(view);
		mDialog.show();
	}

}
