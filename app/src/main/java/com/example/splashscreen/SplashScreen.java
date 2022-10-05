
package com.example.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class SplashScreen extends AppCompatActivity {

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        linearLayout = findViewById(R.id.linearLayout);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade_scale);

        linearLayout.startAnimation(anim);

        findViewById(R.id.startbtn).setOnClickListener(v -> {
            Intent i = new Intent(this, DirectionActivity.class);
            startActivity(i);
            finish();
            finishAffinity();
        });

    }


}