package com.myplaces.myplaces;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>{

    private List<MyPlace> places;
    //callback
    private MyPlaceListener listener;

    interface MyPlaceListener{
        void onCategoryClick(int position, View view);
     //   void onCategoryLongClick(int position, View view);
    }

    public void setListener(MyPlaceListener listener){
        this.listener = listener;
    }

    public PlaceAdapter(List<MyPlace> places) {
        this.places = places;
    }

    public class PlaceViewHolder extends RecyclerView.ViewHolder{

        TextView categoryTv;

        public PlaceViewHolder(View itemView) {
            super(itemView);

            categoryTv = itemView.findViewById(R.id.place_category_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        listener.onCategoryClick(getAdapterPosition(), v);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_category_cell, parent, false);
        PlaceViewHolder placeViewHolder = new PlaceViewHolder(view);
        return placeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        MyPlace myPlace = places.get(position);
        holder.categoryTv.setText(myPlace.getTitle());
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}


