package com.antonina.socialsynchro.common.gui.other;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.DrawableTransformation;

import java.security.MessageDigest;

public class GrayscaleTransformation extends BitmapTransformation {
    private static Paint paint = new Paint();

    static {
        ColorMatrix grayscaleColorMatrix = new ColorMatrix();
        grayscaleColorMatrix.setSaturation(0f);
        ColorMatrixColorFilter grayscaleFilter = new ColorMatrixColorFilter(grayscaleColorMatrix);
        paint.setColorFilter(grayscaleFilter);
    }


    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap result = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);

        canvas.drawBitmap(toTransform, 0, 0, paint);
        return result;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update("grayscale transformation".getBytes());
    }
}
