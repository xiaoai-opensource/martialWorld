package com.bitlove.mw.view;

import com.bitlove.mw.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
/**
 * 关于界面
 * */
public class AboutDialog extends LinearLayout {

	public AboutDialog(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService
			      (Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.about_dialog, this);
	}

}
