package com.androidgames.framework;

import android.graphics.Bitmap;

import com.androidgames.framework.Graphics.PixmapFormat;

public interface Pixmap {
	public Bitmap getBitmap();
	
	public void setBitmap(Bitmap bmp);
	
    public int getWidth();

    public int getHeight();

    public PixmapFormat getFormat();

    public void dispose();
}
