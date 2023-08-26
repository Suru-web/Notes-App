package com.javaproject.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.OnSwipe;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String colors[] = {"orange","darkorange","green","blue","lightyellow"};
    FloatingActionButton addNote;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    MyAdapter myAdapter;
    ArrayList<user_object> list;
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
        recyclerView = findViewById(R.id.notesList);
        databaseReference = FirebaseDatabase.getInstance().getReference("notes");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        list = new ArrayList<>();
        myAdapter = new MyAdapter(this,list);
        recyclerView.setAdapter(myAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
}