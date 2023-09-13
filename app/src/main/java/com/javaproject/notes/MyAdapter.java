package com.javaproject.notes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements View.OnLongClickListener {

    Context context;
    static ArrayList<user_object> list;
    int[] colors;
    int currentColorIndex = 0;

    public MyAdapter(Context context, ArrayList<user_object> list,int[] colors) {
        this.context = context;
        this.list = list;
        this.colors = colors;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        user_object userObject = list.get(position);
        holder.title.setText(userObject.getTitle());
        holder.cont.setText(userObject.getNotescontent());


        int currentColor = colors[currentColorIndex];
        holder.cardView.setCardBackgroundColor(currentColor);

        currentColorIndex = (currentColorIndex + 1) % colors.length;
        holder.itemView.setOnLongClickListener(this);
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

    @Override
    public boolean onLongClick(View v) {
        return false;
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        TextView title,cont;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleFetch);
            cont = itemView.findViewById(R.id.contentFetch);
            cardView = itemView.findViewById(R.id.cardView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    user_object userObject = list.get(position);
                    String id = userObject.getId();
                    Intent intent = new Intent(itemView.getContext(), add_notes.class);
                    intent.putExtra("id",id);
                    intent.putExtra("clickedCardView?",1);
                    itemView.getContext().startActivity(intent);
                }
            });

        }

    }

}
