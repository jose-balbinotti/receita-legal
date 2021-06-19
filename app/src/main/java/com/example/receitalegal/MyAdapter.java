package com.example.receitalegal;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private RecyclerViewClickListener listener;

    Context context;
    ArrayList<Recipe> recipeArrayList;

    public MyAdapter(Context context, ArrayList<Recipe> recipeArrayList, RecyclerViewClickListener listener) {
        this.context = context;
        this.recipeArrayList = recipeArrayList;
        this.listener = listener;
    }


    @NonNull
    @NotNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.recipe_row, parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyAdapter.MyViewHolder holder, int position) {
            Recipe recipe = recipeArrayList.get(position);
            holder.name.setText(recipe.name);
            holder.description.setText(recipe.description);
            Picasso.with(context).load(recipe.img).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return recipeArrayList.size();
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name,description;
        ImageView img;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.post_img);
            name = itemView.findViewById(R.id.post_title);
            description = itemView.findViewById(R.id.post_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }
}
