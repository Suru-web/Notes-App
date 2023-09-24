package com.javaproject.notes.Adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.javaproject.notes.MainActivity;
import com.javaproject.notes.R;
import com.javaproject.notes.add_notes;
import com.javaproject.notes.fingerprint;
import com.javaproject.notes.user_object;

import java.util.ArrayList;
import java.util.concurrent.Executor;

public class lockedAdapter extends RecyclerView.Adapter<lockedAdapter.MyViewHolder> implements fingerprint.FingerPrintCallBack {
    Context context;
    static ArrayList<user_object> list;

    public lockedAdapter(Context context, ArrayList<user_object> list) {
        this.context = context;
        lockedAdapter.list = list;
    }

    @NonNull
    @Override
    public lockedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_item,parent,false);
        return new lockedAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull lockedAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        user_object userObject = list.get(position);
        holder.title.setText(userObject.getTitle());
        holder.cont.setVisibility(View.GONE);
        holder.lockimg.setVisibility(View.VISIBLE);
        holder.lockimg.setMinAndMaxProgress(0f,0.8f);
        holder.lockimg.playAnimation();
        holder.cardView.setCardBackgroundColor(userObject.getColor());
        final boolean[] flipped = {false};
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fingerprint fingerprint = new fingerprint(context);
                fingerprint.fingerprint(context,"open",position);
                fingerprint.setfingerprintCallback(lockedAdapter.this);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                FlipAnimationCardView(holder, userObject.getColor(), flipped);
                holder.lock.setText("Unlock");
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fingerprint fingerprint = new fingerprint(context);
                        fingerprint.fingerprint(context,"delete",position);
                        fingerprint.setfingerprintCallback(lockedAdapter.this);
                    }
                });
                holder.lock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FlipAnimationCardView(holder, userObject.getColor(), flipped);
                        fingerprint fingerprint = new fingerprint(context);
                        fingerprint.fingerprint(context,"lock",position);
                        fingerprint.setfingerprintCallback(lockedAdapter.this);
                    }
                });
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        TextView title,cont;
        LottieAnimationView lockimg;
        BiometricManager biometricManager;
        LinearLayout front,back;
        Button lock,delete;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleFetch);
            cont = itemView.findViewById(R.id.contentFetch);
            cardView = itemView.findViewById(R.id.cardView);
            lockimg = itemView.findViewById(R.id.lockImage);
            front = itemView.findViewById(R.id.frontLayout);
            back = itemView.findViewById(R.id.backLayout);
            lock = itemView.findViewById(R.id.lockButton);
            delete = itemView.findViewById(R.id.deleteButton);
            biometricManager = androidx.biometric.BiometricManager.from(itemView.getContext());

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
    private void unlockNote(int position){
        user_object userObject = list.get(position);
        String id = userObject.getId();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference lockNote = FirebaseDatabase.getInstance().getReference("notes").child(userID).child(id);
        deletenote(position);
        lockNote.setValue(userObject);
    }
    private void FlipAnimationCardView(lockedAdapter.MyViewHolder holder, int currentColor, boolean[] flipped){
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

    @Override
    public void onSuccess(String what, int position) {
        Handler handler = new Handler();
        long delayMillis = 600;
        if (what.equals("lock")){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    unlockNote(position);
                }
            }, delayMillis);
        }
        else if (what.equals("delete")){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    deletenote(position);
                }
            }, delayMillis);
        } else if (what.equals("open")) {
            user_object userObject = list.get(position);
            String id = userObject.getId();
            Intent intent = new Intent(context, add_notes.class);
            intent.putExtra("id",id);
            intent.putExtra("clickedCardView?",0);
            intent.putExtra("lockednote?",1);
            context.startActivity(intent);
        }
    }

    @Override
    public void onFail(String what, int position) {
    }
}
