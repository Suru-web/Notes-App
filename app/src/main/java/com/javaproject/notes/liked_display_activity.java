package com.javaproject.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class liked_display_activity extends AppCompatActivity implements View.OnClickListener, GestureDetector.OnGestureListener {

    Button allbtn,likedBTN;
    DatabaseReference likedREF;
    likedAdapter likedAdapter;
    ArrayList<user_object> list;
    int[] colors;
    RecyclerView recyclerView;
    Resources resources;
    String userID;
    Boolean likedNoteExists = false;
    LottieAnimationView empty;
    private GestureDetector gestureDetector;
    float x1,y1,x2,y2,x,y;
    private static float minDistance = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_display);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.black));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        this.gestureDetector = new GestureDetector(liked_display_activity.this,this);

        resources = getResources();
        colors = resources.getIntArray(R.array.card_colors);
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        recyclerView = findViewById(R.id.notesList);
        empty = findViewById(R.id.emptyAnim);
        empty.setVisibility(View.GONE);

        likedREF = FirebaseDatabase.getInstance().getReference("notes").child(userID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 0, true));

        list = new ArrayList<>();
        likedAdapter = new likedAdapter(this,list,colors);
        recyclerView.setAdapter(likedAdapter);

        likedREF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if (dataSnapshot.child("likedNote").getValue(Boolean.class).equals(true)){
                        user_object userObject = dataSnapshot.getValue(user_object.class);
                        list.add(userObject);
                        likedNoteExists = true;
                    }
                }
                if (likedNoteExists) {
                    recyclerView.setVisibility(View.VISIBLE);
                    likedAdapter.notifyDataSetChanged();
                }
                else {
                    empty.setVisibility(View.VISIBLE);
                    empty.setMinAndMaxProgress(0f,1f);
                    empty.playAnimation();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        allbtn = findViewById(R.id.allBtn);
        allbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.allBtn){
            allbtn.setBackgroundColor(getResources().getColor(R.color.orange));
            allbtn.setTextColor(getResources().getColor(R.color.black));
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public boolean onTouchEvent(MotionEvent event) {

        gestureDetector.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();

                x = x2-x1;
                y = y2-y1;
                if (Math.abs(x)>minDistance){
                    if (x2>x1){
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                    else {
                    }
                }

        }


        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(@NonNull MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent e) {

    }

    @Override
    public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}