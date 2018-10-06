package com.myplaces.myplaces;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageView circleIv;
    private ImageView markerIv;
    private ImageView heartIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        circleIv = findViewById(R.id.circle_iv);
        markerIv = findViewById(R.id.marker_iv);
        heartIv = findViewById(R.id.heart_iv);

        Animation moveCircleAnim = AnimationUtils.loadAnimation(this, R.anim.splash_screen_move_circle_anim);
        circleIv.startAnimation(moveCircleAnim);
        circleIv.setRotation(200);

        Animation moveMarkerAnim = AnimationUtils.loadAnimation(this, R.anim.splash_screen_move_marker_anim);
        markerIv.startAnimation(moveMarkerAnim);

        Animation moveHeartAnim = AnimationUtils.loadAnimation(this, R.anim.splash_screen_move_heart_anim);
        heartIv.startAnimation(moveHeartAnim);


       /* AnimationDrawable animationDrawable = (AnimationDrawable)circleIv.getDrawable();
        animationDrawable.start();*/
    }
}
