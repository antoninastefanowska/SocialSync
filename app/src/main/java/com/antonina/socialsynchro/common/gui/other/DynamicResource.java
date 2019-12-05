package com.antonina.socialsynchro.common.gui.other;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;

import java.util.Map;
import java.util.TreeMap;

public class DynamicResource {
    private Drawable standard;
    private Drawable darkened;
    private Drawable grayscale;

    private static Map<Integer, DynamicResource> instances = new TreeMap<>();

    private DynamicResource(Drawable standard, Drawable darkened, Drawable grayscale) {
        this.standard = standard;
        this.darkened = darkened;
        this.grayscale = grayscale;
    }

    private static void create(int resourceID, Context context) {
        Drawable standard = context.getResources().getDrawable(resourceID);
        Drawable grayscale = standard.getConstantState().newDrawable().mutate();
        Drawable darkened = standard.getConstantState().newDrawable().mutate();

        LightingColorFilter darkenColorFilter = new LightingColorFilter(0xFF7F7F7F, 0x00000000);
        darkened.setColorFilter(darkenColorFilter);

        ColorMatrix grayscaleMatrix = new ColorMatrix();
        grayscaleMatrix.setSaturation(0f);
        ColorMatrixColorFilter grayscaleFilter = new ColorMatrixColorFilter(grayscaleMatrix);
        grayscale.setColorFilter(grayscaleFilter);

        DynamicResource newInstance = new DynamicResource(standard, darkened, grayscale);
        instances.put(resourceID, newInstance);
    }

    public static DynamicResource getDynamicResource(int resourceID, Context context) {
        if (instances.get(resourceID) == null)
            create(resourceID, context);
        return instances.get(resourceID);
    }

    public static void free() {
        instances.clear();
    }

    public Drawable getStandard() {
        return standard;
    }

    public Drawable getDarkened() {
        return darkened;
    }

    public Drawable getGrayscale() {
        return grayscale;
    }
}
