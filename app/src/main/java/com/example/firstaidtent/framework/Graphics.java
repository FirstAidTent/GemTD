package com.example.firstaidtent.framework;

import android.graphics.Bitmap;
import android.graphics.Paint;

public interface Graphics {
    enum ImageFormat {
        ARGB8888, ARGB4444, RGB565
    }

    Image newImage(String fileName, ImageFormat format);

    Bitmap newBitmap(String fileName, ImageFormat format);

    void clearScreen(int color);

    void drawLine(int x, int y, int x2, int y2, int color);

    void drawRect(int x, int y, int width, int height, int color);

    void drawRect(int x, int y, int width, int height, int color, boolean fill);

    void drawCircle(int x, int y, double radius, int color, boolean fill);

    void drawImage(Image image, int x, int y, int srcX, int srcY,
                   int srcWidth, int srcHeight);

    void drawImage(Image Image, int x, int y);

    void drawString(String text, int x, int y, Paint paint);

    int getWidth();

    int getHeight();

    void drawARGB(int i, int j, int k, int l);

}