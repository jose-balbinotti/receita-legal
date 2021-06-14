package com.example.receitalegal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText rName, rEmail, rPassword;
    Button rBtnRegister;
    TextView rBtnAlready;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rName = findViewById(R.id.editTextPersonName);
        rEmail = findViewById(R.id.editTextEmailAddress);
        rPassword = findViewById(R.id.editTextPassword);
        rBtnRegister = findViewById(R.id.btnRegister);
        rBtnAlready = findViewById(R.id.btnAlready);

        fAuth = FirebaseAuth.getInstance();

        if(fAuth.getCurrentUser() != null){
            //fAuth.signOut();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            //Toast.makeText(RegisterActivity.this, "Já está logado !" , Toast.LENGTH_SHORT).show();
            finish();
        }

        // set listener to btn and validate data

        rBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = rEmail.getText().toString().trim();
                String password = rPassword.getText().toString().trim();

                if(email.isEmpty()){
                    rEmail.setError("email required");
                    return;
                }

                if(password.isEmpty()){
                    rPassword.setError("password required");
                    return;
                }

                if(password.length() < 5){
                    rPassword.setError("password require 5 or more characters");
                    return;
                }

                // register into firebase

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "User created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }else {
                            Toast.makeText(RegisterActivity.this, "Error !" +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }
        });

    }
}