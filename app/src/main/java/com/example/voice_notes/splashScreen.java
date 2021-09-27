package com.example.voice_notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.wave.MultiWaveHeader;

public class splashScreen extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;

    Animation topAnim , downAnim;
    TextView logo , slogan;
    ImageView icon;

    MultiWaveHeader waveFooter;

    



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


//      animations
        topAnim= AnimationUtils.loadAnimation(this,R.anim.top);
        downAnim = AnimationUtils.loadAnimation(this,R.anim.down);

//      hooks
        logo = findViewById(R.id.textView7);
        slogan = findViewById(R.id.textView8);
        icon = findViewById(R.id.imageView7);
        waveFooter = findViewById(R.id.waves_bottom);

        logo.setAnimation(topAnim);
        slogan.setAnimation(topAnim);
        icon.setAnimation(downAnim);

        waveFooter.setVelocity(1);
        waveFooter.setProgress(1);
        waveFooter.isRunning();
        waveFooter.setWaveHeight(70);
        waveFooter.setStartColor(Color.YELLOW);
        waveFooter.setCloseColor(Color.MAGENTA);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splashScreen.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);

    }
}