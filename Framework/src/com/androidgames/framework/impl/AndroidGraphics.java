package com.androidgames.framework.impl;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.Paint.Style;

import com.androidgames.framework.Graphics;
import com.androidgames.framework.Pixmap;

public class AndroidGraphics implements Graphics {
    AssetManager assets;
    Bitmap frameBuffer;
    Canvas canvas;
    Paint paint;
    Rect srcRect = new Rect();
    Rect dstRect = new Rect();

    public AndroidGraphics(AssetManager assets, Bitmap frameBuffer) {
        this.assets = assets;
        this.frameBuffer = frameBuffer;
        this.canvas = new Canvas(frameBuffer);
        this.paint = new Paint();
    }

    @Override
    public Pixmap newPixmap(String fileName, PixmapFormat format) {
        Config config = null;
        if (format == PixmapFormat.RGB565)
            config = Config.RGB_565;
        else if (format == PixmapFormat.ARGB4444)
            config = Config.ARGB_4444;
        else
            config = Config.ARGB_8888;

        Options options = new Options();
        options.inPreferredConfig = config;

        InputStream in = null;
        Bitmap bitmap = null;
        try {
            in = assets.open(fileName);
            bitmap = BitmapFactory.decodeStream(in);
            if (bitmap == null)
                throw new RuntimeException("Couldn't load bitmap from asset '" + fileName + "'");
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load bitmap from asset '" + fileName + "'");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }

        if (bitmap.getConfig() == Config.RGB_565)
            format = PixmapFormat.RGB565;
        else if (bitmap.getConfig() == Config.ARGB_4444)
            format = PixmapFormat.ARGB4444;
        else
            format = PixmapFormat.ARGB8888;

        return new AndroidPixmap(bitmap, format);
    }

    @Override
    public void clear(int color) {
        canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8, (color & 0xff));
    }

    @Override
    public void drawPixel(int x, int y, int color) {
        paint.setColor(color);
        canvas.drawPoint(x, y, paint);
    }

    @Override
    public void drawLine(int x, int y, int x2, int y2, int color) {
        paint.setColor(color);
        canvas.drawLine(x, y, x2, y2, paint);
    }

    @Override
    public void drawRect(int x, int y, int width, int height, int color) {
        paint.setColor(color);
        paint.setStyle(Style.FILL);
        canvas.drawRect(x, y, x + width, y + height, paint);
    }
    
    @Override
    public void drawRect(int x, int y, int width, int height, int color, int alpha) {
    	paint.setColor(color);
    	paint.setAlpha(alpha);
        paint.setStyle(Style.FILL);
        canvas.drawRect(x, y, x + width, y + height, paint);
    }
    
    @Override
    public void drawCircle(int cx, int cy, int radius, int color) {
        paint.setColor(color);
        paint.setStyle(Style.FILL);
        canvas.drawCircle(cx, cy, radius, paint);
    }
    
    @Override
    public void drawText(String text, int x, int y, int color, float textSize){
    	paint.setColor(color);
    	paint.setTextSize(textSize);
    	canvas.drawText(text, x, y, paint);
    }
    
    @Override
    public void drawText(String text, int x, int y, int color, float textSize, int alpha){
    	paint.setColor(color);
    	paint.setTextSize(textSize);
    	paint.setAlpha(alpha);
    	canvas.drawText(text, x, y, paint);
    }
    
    @Override
    public void drawText(Graphics g, Pixmap characters, String line, int x, int y) {
    	drawText(g, characters, line, x, y, null);
		
    }
    
    @Override
    public void drawText(Graphics g, Pixmap characters, String line, int x, int y, Paint paint) {
    	
		final int NUMBERS_SRC_Y = 32;
    	final int LETTERS_SRC_Y = 0;
    	
        int len = line.length();
        for (int i = 0; i < len; i++) {
            char character = line.charAt(i);

            if (character == ' ') {
                x += 20;
                continue;
            }

            int srcX = 0;
            int srcWidth = 0;
            if (character == '.') {
                srcX = 200;
                srcWidth = 10;
                g.drawPixmap(characters, x, y, srcX, NUMBERS_SRC_Y, srcWidth, 32, paint);
            } else if (character >='0' && character <= '9') {
                srcX = (character - '0') * 20;
                srcWidth = 20;
                g.drawPixmap(characters, x, y, srcX, NUMBERS_SRC_Y, srcWidth, 32, paint);
            } else if (character >='a' && character <= 'z') { 
            	srcX = (character - 'a') * 20;
                srcWidth = 20;
                g.drawPixmap(characters, x, y, srcX, LETTERS_SRC_Y, srcWidth, 32, paint);
            } else if (character >='A' && character <= 'Z') {
            	srcX = (character - 'A') * 20;
                srcWidth = 20;
                g.drawPixmap(characters, x, y, srcX, LETTERS_SRC_Y, srcWidth, 32, paint);
            }

            x += srcWidth;
        }
    }
    
    @Override
    public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight, Paint paint) {
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth - 1;
        srcRect.bottom = srcY + srcHeight - 1;

        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + srcWidth - 1;
        dstRect.bottom = y + srcHeight - 1;
        
        canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, srcRect, dstRect, paint);
    }
    
    @Override
    public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight) {
    	drawPixmap(pixmap, x, y, srcX, srcY, srcWidth, srcHeight, null);
    }
    
    
    @Override
    public void drawPixmap(Pixmap pixmap, int x, int y, Paint paint) {
        canvas.drawBitmap(((AndroidPixmap)pixmap).bitmap, x, y, paint);
    }
    
    @Override
    public void drawPixmap(Pixmap pixmap, int x, int y) {
    	drawPixmap(pixmap, x, y, null);
    }

    @Override
    public int getWidth() {
        return frameBuffer.getWidth();
    }

    @Override
    public int getHeight() {
        return frameBuffer.getHeight();
    }
}
