package com.bitlove.mw.view;

import com.bitlove.mw.manager.BookPageManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

public class BookPageView extends View {
	private Bitmap pageBitmap;
	private Context mContext;

	public BookPageView(Context context,Bitmap bitmap) {
		super(context);
		mContext = context;
		pageBitmap = bitmap;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(pageBitmap, 0,0, null);
	}

}
