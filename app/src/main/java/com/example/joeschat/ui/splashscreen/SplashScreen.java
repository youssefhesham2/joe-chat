package com.example.joeschat.ui.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.joeschat.R;
import com.example.joeschat.ui.mainactivity.MainActivity;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        EasySplashScreen config = new EasySplashScreen(SplashScreen.this)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(2000)
                .withBackgroundResource(android.R.color.white)
                .withHeaderText("")
                .withFooterText("devolper:youssef hesham")
                .withBeforeLogoText("Joe`s Chat")
                .withLogo(R.drawable.chat_icon)
                .withAfterLogoText("");


        //change text color
        config.getHeaderTextView().setTextColor(Color.WHITE);
        config.getBeforeLogoTextView().setTextSize(30);

        //finally create the view
        View easySplashScreenView = config.create();
        setContentView(easySplashScreenView);
    }
}
