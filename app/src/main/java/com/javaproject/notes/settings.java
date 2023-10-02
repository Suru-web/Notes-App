package com.javaproject.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.javaproject.notes.Fragments.AllNotes;

public class settings extends AppCompatActivity implements View.OnClickListener {

    ImageButton col1,col2,col3,colDropDown,backButton;
    Boolean layoutChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        col1 = findViewById(R.id.oneColBtn);
        col2 = findViewById(R.id.twoColBtn);
        col3 = findViewById(R.id.threeColBtn);
        colDropDown = findViewById(R.id.threeColBtn);
        backButton = findViewById(R.id.backBtnSettings);
        col1.setOnClickListener(this);
        col2.setOnClickListener(this);
        col3.setOnClickListener(this);
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
        } else if (v.getId()==R.id.twoColBtn) {
            editor.putInt("snapColumn", 2);
            Toast.makeText(this,"Changed Appearance to double Column",Toast.LENGTH_SHORT).show();
            editor.apply();
            layoutChanged = true;
        } else if (v.getId()==R.id.threeColBtn) {
            editor.putInt("snapColumn", 3);
            Toast.makeText(this,"Changed Appearance to triple Column",Toast.LENGTH_SHORT).show();
            editor.apply();
            layoutChanged = true;
        } else if (v.getId()==R.id.backBtnSettings) {
            System.out.println(layoutChanged);
            if (!layoutChanged) {
                finish();
            } else {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
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