package com.example.yegor.cafeapp.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class PercentLinearLayout extends LinearLayout {

    public static final float RATIO = 0.75f;

    public PercentLinearLayout(Context context) {
        super(context);
    }

    public PercentLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PercentLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PercentLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int newHeight = (int) (MeasureSpec.getSize(widthMeasureSpec) * RATIO);
        int newHeightSpec = MeasureSpec.makeMeasureSpec(newHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, newHeightSpec);

    }


}
