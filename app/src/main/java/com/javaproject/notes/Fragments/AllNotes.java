package com.javaproject.notes.Fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.javaproject.notes.Adapter.MyAdapter;
import com.javaproject.notes.R;
import com.javaproject.notes.add_notes;
import com.javaproject.notes.databinding.FragmentAllNotesBinding;
import com.javaproject.notes.user_object;

import java.util.ArrayList;

public class AllNotes extends Fragment {



    public AllNotes() {
        // Required empty public constructor
    }

    FragmentAllNotesBinding binding;
    ArrayList<user_object> list = new ArrayList<>();
    DatabaseReference database;
    String userID;
    Resources resources;
    int[] colors;
    FloatingActionButton addnote;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        binding = FragmentAllNotesBinding.inflate(inflater,container,false);
        resources = getResources();
        colors = resources.getIntArray(R.array.card_colors);
        MyAdapter adapter = new MyAdapter(getContext(),list,colors);
        binding.notesList.setAdapter(adapter);

        addnote = binding.addNoteBtn;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.notesList.setLayoutManager(linearLayoutManager);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        binding.notesList.setLayoutManager(layoutManager);
        database = FirebaseDatabase.getInstance().getReference("notes").child(userID);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    user_object userObject = dataSnapshot.getValue(user_object.class);
                    list.add(userObject);
                    userObject.setId(dataSnapshot.getKey());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), add_notes.class);
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }
}