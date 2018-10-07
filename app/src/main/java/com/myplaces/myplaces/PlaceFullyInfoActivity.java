package com.myplaces.myplaces;

import android.content.Intent;
import android.location.Address;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

public class PlaceFullyInfoActivity extends AppCompatActivity {

    private MyPlace mPlaceItem;

    private ImageView mPlaceImageView;
    private TextView mAddressTextView;
    private TextView mCategory;
    private TextView mTitle;
    private TextView mDescription;
    private TextView mPhone;
    private TextView mWebsite;

    private PopupMenu mOptionsPopupMenu;
    private ImageButton mOptionsBtn;
    private ImageButton mNavigationBtn;
    private ImageButton mShareBtn;
    private ImageButton mWebsiteBtn;
    private ImageButton mCallBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mPlaceItem = (MyPlace) intent.getSerializableExtra("myplace");

        setContentView(R.layout.activity_place_info);

        mPlaceImageView = findViewById(R.id.main_image);
        mAddressTextView = findViewById(R.id.address_line);
        mCategory = findViewById(R.id.category);
        mTitle = findViewById(R.id.title);
        mDescription = findViewById(R.id.description);
        mPhone = findViewById(R.id.phone);
        mWebsite = findViewById(R.id.website);

        mNavigationBtn = findViewById(R.id.navigation_btn);
        mShareBtn = findViewById(R.id.share_btn);
        mWebsiteBtn = findViewById(R.id.website_btn);
        mCallBtn = findViewById(R.id.call_btn);

        initOptionsMenu();
        populateFields();
    }

    private void initOptionsMenu() {
        mOptionsBtn = findViewById(R.id.options_btn);
        mOptionsPopupMenu = new PopupMenu(this, mOptionsBtn);

        mOptionsPopupMenu.getMenu().add(getResources().getString(R.string.edit_place));
        mOptionsPopupMenu.getMenu().add(getResources().getString(R.string.remove_place));

        mOptionsPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String title = item.getTitle().toString();
                if (title.equals("Edit Place")) {
                    OpenEditActivity();
                }
                if (title.equals("Remove Place")) {
                    RemovePlace();
                }

                return true;
            }
        });
    }

    private void populateFields() {
        if (mPlaceItem.getDefaultPhoto() != null) {
            mPlaceImageView.setImageBitmap(mPlaceItem.getDefaultPhoto());
        }

        mAddressTextView.setText(mPlaceItem.getAddress().getAddressLine(0));
        mCategory.setText(mPlaceItem.getCategory());
        mTitle.setText(mPlaceItem.getTitle());
        mDescription.setText(mPlaceItem.getDescription());
        mPhone.setText(mPlaceItem.getPhoneNumber());
        mWebsite.setText(mPlaceItem.getWebURL());

        //Hide Action button if data not exist
        if (mPlaceItem.getPhoneNumber() == null || mPlaceItem.getPhoneNumber().length() == 0) {
            mCallBtn.setVisibility(View.GONE);
        }
        if (mPlaceItem.getWebURL() == null || mPlaceItem.getWebURL().length() == 0) {
            mWebsiteBtn.setVisibility(View.GONE);
        }

    }

    private void RemovePlace() {
        AppManager.getInstance().RemovePlaceById(mPlaceItem.getTitle());
        AppManager.getInstance().Save(this);

        Toast.makeText(this, getString(R.string.place_has_been_removed), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void OpenEditActivity() {
        Intent intent = new Intent(this, PlaceEditActivity.class);
        intent.putExtra("myplace", mPlaceItem);
        intent.putExtra("action", "edit");
        startActivity(intent);
    }

    public void CallBtnClick(View view) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + mPlaceItem.getPhoneNumber()));
        startActivity(callIntent);
    }

    public void WebsiteBtnClick(View view) {
        String url = mPlaceItem.getWebURL();

        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    public void ShareBtnClick(View view) {

//        String uri = "geo:" + mPlaceItem.getAddress().getLatitude() + ","
//                +mPlaceItem.getAddress().getLongitude() + "?q=" + mPlaceItem.getAddress().getLatitude()
//                + "," + mPlaceItem.getAddress().getLongitude();
//        startActivity(new Intent(android.content.Intent.ACTION_SEND,
//                Uri.parse(uri)));

        String uri = "http://maps.google.com/maps?daddr=" + mPlaceItem.getAddress().getLatitude() + "," + mPlaceItem.getAddress().getLongitude();

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, uri);
        startActivity(Intent.createChooser(sharingIntent, "Share in..."));
    }

    public void NavigateBtnClick(View view) {
        Intent navigateIntent = new Intent(Intent.ACTION_VIEW);
        Address address = mPlaceItem.getAddress();
        String addressLine = address.getAddressLine(0);
        double latitude = address.getLatitude();
        double longitude = address.getLongitude();

        navigateIntent.setData(Uri.parse("geo:" + latitude + "," + longitude + "?q=" + addressLine));
        startActivity(navigateIntent);
    }

    public void OptionMenuClick(View view) {
        mOptionsPopupMenu.show();
    }

}
