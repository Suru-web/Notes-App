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
import com.javaproject.notes.Adapter.lockedAdapter;
import com.javaproject.notes.R;
import com.javaproject.notes.databinding.FragmentLockedNotesBinding;
import com.javaproject.notes.user_object;

import java.util.ArrayList;


public class LockedNotes extends Fragment {


    public LockedNotes() {
        // Required empty public constructor
    }

    FragmentLockedNotesBinding binding;
    ArrayList<user_object> list = new ArrayList<>();
    DatabaseReference database;
    String userID;
    Resources resources;
    LottieAnimationView empty;
    int[] colors;
    Boolean likedNoteExists = true;
    TextView emptytext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        binding = FragmentLockedNotesBinding.inflate(inflater,container,false);
        resources = getResources();
        colors = resources.getIntArray(R.array.card_colors);
        empty = binding.emptyAnim;
        emptytext = binding.emptyTextView;
        lockedAdapter lockedAdapter = new lockedAdapter(getContext(),list,colors);
        binding.notesLockedList.setAdapter(lockedAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.notesLockedList.setLayoutManager(linearLayoutManager);


        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        binding.notesLockedList.setLayoutManager(layoutManager);
        database = FirebaseDatabase.getInstance().getReference("lockedNotes").child(userID);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                likedNoteExists = false;
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    user_object userObject = dataSnapshot.getValue(user_object.class);
                    list.add(userObject);
                    userObject.setId(dataSnapshot.getKey());
                    likedNoteExists = true;
                }
                if (likedNoteExists) {
                    binding.notesLockedList.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.GONE);
                    emptytext.setVisibility(View.GONE);
                    lockedAdapter.notifyDataSetChanged();
                }
                else {
                    binding.notesLockedList.setVisibility(View.GONE);
                    empty.setVisibility(View.VISIBLE);
                    emptytext.setVisibility(View.VISIBLE);
                    empty.setMinAndMaxProgress(0f,1f);
                    empty.setRepeatCount(LottieDrawable.INFINITE);
                    empty.setRepeatMode(LottieDrawable.RESTART);
                    empty.playAnimation();
                    lockedAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return binding.getRoot();
    }
}