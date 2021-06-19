package com.example.receitalegal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class RecipeActivity extends AppCompatActivity {

    TextView txtNameUser;
    RecyclerView recyclerView;
    ArrayList<Recipe> recipeArrayList;
    MyAdapter myAdapter;
    String uId;
    Controller controller = Controller.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        uId = controller.getUid();

        recyclerView = findViewById(R.id.recyclerRecipe);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        txtNameUser = findViewById(R.id.txtNameUser);
        txtNameUser.setText(controller.getName());

        recipeArrayList = new ArrayList<Recipe>();

        myAdapter = new MyAdapter(RecipeActivity.this, recipeArrayList);

        recyclerView.setAdapter(myAdapter);

        EventChangeListener();

    }

    private void EventChangeListener(){
        controller.fFirestore.collection("users").document(uId).collection("recipeBook")
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

    public void logout(View view){
        controller.fAuth.signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        controller = null;
        finish();
    }

    public void newRecipe(View view) {
        startActivity(new Intent(getApplicationContext(), NewRecipeActivity.class));
        finish();
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(this, MainActivity.class));
        finishAffinity();
        return;
    }

}