package com.javaproject.notes.Fragments;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.javaproject.notes.Adapter.likedAdapter;
import com.javaproject.notes.R;
import com.javaproject.notes.databinding.FragmentLikedNotesBinding;
import com.javaproject.notes.user_object;

import java.util.ArrayList;


public class LikedNotes extends Fragment {


    public LikedNotes() {
        // Required empty public constructor
    }

    FragmentLikedNotesBinding binding;
    ArrayList<user_object> list = new ArrayList<>();
    DatabaseReference database;
    String userID;
    LottieAnimationView empty;
    Boolean likedNoteExists = true;
    TextView emptytext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        binding = FragmentLikedNotesBinding.inflate(inflater,container,false);
        empty = binding.emptyAnim;
        emptytext = binding.emptyTextView;
        likedAdapter likedAdapter = new likedAdapter(getContext(),list);
        binding.notesLikedList.setAdapter(likedAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.notesLikedList.setLayoutManager(linearLayoutManager);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        binding.notesLikedList.setLayoutManager(layoutManager);
        database = FirebaseDatabase.getInstance().getReference("notes").child(userID);
        database.keepSynced(true);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                likedNoteExists = false;
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if (dataSnapshot.child("likedNote").getValue(Boolean.class).equals(true)){
                        user_object userObject = dataSnapshot.getValue(user_object.class);
                        list.add(userObject);
                        likedNoteExists = true;
                    }
                }
                if (likedNoteExists) {
                    binding.notesLikedList.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.GONE);
                    emptytext.setVisibility(View.GONE);
                    likedAdapter.notifyDataSetChanged();
                }
                else {
                    binding.notesLikedList.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                    emptytext.setVisibility(View.VISIBLE);
                    empty.setMinAndMaxProgress(0f,1f);
                    empty.setRepeatCount(LottieDrawable.INFINITE);
                    empty.setRepeatMode(LottieDrawable.RESTART);
                    empty.playAnimation();
                    likedAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return binding.getRoot();
    }
}