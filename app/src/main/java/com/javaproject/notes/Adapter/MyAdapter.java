package com.javaproject.notes.Adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.javaproject.notes.R;
import com.javaproject.notes.add_notes;
import com.javaproject.notes.user_object;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements View.OnLongClickListener {

    Context context;
    static ArrayList<user_object> list;
    int nowColor;

    public MyAdapter(Context context, ArrayList<user_object> list) {
        this.context = context;
        MyAdapter.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        user_object userObject = list.get(position);
        holder.title.setText(userObject.getTitle());
        holder.cont.setText(userObject.getNotescontent());
        nowColor = userObject.getColor();
        holder.cardView.setCardBackgroundColor(nowColor);
        final boolean[] flipped = {false};

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                user_object userObject = list.get(position);
                String id = userObject.getId();
                Bundle b = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
                    bitmap.eraseColor(Color.parseColor("#000000"));
                    b = ActivityOptions.makeThumbnailScaleUpAnimation(v, bitmap, 0, 0).toBundle();
                }

                Intent intent = new Intent(context, add_notes.class);
                intent.putExtra("id",id);
                intent.putExtra("clickedCardView?",1);
                context.startActivity(intent, b);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                FlipAnimationCardView(holder, userObject.getColor(), flipped);
                Handler handler = new Handler();
                long delayMillis = 1000;
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FlipAnimationCardView(holder, userObject.getColor(), flipped);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                deletenote(position);
                            }
                        }, delayMillis);
                    }
                });
                holder.lock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FlipAnimationCardView(holder, userObject.getColor(), flipped);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                lockNote(position);
                            }
                        }, delayMillis);
                    }
                });

                return true;
            }

        });

    }
    public void FlipAnimationCardView(MyViewHolder holder, int currentColor, boolean[] flipped){
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 0f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1f);
        ObjectAnimator anim1 = ObjectAnimator.ofPropertyValuesHolder(holder.cardView, scaleX, scaleY);
        anim1.setDuration(400);  // Set the duration in milliseconds
        anim1.setInterpolator(new DecelerateInterpolator());
        PropertyValuesHolder reverseScaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0f, 1f);
        PropertyValuesHolder reverseScaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1f);
        ObjectAnimator anim2 = ObjectAnimator.ofPropertyValuesHolder(holder.cardView, reverseScaleX, reverseScaleY);
        anim2.setDuration(400);  // Set the duration in milliseconds
        anim2.setInterpolator(new AccelerateInterpolator());
        if (!flipped[0]) {
            anim1.start();
            anim1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    holder.front.setVisibility(View.GONE);
                    holder.back.setVisibility(View.VISIBLE);
                    holder.delete.setBackgroundColor(currentColor);
                    holder.lock.setBackgroundColor(currentColor);
                    anim2.start();
                    flipped[0] = true;
                }
            });
        }
        else {
            anim1.start();
            anim1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    holder.front.setVisibility(View.VISIBLE);
                    holder.back.setVisibility(View.GONE);
                    anim2.start();
                    flipped[0] = false;
                }
            });
        }
    }


    private void deletenote(int position){
        user_object userObject = list.get(position);
        String id = userObject.getId();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference deleteRef = FirebaseDatabase.getInstance().getReference("notes").child(userID).child(id);
        DatabaseReference deleteReflocked = FirebaseDatabase.getInstance().getReference("lockedNotes").child(userID).child(id);
        notifyItemRemoved(position);
        deleteRef.removeValue();
        deleteReflocked.removeValue();
        list.remove(position);
        notifyItemRangeChanged(0,getItemCount());
    }
    private void lockNote(int position){
        user_object userObject = list.get(position);
        String id = userObject.getId();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference lockNote = FirebaseDatabase.getInstance().getReference("lockedNotes").child(userID).child(id);
        deletenote(position);
        lockNote.setValue(userObject);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        TextView title,cont;
        LinearLayout front,back;
        Button lock,delete;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleFetch);
            cont = itemView.findViewById(R.id.contentFetch);
            cardView = itemView.findViewById(R.id.cardView);
            front = itemView.findViewById(R.id.frontLayout);
            back = itemView.findViewById(R.id.backLayout);
            lock = itemView.findViewById(R.id.lockButton);
            delete = itemView.findViewById(R.id.deleteButton);

        }

    }

}
