package com.example.receitalegal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {

    EditText lEmail, lPassword;
    Button lBtnLogin;
    Controller controller = Controller.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        controller.fAuth.signOut();

        lEmail = findViewById(R.id.editTextEmailAddress);
        lPassword = findViewById(R.id.editTextPassword);
        lBtnLogin = findViewById(R.id.btnLogin);

        lBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = lEmail.getText().toString().trim();
                String password = lPassword.getText().toString().trim();

                if(email.isEmpty()){
                    lEmail.setError("Email required");
                    return;
                }

                if(password.isEmpty()){
                    lPassword.setError("Password required");
                    return;
                }

                if(password.length() < 5){
                    lPassword.setError("Password require 5 or more characters");
                    return;
                }

                // authenticate the user
                controller.fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Logged Successfully", Toast.LENGTH_SHORT).show();
                            controller.launchMain(LoginActivity.this);

                        }else {
                            Toast.makeText(LoginActivity.this, "Error !" +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void register(View view){
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        finish();
    }
}