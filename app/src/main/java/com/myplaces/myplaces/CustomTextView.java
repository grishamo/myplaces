package com.myplaces.myplaces;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class CustomTextView extends android.support.v7.widget.AppCompatTextView {
   private String mFontPath;

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Get View Attributes
       TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView, 0, 0);

        mFontPath = a.getString(R.styleable.CustomTextView_fontFace);
        setFont();
    }
    public CustomTextView(Context context) {
        super(context);
    }
    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
//        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Montez-Regular.ttf");
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), mFontPath);
        setTypeface(font, Typeface.NORMAL);
    }
}