package com.javaproject.notes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class settings extends AppCompatActivity implements View.OnClickListener {

    ImageButton col1,col2,col3,colDropDown,backButton;
    Boolean layoutChanged = false;
    Boolean dropedDown = false;
    LinearLayout imageCols,changeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        col1 = findViewById(R.id.oneColBtn);
        col2 = findViewById(R.id.twoColBtn);
        col3 = findViewById(R.id.threeColBtn);
        colDropDown = findViewById(R.id.colDropDown);
        backButton = findViewById(R.id.backBtnSettings);
        imageCols = findViewById(R.id.imageTextLL);
        imageCols.setVisibility(View.GONE);
        changeLayout = findViewById(R.id.changeGridLayout);
        col1.setOnClickListener(this);
        col2.setOnClickListener(this);
        col3.setOnClickListener(this);
        changeLayout.setOnClickListener(this);
        colDropDown.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (v.getId()==R.id.oneColBtn){
            editor.putInt("snapColumn", 1);
            Toast.makeText(this,"Changed Appearance to single Column",Toast.LENGTH_SHORT).show();
            editor.apply();
            layoutChanged = true;
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        else if (v.getId()==R.id.twoColBtn) {
            editor.putInt("snapColumn", 2);
            Toast.makeText(this,"Changed Appearance to double Column",Toast.LENGTH_SHORT).show();
            editor.apply();
            layoutChanged = true;
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        else if (v.getId()==R.id.threeColBtn) {
            editor.putInt("snapColumn", 3);
            Toast.makeText(this,"Changed Appearance to triple Column",Toast.LENGTH_SHORT).show();
            editor.apply();
            layoutChanged = true;
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        else if (v.getId()==R.id.backBtnSettings) {
            System.out.println(layoutChanged);
            if (!layoutChanged) {
                finish();
            } else {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }

        else if (v.getId()==R.id.changeGridLayout || v.getId()==R.id.colDropDown) {
            if (dropedDown){
                RotateAnimation rotateAnimation = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setDuration(300);
                rotateAnimation.setFillAfter(true);
                colDropDown.startAnimation(rotateAnimation);
                dropedDown = false;
                imageCols.setVisibility(View.GONE);
            }
            else {
                RotateAnimation rotateAnimation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setDuration(300);
                rotateAnimation.setFillAfter(true);
                colDropDown.startAnimation(rotateAnimation);
                dropedDown = true;
                imageCols.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!layoutChanged) {
            finish();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}