package com.javaproject.notes.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
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
    LottieAnimationView empty;
    TextView emptytext;
    FloatingActionButton addnote;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        binding = FragmentAllNotesBinding.inflate(inflater,container,false);
        MyAdapter adapter = new MyAdapter(getContext(),list);
        binding.notesList.setAdapter(adapter);
        empty = binding.emptyAnim;
        empty.setVisibility(View.GONE);
        emptytext = binding.emptyTextView;
        emptytext.setVisibility(View.GONE);

        addnote = binding.addNoteBtn;

        SharedPreferences sharedPref = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int snapColumn = sharedPref.getInt("snapColumn", 2);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), snapColumn);
        binding.notesList.setLayoutManager(layoutManager);
        database = FirebaseDatabase.getInstance().getReference("notes").child(userID);
        database.keepSynced(true);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    user_object userObject = dataSnapshot.getValue(user_object.class);
                    list.add(userObject);
                    userObject.setId(dataSnapshot.getKey());
                }
                if (list.size() == 0){
                    empty.setVisibility(View.VISIBLE);
                    emptytext.setVisibility(View.VISIBLE);
                    empty.setMinAndMaxProgress(0f,1f);
                    empty.setRepeatCount(LottieDrawable.INFINITE);
                    empty.setRepeatMode(LottieDrawable.RESTART);
                    empty.playAnimation();
                    adapter.notifyDataSetChanged();
                }
                else {
                    empty.setVisibility(View.GONE);
                    emptytext.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), add_notes.class);
                View sharedView = addnote;
                String transitionName = getString(R.string.transition_card); // Use the same string as in the XML
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        (Activity) getContext(), sharedView, transitionName);
                startActivity(intent,options.toBundle());
            }
        });

        return binding.getRoot();
    }
}