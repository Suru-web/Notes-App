package com.javaproject.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;


public class add_notes extends AppCompatActivity implements View.OnClickListener {

    Button notesadd,goback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.black));
        window.setNavigationBarColor(this.getResources().getColor(R.color.black));


        notesadd = findViewById(R.id.noteaddedBtn);
        goback = findViewById(R.id.backButton);
        goback.setOnClickListener(this);
        notesadd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.backButton){
            finish();
        }
        else if (v.getId()==R.id.noteaddedBtn){
            Toast.makeText(this,"Notes added",Toast.LENGTH_SHORT).show();
        }
    }
}