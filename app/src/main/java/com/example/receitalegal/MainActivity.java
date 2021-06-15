package com.example.receitalegal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.icu.number.CompactNotation;
import android.os.Bundle;
import android.service.controls.Control;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity {

    TextView txtNameUser;
    Controller controller = new Controller();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtNameUser = findViewById(R.id.txtNameUser);

        FirebaseFirestore fFirestore = FirebaseFirestore.getInstance();
        DocumentReference docRef = fFirestore.collection("users").document(controller.getUid());
        docRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                String name = documentSnapshot.getString("name");
                if (name != null) {
                    txtNameUser.setText(name);
                    controller.setName(name);
                }
            }
        });
    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    public void pantry (View view){
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    public void recipe (View view) {
        startActivity(new Intent(getApplicationContext(), RecipeActivity.class));
        finish();
    }
}