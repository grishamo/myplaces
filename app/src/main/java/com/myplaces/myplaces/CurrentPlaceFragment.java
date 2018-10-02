package com.myplaces.myplaces;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentPlaceFragment extends Fragment implements OnMapReadyCallback, IPageFragment {

    private GoogleMap mMap;
    private int mPageIcon = R.drawable.ic_location_on_black_24dp;
    private String mTitle = "Current Location";

    Button mSavePlace_btn;

    public CurrentPlaceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_maps, container, false);

        mSavePlace_btn = rootView.findViewById(R.id.save_place_popup_btn);
        mSavePlace_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return rootView;
    }

    public void ShowDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View dialogView = getLayoutInflater().inflate(R.layout.save_place_dialog, null);
        TextView locationTv = dialogView.findViewById(R.id.location_tv);
        TextView placeTitleTv = dialogView.findViewById(R.id.place_title_tv);
        Button editBtn = dialogView.findViewById(R.id.edit_description_btn);
        Button shareBtn = dialogView.findViewById(R.id.share_btn);
        Button saveBtn = dialogView.findViewById(R.id.save_btn);

        RecyclerView recyclerView = dialogView.findViewById(R.id.category_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        builder.setView(dialogView).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public int GetPageIcon() {
        return mPageIcon;
    }

    @Override
    public String GetPageTitle() {
        return mTitle;
    }
}
