package com.javaproject.notes.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.javaproject.notes.R;
import com.javaproject.notes.add_notes;
import com.javaproject.notes.fingerprint;
import com.javaproject.notes.user_object;

import java.util.ArrayList;
import java.util.concurrent.Executor;

public class lockedAdapter extends RecyclerView.Adapter<lockedAdapter.MyViewHolder> {
    Context context;
    static ArrayList<user_object> list;
    int[] colors;
    int currentColorIndex = 0;

    public lockedAdapter(Context context, ArrayList<user_object> list,int[] colors) {
        this.context = context;
        lockedAdapter.list = list;
        this.colors = colors;
    }

    @NonNull
    @Override
    public lockedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_item,parent,false);
        return new lockedAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull lockedAdapter.MyViewHolder holder, int position) {
        user_object userObject = list.get(position);
        holder.title.setText(userObject.getTitle());
        holder.cont.setVisibility(View.GONE);
        holder.lockimg.setVisibility(View.VISIBLE);


        int currentColor = colors[currentColorIndex];
        holder.cardView.setCardBackgroundColor(currentColor);

        currentColorIndex = (currentColorIndex + 1) % colors.length;
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, "Item long pressed! "+ position, Toast.LENGTH_SHORT).show();
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
        ImageView lockimg;
        BiometricManager biometricManager;
        Boolean fpsuccess = false;
        Intent intent;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleFetch);
            cont = itemView.findViewById(R.id.contentFetch);
            cardView = itemView.findViewById(R.id.cardView);
            lockimg = itemView.findViewById(R.id.lockImage);
            biometricManager = androidx.biometric.BiometricManager.from(itemView.getContext());
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    user_object userObject = list.get(position);
                    String id = userObject.getId();
                    intent = new Intent(itemView.getContext(), add_notes.class);
                    intent.putExtra("id",id);
                    intent.putExtra("clickedCardView?",0);
                    intent.putExtra("lockednote?",1);
                    fingerprint fingerprint = new fingerprint(itemView.getContext(),intent);
                    fingerprint.fingerprint(itemView.getContext(),intent);
                }
            });

        }

    }
}
