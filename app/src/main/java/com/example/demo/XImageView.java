package com.example.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class XImageView extends ImageView {

    public XImageView(Context context) {
        super(context);
    }

    public XImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = Math.round(width * .565f);
        int updateHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, updateHeightMeasureSpec);
    }
}
