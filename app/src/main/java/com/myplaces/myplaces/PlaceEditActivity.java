package com.myplaces.myplaces;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

public class PlaceEditActivity extends AppCompatActivity {

    private final String TAG = "PlaceEditActivity";
    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private final int REQUEST_PERMISSION_CAMERA = 2;

    private AppManager mAppMananger = AppManager.getInstance();
    private SpinnerData mCategoriesSpinnerObj;
    private String mUserAction;

    private Button mAddressBtn;
    private ImageView mMainImageIv;
    private EditText mTitleEt;
    private EditText mDescriptionEt;
    private EditText mPhoneEt;
    private EditText mWebsiteEt;
    private MyPlace mPlaceItem;
    private ImageView mTakenPictureIv;
    private LinearLayout mImagesLayout;
    private Address mAddress;
    private Spinner mCategoriesSp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mPlaceItem = (MyPlace) intent.getSerializableExtra("myplace");
        mUserAction = intent.getStringExtra("action");

        setContentView(R.layout.activity_place_edit);

        // Get XML elements:
        mImagesLayout = findViewById(R.id.images_layout);
        mAddressBtn = findViewById(R.id.address_btn);

        mCategoriesSp = findViewById(R.id.choosen_category_spinner);
        mMainImageIv = findViewById(R.id.main_image);
        mTitleEt = findViewById(R.id.title_et);
        mDescriptionEt = findViewById(R.id.description_et);
        mPhoneEt = findViewById(R.id.phone_et);
        mWebsiteEt = findViewById(R.id.website_et);

        // Populate XML fields with place data
        populateFields();
    }

    private void populateFields() {
        if(mPlaceItem != null) {
            mAddress = mPlaceItem.getAddress();
            mTitleEt.setText(mPlaceItem.getTitle());
            mDescriptionEt.setText(mPlaceItem.getDescription());
            mAddressBtn.setText(mAddress.getAddressLine(0));
            mPhoneEt.setText(mPlaceItem.getPhoneNumber());
            mWebsiteEt.setText(mPlaceItem.getWebURL());

            Bitmap defaultImage = mPlaceItem.getDefaultPhoto();
            if(defaultImage != null)
            {
                mMainImageIv.setImageBitmap(defaultImage);
            }
        }

        mCategoriesSpinnerObj = new SpinnerData();
        mCategoriesSpinnerObj.populate(this, mCategoriesSp);
        mCategoriesSpinnerObj.selectItem(mPlaceItem.getCategory());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PERMISSION_CAMERA) {
            super.onActivityResult(requestCode, resultCode, data);

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            mTakenPictureIv.setImageBitmap(bitmap);
        }
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                mAddress = Utils.GetPlaceAddressByLatLng(this, place.getLatLng());

                assert mAddress != null;
                mAddressBtn.setText(mAddress.getAddressLine(0));

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public void SaveBtnClick(View view) {
        mPlaceItem.setAddress(mAddress);
        mPlaceItem.setTitle(mTitleEt.getText().toString());
        mPlaceItem.setDescription(mDescriptionEt.getText().toString());
        mPlaceItem.setPhoneNumber(mPhoneEt.getText().toString());
        mPlaceItem.setWebURL(mWebsiteEt.getText().toString());

        // Check if main image is Bitmap
        Drawable drawable = mMainImageIv.getDrawable();
        boolean hasImage = (drawable != null);
        if (hasImage && (drawable instanceof BitmapDrawable)) {
            Bitmap image = ((BitmapDrawable)drawable).getBitmap();
            mPlaceItem.setDefaultPhoto(image);
        }

        String category = mCategoriesSpinnerObj.getSelectedItemStr();
        if (category != null) {
            mPlaceItem.setCategory(category);
        }

        if(mUserAction.equals("create"))
        {
            if (!(mAppMananger.isPlaceExist(mPlaceItem))) {
                AppManager.SetMenuItems(mPlaceItem);
                mAppMananger.getMyPlaces().add(mPlaceItem);
                mAppMananger.Save(this);
                Toast.makeText(this, getString(R.string.place_saved), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(this, getString(R.string.place_exist_error), Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void AddressButtonClick(View view) {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    public void AddImageBtnClick(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_PERMISSION_CAMERA);

        mTakenPictureIv = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, ViewGroup.LayoutParams.MATCH_PARENT);

        mTakenPictureIv.setLayoutParams(params);
        mImagesLayout.addView(mTakenPictureIv, 0);
    }
}
