package com.androidgames.framework;

import android.graphics.Paint;

/**
 * Contains all the graphical manipulation methods of the framework
 */
public interface Graphics {
	
	/**
     * The format that the Pixmap will have, that may have alpha enabled or not  
     */
    public static enum PixmapFormat {
        ARGB8888, ARGB4444, RGB565
    }

    /**
     * Generates a new Pixel Map from the specified image file with the given PixmapFormat
     */
    public Pixmap newPixmap(String fileName, PixmapFormat format);

    
    /**
     * Clear the entire screen using the specified color
     */
    public void clear(int color);

    
    /**
     * Draws a single pixel using the Android native method
     */
    public void drawPixel(int x, int y, int color);
    
    
    /**
     * Draws a line using the Android native method
     */
    public void drawLine(int x, int y, int x2, int y2, int color);

    
    /**
     * Draws a filled rectangle using the Android native method
     */
    public void drawRect(int x, int y, int width, int height, int color);
    /**
     * Draws a filled rectangle using the Android native method with the specified alpha value
     */
    public void drawRect(int x, int y, int width, int height, int color, int alpha);
    
    
    /**
     * Draws a filled circle using the Android native method
     */
    public void drawCircle(int x, int y, int radius, int color);
    
    
    /**
     * Draws a text using the Android native method
     */
    public void drawText(String text, int x, int y, int color, float textSize);
    /**
     * Draws a text using the Android native method with the specified alpha value
     */
    public void drawText(String text, int x, int y, int color, float textSize, int alpha);
    
    
    /**
     * Draws a text whose caracters are Pixmaps
     */
    public void drawText(Graphics g, Pixmap characters, String line, int x, int y);
    /**
     * Draws a text whose caracters are Pixmaps, using a Paint to set the drawing attributes
     */
    public void drawText(Graphics g, Pixmap characters, String line, int x, int y, Paint paint);

    
    /**
     * Draws the Pixel Map cutted by the Src Rectangle in the Frame Buffer, using a Paint to set the drawing attributes
     */
    public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight, Paint paint);
    /**
     * Draws the Pixel Map cutted by the Src Rectangle in the Frame Buffer
     */
    public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight);
    /**
     * Draws the Pixel Map in the Frame Buffer, using a Paint to set the drawing attributes
     */
    public void drawPixmap(Pixmap pixmap, int x, int y, Paint paint);
    /**
     * Draws the Pixel Map in the Frame Buffer
     */
    public void drawPixmap(Pixmap pixmap, int x, int y);
    
    
    /**
     * Returns the virtual FrameBuffer width
     */
    public int getWidth();
    /**
     * Returns the virtual FrameBuffer height
     */
    public int getHeight();
}
