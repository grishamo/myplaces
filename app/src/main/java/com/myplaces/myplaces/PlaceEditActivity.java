package com.myplaces.myplaces;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

public class PlaceEditActivity extends AppCompatActivity {

    private ImageButton mTakePicBtn;
    private Button mSavePlaceBtn;
    private ImageView mMainImageIv;
    private EditText mLocationEt;
    private EditText mTitleEt;
    private EditText mDescriptionEt;
    private MyPlace mPlaceItem;
    private ImageView mTakenPictureIv;
    private LinearLayout mImagesLayout;

    private Context _this;

    private final int REQUEST_PERMISSION_CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mPlaceItem = (MyPlace) intent.getSerializableExtra("myplace");

        Log.i("PlaceEditActivity", "onCreate");
        setContentView(R.layout.activity_place_edit);

        _this = this;
        mMainImageIv = findViewById(R.id.main_image);
        mTakePicBtn = findViewById(R.id.add_image_ib);
        mLocationEt = findViewById(R.id.location_et);
        mTitleEt = findViewById(R.id.title_et);
        mDescriptionEt = findViewById(R.id.description_et);
        mImagesLayout = findViewById(R.id.images_layout);
        mSavePlaceBtn = findViewById(R.id.save_place_btn);

        initAddImageClickListener();
        populateFields();
    }

    private void populateFields() {
        if(mPlaceItem != null) {
            mTitleEt.setText(mPlaceItem.getTitle());
            mLocationEt.setText(mPlaceItem.getTitle());
            mDescriptionEt.setText(mPlaceItem.getDescription());
            //mMainImageIv.setImageBitmap(mPlaceItem.GetAllImages().get(0));
        }
    }

    private void initAddImageClickListener() {
        mTakePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_PERMISSION_CAMERA);

                mTakenPictureIv = new ImageView(_this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, ViewGroup.LayoutParams.MATCH_PARENT);

                mTakenPictureIv.setLayoutParams(params);
                mImagesLayout.addView(mTakenPictureIv, 0);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PERMISSION_CAMERA) {
            super.onActivityResult(requestCode, resultCode, data);

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            mTakenPictureIv.setImageBitmap(bitmap);
        }
    }
}
