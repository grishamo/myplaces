package com.myplaces.myplaces;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImageThumbImageView extends android.support.v7.widget.AppCompatImageView {
    private boolean mIsSelected;
    private Context mContext;

    public ImageThumbImageView(Context context) {
        super(context);
        mContext = context;
        setStyles();
    }
    public ImageThumbImageView(Context context,  AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageThumbImageView, 0, 0);
        mIsSelected = a.getBoolean(R.styleable.ImageThumbImageView_selected, false);

        setStyles();
        select(mIsSelected);
    }

    private void setStyles() {
        int marginSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        int paddingSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
        params.setMargins(marginSize, marginSize, marginSize, marginSize);

        this.setLayoutParams(params);
        this.setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
        this.setCropToPadding(true);
        this.setScaleType(ImageView.ScaleType.CENTER_CROP);
        this.setBackgroundColor(Color.parseColor("#AAAAAA"));
    }

    public void select(Boolean isSelected) {
        mIsSelected = isSelected;
        if(isSelected){
            this.setBackgroundColor(ContextCompat.getColor(mContext ,R.color.main_btn));
        }
        else {
            this.setBackgroundColor(Color.parseColor("#AAAAAA"));
        }
    }
}
