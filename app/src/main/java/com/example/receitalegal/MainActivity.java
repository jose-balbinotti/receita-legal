package com.example.receitalegal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView txtNameUser;
    Controller controller = Controller.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtNameUser = findViewById(R.id.txtNameUser);
        txtNameUser.setText(controller.getName());
    }

    public void logout(View view){
        controller.fAuth.signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        controller = null;
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

    @Override
    public void onBackPressed(){
        startActivity(new Intent(this, MainActivity.class));
        finishAffinity();
        return;
    }
}