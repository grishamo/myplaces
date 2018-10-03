package com.myplaces.myplaces;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentPlaceFragment extends Fragment implements OnMapReadyCallback, IPageFragment {

    private GoogleMap mMap;
    private int mPageIcon = R.drawable.ic_location_on_black_24dp;
    private String mTitle = "Current Location";
    private final int REQUEST_PERMISSION_CAMERA = 1;

    Button mSavePlace_btn;
    RecyclerView recyclerView;
    List<MyPlaces> myPlacesList;
    PlaceAdapter placeAdapter;
    ImageButton takePic_btn;
    ImageView takenPicture_iv;
    LinearLayout linearLayout;

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

        recyclerView = dialogView.findViewById(R.id.categories_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        myPlacesList = new ArrayList<>();
        myPlacesList.add(new MyPlaces("Bars", "bla", "bla"));
        myPlacesList.add(new MyPlaces("Rest", "bla", "bla"));
        myPlacesList.add(new MyPlaces("Hotel", "bla", "bla"));
        myPlacesList.add(new MyPlaces("Brothel", "bla", "bla"));
        myPlacesList.add(new MyPlaces("Custom", "bla", "bla"));
        myPlacesList.add(new MyPlaces("asd", "bla", "bla"));
        myPlacesList.add(new MyPlaces("Cusagdsfhdtom", "bla", "bla"));
        myPlacesList.add(new MyPlaces("argfgbvhn", "bla", "bla"));
        myPlacesList.add(new MyPlaces("w45ergd", "bla", "bla"));
        myPlacesList.add(new MyPlaces("4w56tertesr", "bla", "bla"));

        placeAdapter = new PlaceAdapter(myPlacesList);
        recyclerView.setAdapter(placeAdapter);

        builder.setView(dialogView).show();

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowEditDialog();
            }
        });
    }

    public void ShowEditDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

       final View dialogView = getLayoutInflater().inflate(R.layout.custom_place_description_dialog, null);
        takePic_btn = dialogView.findViewById(R.id.add_image_ib);

        takePic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_PERMISSION_CAMERA);

                takenPicture_iv = new ImageView(getContext());
                linearLayout = dialogView.findViewById(R.id.images_layout);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, ViewGroup.LayoutParams.MATCH_PARENT);

                takenPicture_iv.setLayoutParams(params);
                linearLayout.addView(takenPicture_iv, 0);
            }
        });

        builder.setView(dialogView).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        takenPicture_iv.setImageBitmap(bitmap);
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
