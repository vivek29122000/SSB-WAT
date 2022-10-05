package com.example.splashscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class AfterTestActivity extends AppCompatActivity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_after_test);

        StringBuilder sb = new StringBuilder();
        int i = 1;
        for(String k: Data.tmp) {
            sb.append(i++ + ". " + k + "\n\n");
        }

        tv = findViewById(R.id.tv);
        tv.setText(sb.toString());

        findViewById(R.id.btn_).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AfterTestActivity.this, DirectionActivity.class));
                finish();finishAffinity();
            }
        });

    }
}