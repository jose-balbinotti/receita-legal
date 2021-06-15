package com.example.receitalegal;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.core.FirestoreClient;

import org.jetbrains.annotations.NotNull;

public class Controller {

    private String name;
    private String email;
    private String uid;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore fFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionRef = fFirestore.collection("users");

    public Controller() {
        this.name = "";
        this.email = fAuth.getCurrentUser().getEmail();
        this.uid = fAuth.getCurrentUser().getUid();
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

    public boolean getUserIsLogged() {
        if(fAuth.getCurrentUser() == null){
            return false;
        }else {

            return true;
        }
    }

    public void getDocReference(String uid) {
        DocumentReference docRef = collectionRef.document(uid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    name = documentSnapshot.getString("fullName");
                    setName(name);
//                    Map<String, Object> usersData = documentSnapshot.getData();
                    Log.d("nome docSnap", name);
                } else {
                    Log.d("sem dado", "No such document");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.d("Erro", "Erro");
            }
        });

//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        setName((String) document.getData().get("name")); //Set name of user
//                        Log.d("nome", (String) document.getData().get("name") + getName());
//                    } else {
//                        Log.d("sem dado", "No such document");
//                    }
//                } else {
//                    Log.d("erro", "get failed with ", task.getException());
//                }
//            }
//        });
    }

}
