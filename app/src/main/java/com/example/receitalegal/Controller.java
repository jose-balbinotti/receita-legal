package com.example.receitalegal;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Controller {

    private static Controller instance;
    public FirebaseAuth fAuth;
    public FirebaseFirestore fFirestore;
    private String name;
    private String email;
    private String uid;

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

}
