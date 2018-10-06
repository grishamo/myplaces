package com.myplaces.myplaces;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity
{
    private SimpleFragmentPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ShowSplashScreen();
        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Find TabLayout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);

        // Create an adapter that knows which fragment should be shown on each page
        adapter = new SimpleFragmentPagerAdapter(this, getSupportFragmentManager(), tabLayout);


        // Add Page Fragment to adapter
        adapter.AddFragment(new CurrentPlaceFragment())
                .AddFragment(new MyPlacesFragment());


        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        tabLayout.setupWithViewPager(viewPager);
        adapter.SetTabsIcon();

        AppManager appManager = AppManager.getInstance();
        appManager.Load(getApplicationContext());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position)
            {
                IPageFragment fragment = (IPageFragment)adapter.getItem(position);
                fragment.FragmentSelect();
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    public void ShowSplashScreen(){
        Intent intent = new Intent(this, SplashScreenActivity.class);
        startActivity(intent);
    }
}
