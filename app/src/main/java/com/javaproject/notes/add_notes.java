package com.javaproject.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class add_notes extends AppCompatActivity implements View.OnClickListener {

    Button notesadd, goback;
    DatabaseReference databaseReference;
    TextInputLayout notesText;
    TextView titleText;
    String id;
    Boolean clicked = false;
    Boolean liked = false;
    String userID, listID;
    LottieAnimationView likeBtn;
    String title, content;
    Vibrator vibrator;
    int clickedCard,lockedcard;
    Boolean likedNoteSave = false;

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
        likeBtn = findViewById(R.id.likeAnimBtn);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        Intent intent = getIntent();
        listID = intent.getStringExtra("id");
        clickedCard = intent.getIntExtra("clickedCardView?", 0);
        lockedcard = intent.getIntExtra("lockednote?",0);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("notes");
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        if (clickedCard == 1){
            DatabaseReference notesref = FirebaseDatabase.getInstance().getReference().child("notes").child(userID).child(listID);
            setDefaultData(notesref);
        }
        if (lockedcard == 1){
            DatabaseReference notesref = FirebaseDatabase.getInstance().getReference().child("lockedNotes").child(userID).child(listID);
            setDefaultData(notesref);
            likeBtn.setVisibility(View.GONE);
            String done = "Done";
            notesadd.setVisibility(View.VISIBLE);
            notesadd.setText(done);
        }


        goback.setOnClickListener(this);
        notesadd.setOnClickListener(this);
        likeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        id = databaseReference.push().getKey();
        if (v.getId() == R.id.backButton) {
            vibrator.vibrate(1);
            finish();
        } else if (v.getId() == R.id.noteaddedBtn) {
            vibrator.vibrate(1);
            content = notesText.getEditText().getText().toString();
            title = titleText.getText().toString();
            if (clicked) {
                updateData(title, content, liked, listID);
                finish();
            } else {
                updateData(title, content, liked, id);
                finish();
            }
        } else if (v.getId() == R.id.likeAnimBtn) {
            vibrator.vibrate(1);
            HashMap<String, Object> hashMap = new HashMap<>();
            if (clicked) {
                if (liked) {
                    likeBtn.setMinAndMaxProgress(0.5f, 1f);
                    liked = false;
                } else {
                    likeBtn.setMinAndMaxProgress(0f, 0.5f);
                    liked = true;
                }
                likeBtn.playAnimation();
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (clickedCard == 1){
            content = notesText.getEditText().getText().toString();
            title = titleText.getText().toString();
            updateData(title,content,liked,listID);
            Toast.makeText(add_notes.this,"Note Saved",Toast.LENGTH_SHORT).show();
        }
        if (lockedcard == 1){
            finish();
        }
        else {
            finish();
        }
    }

    private void updateData(String title, String content, Boolean liked, String listID) {
        DatabaseReference notesreference = FirebaseDatabase.getInstance().getReference().child("notes").child(userID).child(listID);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", listID);
        hashMap.put("title", title);
        hashMap.put("notescontent", content);
        hashMap.put("likedNote", liked);
        notesreference.setValue(hashMap);
    }
    private void setDefaultData(DatabaseReference notesref){
            clicked = true;
            notesadd.setVisibility(View.GONE);
            likeBtn.setVisibility(View.VISIBLE);
            notesref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        title = snapshot.child("title").getValue(String.class);
                        content = snapshot.child("notescontent").getValue(String.class);
                        liked = snapshot.child("likedNote").getValue(Boolean.class);
                        titleText.setText(title);
                        notesText.getEditText().setText(content);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            notesref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        liked = snapshot.child("likedNote").getValue(Boolean.class);
                        if (liked) {
                            likeBtn.setMinAndMaxProgress(0.5f, 0.5f);
                            likeBtn.playAnimation();
                        } else {
                            likeBtn.setMinAndMaxProgress(0f, 0f);
                            likeBtn.playAnimation();
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }
}