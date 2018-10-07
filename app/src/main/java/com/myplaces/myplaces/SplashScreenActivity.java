package com.myplaces.myplaces;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {

    private RelativeLayout root;
    private ImageView circleIv;
    private ImageView markerIv;
    private ImageView heartIv;
    private TextView titleTv;
    private ObjectAnimator heartMovement;
    private ObjectAnimator titleMovement;
    private ObjectAnimator circleMovement;
    private ObjectAnimator markerMovement;
    private ObjectAnimator titleAlha;
    private ObjectAnimator markerRotate;

    private float parentCenterX;
    private float parentCenterY;

    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        root = findViewById(R.id.splash_root_layout);
        circleIv = findViewById(R.id.circle_iv);
        markerIv = findViewById(R.id.marker_iv);
        heartIv = findViewById(R.id.heart_iv);
        titleTv = findViewById(R.id.splash_title);


        parentCenterX = root.getX() + root.getWidth()/2;
        parentCenterY = root.getY() + root.getHeight()/2;

        circleMovement = ObjectAnimator.ofFloat(circleIv, "translationX",  -500, parentCenterX);
        circleMovement.setDuration(700);
        circleMovement.start();

        markerMovement = ObjectAnimator.ofFloat(markerIv, "translationY",  -500, parentCenterX);
        markerMovement.setDuration(700);
        markerMovement.start();

        heartMovement = ObjectAnimator.ofPropertyValuesHolder(heartIv,
                PropertyValuesHolder.ofFloat("scaleX", 1f, 0.85f, 1f),
                PropertyValuesHolder.ofFloat("scaleY", 1f, 0.85f, 1f));
        heartMovement.setDuration(800);
        heartMovement.setRepeatMode(ValueAnimator.REVERSE);
        heartMovement.setRepeatCount(4);
        heartMovement.start();


        heartMovement.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                SplashScreenActivity.this.finish();

                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        titleMovement = ObjectAnimator.ofFloat(titleTv, "translationY", 500, parentCenterY);
        titleAlha = ObjectAnimator.ofFloat(titleTv, "alpha", 0f, 1f);

        titleMovement.setDuration(2000);
        titleAlha.setDuration(2000);

        titleMovement.start();
        titleAlha.start();
    }
}
