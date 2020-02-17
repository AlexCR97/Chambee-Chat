package com.example.chambeechat.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.chambeechat.R;

public class SplashActivity extends AppCompatActivity {

    private LinearLayout llNombre;
    private ImageView ivLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ivLogo = findViewById(R.id.ivLogo);
        llNombre = findViewById(R.id.llNombre);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent sharedIntent =new Intent(SplashActivity.this, LoginActivity.class);
                Pair[] pairs=new Pair[2];
                pairs[0] = new Pair<View,String>(ivLogo,"imageTransition");
                pairs[1] = new Pair<View,String>(llNombre,"layoutTransition");
                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this,pairs);
                startActivity(sharedIntent,options.toBundle());
                cerrarDespuesDeUnSegundo();
            }
        },3000);
    }

    private void cerrarDespuesDeUnSegundo() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ivLogo.setVisibility(View.INVISIBLE);
                finish();
            }
        }, 1000);
    }
}
