package com.example.receitalegal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ProductActivity extends AppCompatActivity {

    TextView txtNameUser;
    TextView txtNumRecipes;
    ImageButton btnNewProduct;
    Controller controller = Controller.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        txtNameUser = findViewById(R.id.txtNameUser);
        txtNameUser.setText(controller.getName());

        txtNumRecipes = findViewById(R.id.txtNumRecipes);
        txtNumRecipes.setText(controller.getNumRecipes().toString() + " recipes");

        btnNewProduct = findViewById(R.id.btnNewProduct);

        btnNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), NewProductActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(this, MainActivity.class));
        finishAffinity();
        return;
    }

    public void logout(View view){
        controller.fAuth.signOut();
        controller.logout();
        controller = null;
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}