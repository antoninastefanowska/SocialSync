package com.antonina.socialsynchro.common.gui.other;

import android.view.View;

import com.antonina.socialsynchro.R;
import com.gtomato.android.ui.manager.CarouselLayoutManager;
import com.gtomato.android.ui.widget.CarouselView;

public class CustomViewTransformer implements CarouselView.ViewTransformer {
    @Override
    public void onAttach(CarouselLayoutManager layoutManager) { }

    @Override
    public void transform(View view, float position) {
        int width = view.getWidth();
        float alpha, transX, scale, labelAlpha;
        if (-5 < position && position <= 0) {
            alpha = (5.0f + position * 0.9f) / 5.0f;
            scale = (5.0f + position * 0.5f) / 5.0f;
            labelAlpha = (1.0f + position) / 1.0f;
        } else if (0 < position && position <= 5){
            alpha = (5.0f - position * 0.9f) / 5.0f;
            scale = (5.0f - position * 0.5f) / 5.0f;
            labelAlpha = (1.0f - position) / 1.0f;
        } else {
            alpha = 0.0f;
            labelAlpha = 0.0f;
            scale = 0.0f;
        }
        transX = position * width;

        View label = view.findViewById(R.id.operation_label);
        view.setAlpha(alpha);
        view.setTranslationX(transX);
        view.setScaleX(scale);
        view.setScaleY(scale);
        label.setAlpha(labelAlpha);
    }
}
