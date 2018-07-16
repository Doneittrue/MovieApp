package com.example.khale.moviesnap;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Handler;
/**
 * Created by khale on 10/27/2016.
 */

public class splashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        //time to move to main_movie activity
        int INTRO_TIME = 1000;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(splashScreen.this, Movie_Main.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.accelerate_interpolator);
                finish();
            }
        }, INTRO_TIME);
    }
}
