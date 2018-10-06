package com.myplaces.myplaces;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
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

    private PopupMenu optionsPopupMenu;
    private ImageButton optionsBtn;

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

        initOptionsMenu();
        populateFields();
    }

    private void initOptionsMenu() {
        optionsBtn = findViewById(R.id.options_btn);
        optionsPopupMenu = new PopupMenu(this, optionsBtn);

        optionsPopupMenu.getMenu().add("Edit Place");
        optionsPopupMenu.getMenu().add("Remove Place");

        optionsPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String title = item.getTitle().toString();
                if (title.equals("Edit Place")) {
                    OpenEditActivity();
                }

                return true;
            }
        });
    }

    private void OpenEditActivity() {
        Intent intent = new Intent(this, PlaceEditActivity.class);
        intent.putExtra("myplace", mPlaceItem);
        intent.putExtra("action", "edit");
        startActivity(intent);
    }

    private void populateFields() {
        if(mPlaceItem.getDefaultPhoto() != null) {
            mPlaceImageView.setImageBitmap(mPlaceItem.getDefaultPhoto());
        }

        mAddressTextView.setText(mPlaceItem.getAddress().getAddressLine(0));
        mCategory.setText(mPlaceItem.getCategory());
        mTitle.setText(mPlaceItem.getTitle());
        mDescription.setText(mPlaceItem.getDescription());
        mPhone.setText(mPlaceItem.getPhoneNumber());
        mWebsite.setText(mPlaceItem.getWebURL());
    }

    public void CallBtnClick(View view) {
    }

    public void WebsiteBtnClick(View view) {
    }

    public void ShareBtnClick(View view) {
    }

    public void NavigateBtnClick(View view) {
    }

    public void OptionMenuClick(View view) {
        optionsPopupMenu.show();
    }
}
