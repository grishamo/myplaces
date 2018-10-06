package com.myplaces.myplaces;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlaceItemView extends LinearLayout {
    private View mView;
    private Context mContext;

    public PlaceItemView(Context context) {
        super(context);
        mContext = context;
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = layoutInflater.inflate(R.layout.myplaces_cell, this, false);
    }

    public PlaceItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = layoutInflater.inflate(R.layout.myplaces_cell, this, false);

    }

    public void SetPlace(MyPlace place) {

        TextView title = mView.findViewById(R.id.place_name);
        TextView location = mView.findViewById(R.id.location_name);
        TextView description = mView.findViewById(R.id.place_description);
        ImageView image = mView.findViewById(R.id.place_img);

        title.setText(place.getTitle());
        location.setText(place.getAddress().getAddressLine(0));
        description.setText(place.getDescription());

        if(place.getDefaultPhoto() != null)
        {
            image.setImageBitmap(place.getDefaultPhoto());
        }
        else {
            image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.default_image_small));
        }
    }
}
