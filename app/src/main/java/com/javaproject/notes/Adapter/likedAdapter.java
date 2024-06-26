package com.javaproject.notes.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.javaproject.notes.R;
import com.javaproject.notes.add_notes;
import com.javaproject.notes.user_object;

import java.util.ArrayList;

public class likedAdapter extends RecyclerView.Adapter<likedAdapter.MyViewHolder> {
    Context context;
    static ArrayList<user_object> list;

    public likedAdapter(Context context, ArrayList<user_object> list) {
        this.context = context;
        likedAdapter.list = list;
    }

    @NonNull
    @Override
    public likedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_item,parent,false);
        return new likedAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull likedAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        user_object userObject = list.get(position);
        holder.title.setText(userObject.getTitle());
        holder.cont.setText(userObject.getNotescontent());
        holder.cardView.setCardBackgroundColor(userObject.getColor());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                user_object userObject = list.get(position);
                String id = userObject.getId();


                // Create an Intent for the "Add Notes" activity
                Intent intent = new Intent(context, add_notes.class);
                intent.putExtra("id",id);
                intent.putExtra("clickedCardView?",1);

                // Set up the shared element transition
                Bundle b = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
                    bitmap.eraseColor(Color.parseColor("#000000"));
                    b = ActivityOptions.makeThumbnailScaleUpAnimation(v, bitmap, 0, 0).toBundle();
                }

                // Start the activity with the transition animation
                context.startActivity(intent, b);
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


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleFetch);
            cont = itemView.findViewById(R.id.contentFetch);
            cardView = itemView.findViewById(R.id.cardView);

        }

    }
}
