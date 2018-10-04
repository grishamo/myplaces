package com.myplaces.myplaces;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MyPlacesCustomAdapter extends BaseAdapter
{
    private List<MyPlace> places;
    private Context context;

    public MyPlacesCustomAdapter(List<MyPlace> places, Context context) {
        this.places = places;
        this.context = context;
    }

    @Override
    public int getCount() {
        return places.size();
    }

    @Override
    public Object getItem(int i) {
        return places.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.myplaces_cell, viewGroup, false);
        }

        MyPlace place = places.get(i);

        TextView title = view.findViewById(R.id.place_name);
        TextView location = view.findViewById(R.id.location_name);
        TextView description = view.findViewById(R.id.place_description);

        title.setText(place.getTitle());
        location.setText(place.getLocation());
        description.setText(place.getDescription());

        return view;
    }
}

