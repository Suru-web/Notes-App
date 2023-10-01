package com.javaproject.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.transition.Fade;
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
    DatabaseReference notesref;
    TextInputLayout notesText;
    TextView titleText;
    String id;
    Boolean clicked = false;
    Boolean liked = false;
    Boolean defaultLiked;
    String userID, listID;
    LottieAnimationView likeBtn;
    String title, content;
    Vibrator vibrator;
    int clickedCard,lockedcard;
    int storedColor;
    String defTitle,defCont;
    Resources resources;
    int[] colors;
    int currentColor, colorIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Fade fade = new Fade();
        fade.excludeTarget(getWindow().getClass(),true);
        fade.excludeTarget(android.R.id.statusBarBackground,true);
        fade.excludeTarget(android.R.id.navigationBarBackground,true);
        getWindow().setEnterTransition(fade);


        notesadd = findViewById(R.id.noteaddedBtn);
        goback = findViewById(R.id.backButton);
        notesText = findViewById(R.id.notesTextInput);
        titleText = findViewById(R.id.titleTextView);
        likeBtn = findViewById(R.id.likeAnimBtn);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        resources = getResources();
        colors = resources.getIntArray(R.array.card_colors);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        colorIndex = sharedPreferences.getInt("colorIndex",0);

        currentColor = colors[colorIndex];
        colorIndex = (colorIndex + 1 ) % colors.length;
        editor.putInt("colorIndex",colorIndex);
        editor.apply();

        Intent intent = getIntent();
        listID = intent.getStringExtra("id");
        clickedCard = intent.getIntExtra("clickedCardView?", 0);
        lockedcard = intent.getIntExtra("lockednote?",0);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("notes");
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        if (clickedCard == 1){
            notesref = FirebaseDatabase.getInstance().getReference().child("notes").child(userID).child(listID);
            setDefaultData(notesref);
            clicked = true;
        }
        if (lockedcard == 1){
            notesref = FirebaseDatabase.getInstance().getReference().child("lockedNotes").child(userID).child(listID);
            setDefaultData(notesref);
            likeBtn.setVisibility(View.GONE);
            notesadd.setVisibility(View.VISIBLE);
            notesadd.setText("Done");
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
            if (lockedcard == 1){
                updateData(title,content,false,listID,notesref,storedColor);
                finish();
            }
            else {
                if (clicked) {
                    updateData(title, content, liked, listID,notesref,storedColor);
                    finish();
                } else {
                    notesref = FirebaseDatabase.getInstance().getReference().child("notes").child(userID).child(id);
                    updateData(title, content, liked, id,notesref,currentColor);
                    finish();
                }
            }
        } else if (v.getId() == R.id.likeAnimBtn) {
            vibrator.vibrate(1);
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
        content = notesText.getEditText().getText().toString();
        title = titleText.getText().toString();
        if (title.isEmpty() && content.isEmpty()){
            finish();
        }
        else {
            if (title.equals(defTitle) && content.equals(defCont) && defaultLiked == liked) {
                finish();
            } else {
                updateData(title, content, liked, listID, notesref, storedColor);
                Toast.makeText(add_notes.this, "Note Saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void updateData(String title, String content, Boolean liked, String listID, DatabaseReference reference,int currentColor) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", listID);
        hashMap.put("title", title);
        hashMap.put("notescontent", content);
        hashMap.put("likedNote", liked);
        hashMap.put("color",currentColor);
        reference.setValue(hashMap);
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
                        storedColor = snapshot.child("color").getValue(Integer.class);
                        titleText.setText(title);
                        notesText.getEditText().setText(content);
                        defCont = content;
                        defTitle = title;
                        defaultLiked = liked;
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