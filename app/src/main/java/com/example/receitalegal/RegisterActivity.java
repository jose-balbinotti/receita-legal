package com.example.receitalegal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = null;
    EditText rName, rEmail, rPassword;
    Button rBtnRegister;
    TextView rBtnAlready;
    String uId;
    Controller controller = Controller.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rName = findViewById(R.id.editTextPersonName);
        rEmail = findViewById(R.id.editTextEmailAddress);
        rPassword = findViewById(R.id.editTextPassword);
        rBtnRegister = findViewById(R.id.btnRegister);
        rBtnAlready = findViewById(R.id.btnAlready);

        if(controller.fAuth.getCurrentUser() != null){
            controller.launchMain(RegisterActivity.this);
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }


        rBtnRegister.setOnClickListener(view -> {
            String email = rEmail.getText().toString().trim();
            String password = rPassword.getText().toString().trim();
            String name = rName.getText().toString().trim();

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
            controller.fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "User created", Toast.LENGTH_SHORT).show();

                        //get user ID
                        uId = controller.fAuth.getCurrentUser().getUid();
                        //creating collection users
                        DocumentReference documentReference = controller.fFirestore.collection("users").document(uId);
                        Map<String,Object> user = new HashMap<>();
                        user.put("name",name);
                        user.put("email",email);

                        //insert into database
                        documentReference.set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                controller.launchMain(RegisterActivity.this);
//                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                              Log.d(TAG,"onSucess: USER PROFILE IS CREATED FOR "+ uId);
                            }
                        });


                    }else {
                        Toast.makeText(RegisterActivity.this, "Error !" +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        });

    }

    public void login(View view){
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(this, LoginActivity.class));
        finishAffinity();
        return;
    }
}