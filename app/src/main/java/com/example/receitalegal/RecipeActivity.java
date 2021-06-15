package com.example.receitalegal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class RecipeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<User> userArrayList;
    ArrayList<Recipe> recipeArrayList;
    MyAdapter myAdapter;
    FirebaseFirestore db;
    FirebaseAuth fAuth;
    String uId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        fAuth = FirebaseAuth.getInstance();
        uId = fAuth.getCurrentUser().getUid();


        recyclerView = findViewById(R.id.recyclerRecipe);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        //userArrayList = new ArrayList<User>();

        recipeArrayList = new ArrayList<Recipe>();

        //myAdapter = new MyAdapter(RecipeActivity.this, userArrayList);
        myAdapter = new MyAdapter(RecipeActivity.this, recipeArrayList);

        recyclerView.setAdapter(myAdapter);

        EventChangeListener();

    }

    private void EventChangeListener(){
        db.collection("users").document(uId).collection("recipeBook")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {

                        if(error != null){
                            Log.e("Firestore error",error.getMessage());
                            return;
                        }

                        for(DocumentChange dc : value.getDocumentChanges()){

                            if(dc.getType() == DocumentChange.Type.ADDED){

                                recipeArrayList.add(dc.getDocument().toObject(Recipe.class));
                            }

                            myAdapter.notifyDataSetChanged();

                        }

                    }
                });
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    public void newRecipe(View view) {
        startActivity(new Intent(getApplicationContext(), NewRecipeActivity.class));
        finish();
    }


}