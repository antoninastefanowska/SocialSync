package com.antonina.socialsynchro.common.gui.other;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.antonina.socialsynchro.R;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

public class MaskTransformation extends BitmapTransformation {
    private static Paint maskPaint = new Paint();
    private Context context;
    private int maskImageID;

    static {
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    }

    public MaskTransformation(Context context) {
        this.context = context.getApplicationContext();
        maskImageID = R.drawable.mask_avatar;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap result = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        Drawable mask = context.getResources().getDrawable(maskImageID);
        Canvas canvas = new Canvas(result);

        mask.setBounds(0, 0, outWidth, outHeight);
        mask.draw(canvas);
        canvas.drawBitmap(toTransform, 0, 0, maskPaint);

        return result;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update("mask transformation".getBytes());
    }
}
