package com.example.engine_android;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.engine_common.interfaces.IImage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ImageAndroid implements IImage {
    //parameter info of the image
    Bitmap image;

    public ImageAndroid(String path, AssetManager assetManager) {
        //loads the image and stores its info
        try {
            InputStream is = assetManager.open(path);
            image = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //getters for the image info, and its size
    public Bitmap getImage() {
        return image;
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }
}
