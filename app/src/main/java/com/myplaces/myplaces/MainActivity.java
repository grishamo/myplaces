package com.myplaces.myplaces;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity
{
    private SimpleFragmentPagerAdapter adapter;
    private ImageView catAnimIv;
    private static int count = 0;

    Handler h = new Handler();
    int delay = 20*1000; //1 second=1000 millisecond, 15*1000=15seconds
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        if(count == 0) {
//            count++;
//            ShowSplashScreen();
//        }
        ShowCatAnimation();


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

    public void ShowCatAnimation(){
        catAnimIv = findViewById(R.id.cat_anim_iv);
        AnimationDrawable animationDrawable = (AnimationDrawable)catAnimIv.getDrawable();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.cat_move_animation);
        catAnimIv.startAnimation(animation);
        animationDrawable.start();
        animation.getRepeatCount();
        catAnimIv.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        //start handler as activity become visible

        h.postDelayed( runnable = new Runnable() {
            public void run() {
                //do something
                ShowCatAnimation();

                h.postDelayed(runnable, delay);
            }
        }, delay);

        super.onResume();
    }


    @Override
    protected void onPause() {
        h.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }
}
