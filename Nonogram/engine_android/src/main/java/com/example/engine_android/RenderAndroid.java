package com.example.engine_android;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.engine_common.interfaces.IRender;
import com.example.engine_common.interfaces.IScene;
import com.example.engine_common.shared.FontType;

import java.io.File;
import java.util.HashMap;



public class RenderAndroid implements IRender {
    //android graphic variables
    private SurfaceView myView;
    private SurfaceHolder holder;
    private Canvas canvas;
    private Paint paint;

    //resource managers
    private HashMap<String, ImageAndroid> images;
    private HashMap<String, FontAndroid> fonts;
    private AssetManager assetManager;

    //screen orientation info
    private boolean verticalScreen;

    //canvas position info
    private int posCanvasX, posCanvasY;

    //canvas scale, height and width
    private int baseWidth;
    private int baseHeight;
    private float scale;

    public RenderAndroid(SurfaceView myView, AssetManager aM, float ratio) {
        //
        this.myView = myView;
        this.holder = this.myView.getHolder();
        this.assetManager = aM;
        this.paint = new Paint();
        this.fonts = new HashMap<>();
        this.images = new HashMap<>();
        this.verticalScreen = true;

        //initializes canvas values
        this.scale = ratio;
    }

    public void scaleApp() {
        while(holder.getSurfaceFrame().width() == 0);
        //400x600
        //x----y
        float w = this.holder.getSurfaceFrame().width();
        float y = this.holder.getSurfaceFrame().height();
        float scaleX = w;
        float scaleY = y;
        if (scaleX * this.scale < scaleY) scaleY = scaleX / scale;
        else scaleX = scaleY * this.scale;
        this.posCanvasX = (int) (w - scaleX) / 2;
        this.posCanvasY = (int) (y - scaleY) / 2;
        this.baseWidth = (int)scaleX;
        this.baseHeight = (int)scaleY;
    }

    public boolean surfaceValid() {
        return this.holder.getSurface().isValid();
    }

    public void clear() {
        this.canvas = this.holder.lockCanvas();
        this.canvas.drawColor(0xFFFFFFFF);
        this.canvas.translate(this.posCanvasX, this.posCanvasY);
        setColor(0xFFFFFFFF);
        drawRectangle(0, 0, this.baseWidth, this.baseHeight, true);
    }

    public void present() {
        this.holder.unlockCanvasAndPost(this.canvas);
    }

    @Override
    public String loadImage(String filePath) {
        File imageFile = new File(filePath);
        String convFilepath = filePath.replaceAll("./assets/", "");
        if(!this.images.containsKey(imageFile.getName()))
            this.images.put(imageFile.getName(), new ImageAndroid(convFilepath, this.assetManager));
        return imageFile.getName();
    }

    @Override
    public String loadFont(String filePath, FontType type, int size) {
        File fontFile = new File(filePath);
        String fontID = fontFile.getName() + type.toString() + size;
        String convFilepath = filePath.replaceAll("./assets/", "");
        if(!this.fonts.containsKey(fontID))
            this.fonts.put(fontID, new FontAndroid(convFilepath, this.assetManager, size, type));
        return fontID;
    }

    @Override
    public void setColor(int hexColor) {
        this.paint.setColor(hexColor);
    }

    @Override
    public void setFont(String fontID) {
        FontAndroid font = this.fonts.get(fontID);
        this.paint.setTypeface(font.getFont());
        this.paint.setTextSize(font.getSize());
    }

    @Override
    public void drawLine(int og_x, int og_y, int dst_x, int dst_y) {
        this.canvas.drawLine(og_x, og_y, dst_x, dst_y, this.paint);
    }

    @Override
    public void drawRectangle(int x, int y, int width, int height, boolean fill) {
        this.paint.setStyle(fill ? Paint.Style.FILL_AND_STROKE : Paint.Style.STROKE);
        this.canvas.drawRect(x, y, x + width, y + height, this.paint);
    }

    @Override
    public void drawCircle(int x, int y, int r, boolean fill) {
        this.paint.setStyle(fill ? Paint.Style.FILL_AND_STROKE : Paint.Style.STROKE);
        this.canvas.drawCircle(x, y, r, this.paint);
    }

    @Override
    public void drawImage(int x, int y, int width, int height, String imageID) {
        Bitmap image = this.images.get(imageID).getImage();
        Rect src = new Rect(0,0,image.getWidth(), image.getHeight());
        Rect dst = new Rect(x, y, x + width, y+height);
        this.canvas.drawBitmap(image, src, dst, this.paint);
    }

    @Override
    public void drawText(int x, int y, String text) {
        this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.canvas.drawText(text, x, y, this.paint);
    }

    @Override
    public int getTextWidth(String fontID, String text) {
        Typeface prev_font = this.paint.getTypeface();
        FontAndroid font = this.fonts.get(fontID);

        this.paint.setTypeface(font.getFont());
        float width = this.paint.measureText(text);
        this.paint.setTypeface(prev_font);

        return (int)width;
    }

    @Override
    public int getTextHeight(String fontID) { return fonts.get(fontID).getSize(); }

    @Override
    public int getWidth() { return this.baseWidth; }

    @Override
    public int getHeight() { return this.baseHeight; }

    public int getViewWidth() { return this.myView.getWidth(); }

    public int getViewHeight() { return this.myView.getHeight(); }
}