package com.acharya.habbaregistration.SplashScreen;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.acharya.habbaregistration.Login.Login;
import com.acharya.habbaregistration.R;
import com.airbnb.lottie.LottieAnimationView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        final LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.animation_view);
        animationView.setAnimation("loading.json");
        animationView.loop(false);
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(3000);
        animator.addUpdateListener(animation -> {animationView.setProgress((Float) animation.getAnimatedValue());});
        animator.start();

        // animationView.playAnimation();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
                setContentView(R.layout.activity_login);
                finish();
            }
        }, 3250);
    }

}
