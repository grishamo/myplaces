package com.myplaces.myplaces;

import android.content.Intent;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import java.util.Objects;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentPlaceFragment extends Fragment implements OnMapReadyCallback, IPageFragment {

    private GoogleMap mMap;
    private View rootView;
    private Button SearchPlaceBtn;
    private int mPageIcon = R.drawable.ic_location_on_black_24dp;
    private String mTitle = "Current Location";
    private final int REQUEST_PERMISSION_CAMERA = 2;

    Button mSavePlace_btn;
    RecyclerView recyclerView;
    List<MyPlaces> myPlacesList;
    PlaceAdapter placeAdapter;
    ImageButton takePic_btn;
    ImageView takenPicture_iv;
    LinearLayout linearLayout;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    public CurrentPlaceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_current_place, container, false);

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

        initSearchPlaceBtn();

        return rootView;
    }

    private void initSearchPlaceBtn()
    {
        SearchPlaceBtn = rootView.findViewById(R.id.SearchPlaceBtn);
        SearchPlaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(Objects.requireNonNull(getActivity()));
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getContext(), data);
                Log.i(mTitle, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getContext(), data);
                // TODO: Handle the error.
                Log.i(mTitle, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        if(requestCode == REQUEST_PERMISSION_CAMERA){
            super.onActivityResult(requestCode, resultCode, data);

            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            takenPicture_iv.setImageBitmap(bitmap);
        }
    }
}
