package com.javaproject.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.airbnb.lottie.LottieAnimationView;

public class Community_Notes extends AppCompatActivity {
    ImageButton backButton;
    LottieAnimationView comm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_notes);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        backButton = findViewById(R.id.backbtnComNotes);
        comm = findViewById(R.id.communityAnimation);
        comm.setMinAndMaxProgress(0f,1f);
        comm.playAnimation();









        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}