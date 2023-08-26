package com.javaproject.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton addNote;
    Button allBTN, likedBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.black));
        window.setNavigationBarColor(this.getResources().getColor(R.color.black));

        addNote = findViewById(R.id.addNoteBtn);
        allBTN = findViewById(R.id.allBtn);
        likedBTN = findViewById(R.id.likedBtn);

        addNote.setOnClickListener(this);
        allBTN.setOnClickListener(this);
        likedBTN.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.addNoteBtn){
            Intent intent = new Intent(this, add_notes.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.allBtn){
            allBTN.setBackgroundColor(getResources().getColor(R.color.orange));
            allBTN.setTextColor(getResources().getColor(R.color.black));
            likedBTN.setBackgroundColor(getResources().getColor(R.color.black));
            likedBTN.setTextColor(getResources().getColor(R.color.white));
        }
        else if (v.getId()==R.id.likedBtn){
            allBTN.setBackgroundColor(getResources().getColor(R.color.black));
            allBTN.setTextColor(getResources().getColor(R.color.white));
            likedBTN.setBackgroundColor(getResources().getColor(R.color.orange));
            likedBTN.setTextColor(getResources().getColor(R.color.black));
        }
    }
}