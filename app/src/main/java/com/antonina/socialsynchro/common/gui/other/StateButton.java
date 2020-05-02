package com.antonina.socialsynchro.common.gui.other;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

public class StateButton extends AppCompatButton {
    private final static String XMLNS = "http://schemas.android.com/apk/res/android";
    private DynamicResource dynamicBackground;

    public StateButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        int backgroundID = attrs.getAttributeResourceValue(XMLNS, "background", -1);
        try {
            dynamicBackground = DynamicResource.getDynamicResource(backgroundID, context);
        } catch (Resources.NotFoundException e) { }
    }

    @Override
    public void setBackgroundResource(@DrawableRes int resId) {
        super.setBackgroundResource(resId);
        dynamicBackground = DynamicResource.getDynamicResource(resId, getContext());
        setBackgroundDrawable(pickBackground());
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        if (dynamicBackground != null)
            setBackgroundDrawable(pickBackground());
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (dynamicBackground != null)
            setBackgroundDrawable(pickBackground());
    }

    private Drawable pickBackground() {
        if (!isEnabled())
            return dynamicBackground.getGrayscale();
        else if (isPressed())
            return dynamicBackground.getDarkened();
        else
            return dynamicBackground.getStandard();
    }
}
