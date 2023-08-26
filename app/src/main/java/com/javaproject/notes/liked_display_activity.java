package com.javaproject.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class liked_display_activity extends AppCompatActivity implements View.OnClickListener {

    Button allbtn,likedBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_display);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.black));
        window.setNavigationBarColor(this.getResources().getColor(R.color.black));

        allbtn = findViewById(R.id.allBtn);
        allbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.allBtn){
            allbtn.setBackgroundColor(getResources().getColor(R.color.orange));
            allbtn.setTextColor(getResources().getColor(R.color.black));
            finish();
        }
    }
}