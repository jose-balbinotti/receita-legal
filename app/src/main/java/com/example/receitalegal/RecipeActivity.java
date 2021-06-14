package com.example.receitalegal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RecipeActivity extends AppCompatActivity {

    private RecyclerView mRecipeList;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Global");
        mDatabase.keepSynced(true);

        mRecipeList = (RecyclerView)findViewById(R.id.recyclerRecipe);
        mRecipeList.setHasFixedSize(true);
        mRecipeList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}