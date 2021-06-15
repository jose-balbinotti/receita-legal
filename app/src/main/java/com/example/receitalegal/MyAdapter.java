package com.example.receitalegal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
//    ArrayList<User> userArrayList;
    ArrayList<Recipe> recipeArrayList;

    public MyAdapter(Context context, ArrayList<Recipe> recipeArrayList) {
        this.context = context;
        this.recipeArrayList = recipeArrayList;
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

//        User user = userArrayList.get(position);
//        holder.name.setText(user.name);
//        holder.email.setText(user.email);

            Recipe recipe = recipeArrayList.get(position);
            holder.name.setText(recipe.name);
            holder.description.setText(recipe.description);

    }

    @Override
    public int getItemCount() {
        return recipeArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name,description,img;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
//            img = itemView.findViewById(R.id.post_img);
            name = itemView.findViewById(R.id.post_title);
            description = itemView.findViewById(R.id.post_description);

        }
    }
}
