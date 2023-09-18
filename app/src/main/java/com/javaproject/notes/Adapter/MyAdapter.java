package com.javaproject.notes.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

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
    int[] colors;
    int currentColorIndex = 0;

    public MyAdapter(Context context, ArrayList<user_object> list,int[] colors) {
        this.context = context;
        MyAdapter.list = list;
        this.colors = colors;
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


        int currentColor = colors[currentColorIndex];
        holder.cardView.setCardBackgroundColor(currentColor);

        currentColorIndex = (currentColorIndex + 1) % colors.length;
        holder.itemView.setOnLongClickListener(this);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context,holder.title);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Delete note")){
                            deletenote(position);
                        }
                        else if (item.getTitle().equals("Lock note")){
                            lockNote(position);
                        }
                        return true;
                    }
                });
                popupMenu.show();
                return true;
            }
        });

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
