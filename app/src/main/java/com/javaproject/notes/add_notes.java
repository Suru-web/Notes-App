package com.javaproject.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class add_notes extends AppCompatActivity implements View.OnClickListener {

    Button notesadd,goback;
    DatabaseReference databaseReference;
    TextInputLayout notesText;
    TextView titleText;
    String id;

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
        notesText = findViewById(R.id.notesTextInput);
        titleText = findViewById(R.id.titleTextView);
        goback.setOnClickListener(this);
        notesadd.setOnClickListener(this);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("notes");


        Intent intent = getIntent();
        String listID = intent.getStringExtra("id");
        int clickedCard = intent.getIntExtra("clickedCardView?",0);
        if (clickedCard == 1){
            DatabaseReference notesref = FirebaseDatabase.getInstance().getReference().child("notes").child(listID);
            notesref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String title = snapshot.child("title").getValue(String.class);
                        String content = snapshot.child("notescontent").getValue(String.class);

                        titleText.setText(title);
                        notesText.getEditText().setText(content);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.backButton){
            finish();
        }
        else if (v.getId()==R.id.noteaddedBtn){
            id = databaseReference.push().getKey();
            String note = notesText.getEditText().getText().toString();
            String title = titleText.getText().toString();
            user_object user = new user_object(id,title,note);
            databaseReference.child(id).setValue(user);
            Toast.makeText(this,"Notes added",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}