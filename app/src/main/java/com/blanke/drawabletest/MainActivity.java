package com.blanke.drawabletest;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    private ImageView loadImageView;
    private MaterialLoadDrawable materialLoadDrawable;
    private ProgressBar progressBar;
    private ImageView roundImageview, circleImageview;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadImageView = (ImageView) findViewById(R.id.my_progress);
        circleImageview = (ImageView) findViewById(R.id.my_circleview);
        roundImageview = (ImageView) findViewById(R.id.my_roundview);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.android);
        circleImageview.setImageDrawable(new CircleImageDrawable(bitmap));
        roundImageview.setImageDrawable(new RoundImageDrawable(bitmap, 15.0F));
        materialLoadDrawable = new MaterialLoadDrawable();
        loadImageView.setImageDrawable(materialLoadDrawable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        materialLoadDrawable.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        materialLoadDrawable.stop();
    }
}
