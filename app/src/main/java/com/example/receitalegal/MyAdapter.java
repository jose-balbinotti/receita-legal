package com.example.receitalegal;

import android.content.Context;
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

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private RecyclerViewClickListener listener;

    Context context;
    ArrayList<RecipePreview> recipePreviewArrayList;

    public MyAdapter(Context context, ArrayList<RecipePreview> recipePreviewArrayList, RecyclerViewClickListener listener) {
        this.context = context;
        this.recipePreviewArrayList = recipePreviewArrayList;
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
            RecipePreview recipePreview = recipePreviewArrayList.get(position);
            holder.name.setText(recipePreview.name);
            holder.description.setText(recipePreview.description);
            Picasso.with(context).load(recipePreview.img).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return recipePreviewArrayList.size();
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
            listener.onClick(view, getAbsoluteAdapterPosition());
        }
//        public void onClick(View view) {
//            listener.onClick(view, getAdapterPosition());
//        }
    }
}
