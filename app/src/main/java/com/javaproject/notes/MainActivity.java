package com.javaproject.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.OnSwipe;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GestureDetector.OnGestureListener {

    Resources resources;
    FloatingActionButton addNote;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    MyAdapter myAdapter;
    ArrayList<user_object> list;
    Button allBTN, likedBTN;
    int[] colors;
    String userID;
    private GestureDetector gestureDetector;
    float x1,y1,x2,y2,x,y;
    private static float minDistance = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.black));
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        this.gestureDetector = new GestureDetector(MainActivity.this,this);

        resources = getResources();
        colors = resources.getIntArray(R.array.card_colors);
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        addNote = findViewById(R.id.addNoteBtn);
        allBTN = findViewById(R.id.allBtn);
        likedBTN = findViewById(R.id.likedBtn);
        recyclerView = findViewById(R.id.notesList);
        databaseReference = FirebaseDatabase.getInstance().getReference("notes").child(userID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 0, true));
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event); // Delegate touch events to the GestureDetector
                return false; // Return false to allow the RecyclerView to process the touch events
            }
        });


        list = new ArrayList<>();
        myAdapter = new MyAdapter(this,list,colors);
        recyclerView.setAdapter(myAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    user_object userObject = dataSnapshot.getValue(user_object.class);
                    list.add(userObject);
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

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
        else if (v.getId()==R.id.likedBtn){
            allBTN.setBackgroundColor(getResources().getColor(R.color.black));
            allBTN.setTextColor(getResources().getColor(R.color.white));
            likedBTN.setBackgroundColor(getResources().getColor(R.color.orange));
            likedBTN.setTextColor(getResources().getColor(R.color.black));
            Intent intent = new Intent(MainActivity.this, liked_display_activity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        allBTN.setBackgroundColor(getResources().getColor(R.color.orange));
        allBTN.setTextColor(getResources().getColor(R.color.black));
        likedBTN.setBackgroundColor(getResources().getColor(R.color.black));
        likedBTN.setTextColor(getResources().getColor(R.color.white));
    }

    @Override
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
                    }
                    else {
                        Intent intent = new Intent(this, liked_display_activity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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