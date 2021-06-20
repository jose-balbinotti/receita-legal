package com.example.receitalegal;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class Controller {

    private static Controller instance;
    public FirebaseAuth fAuth;
    public FirebaseFirestore fFirestore;
    private String name;
    private String email;
    private String uid;
    private Integer numRecipes;

    public Controller() {
        this.fAuth = FirebaseAuth.getInstance();
        this.fFirestore = FirebaseFirestore.getInstance();
    }

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void countRecipes() {
        fFirestore.collection("users").document(this.uid).collection("recipeBook")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Log.e("Firestore error",error.getMessage());
                            return;
                        }

                        int count = 0;
                        for(DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                count++;
                                setNumRecipes(count);
                            }
                        }
                    }
                });
    }

    public void setNumRecipes(Integer numRecipes) {
        this.numRecipes = numRecipes;
    }

    public Integer getNumRecipes() {
        return this.numRecipes;
    }

}
