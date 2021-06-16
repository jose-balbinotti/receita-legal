package com.example.receitalegal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.grpc.Compressor;

public class NewRecipeActivity extends AppCompatActivity {

    public static final String TAG = null;
    ImageView imageView;
    EditText edName;
    EditText edDesc;
    EditText edIngredient1;
    EditText edIngredient2;
    EditText edIngredient3;
    Button btnSaveRecipe;
    UploadTask uploadTask;

    String imgUrl;

    FirebaseAuth fAuth;
    FirebaseFirestore fFirestore;
    String uId;
    StorageReference storageReference;

    Uri imgUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);

        imageView = findViewById(R.id.imageViewRecipe);
        edName = findViewById(R.id.editTextRecipe);
        edDesc = findViewById(R.id.editTextDescription);
        edIngredient1 = findViewById(R.id.editTextIng1);
        edIngredient2 = findViewById(R.id.editTextIng2);
        edIngredient3 = findViewById(R.id.editTextIng3);
        btnSaveRecipe = findViewById(R.id.btnSaveRecipe);

        fAuth = FirebaseAuth.getInstance();
        uId = fAuth.getCurrentUser().getUid();

        fFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();




        btnSaveRecipe.setOnClickListener(view -> {

                uploadImg();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            startActivity(new Intent(getApplicationContext(), RecipeActivity.class));
            finish();

//            String img = imgUrl;
//            String name = edName.getText().toString().trim();
//            String description = edDesc.getText().toString().trim();
//            String ingr1 = edIngredient1.getText().toString().trim();
//            String ingr2 = edIngredient2.getText().toString().trim();
//            String ingr3 = edIngredient3.getText().toString().trim();
//
//            Map<String, Object> recipe = new HashMap<>();
//            recipe.put("img",img);
//            recipe.put("name", name);
//            recipe.put("description", description);
//            recipe.put("ingr1", ingr1);
//            recipe.put("ingr2", ingr2);
//            recipe.put("ingr3", ingr3);
//
//
//            fFirestore.collection("users").document(uId).collection("recipeBook")
//                .add(recipe)
//                .addOnSuccessListener(documentReference -> {
//                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//
//
//
//
//
//
//                })
//                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
        });


    }

    public void storeData(){

        String img = imgUrl;
        String name = edName.getText().toString().trim();
        String description = edDesc.getText().toString().trim();
        String ingr1 = edIngredient1.getText().toString().trim();
        String ingr2 = edIngredient2.getText().toString().trim();
        String ingr3 = edIngredient3.getText().toString().trim();

        Map<String, Object> recipe = new HashMap<>();
        recipe.put("img",img);
        recipe.put("name", name);
        recipe.put("description", description);
        recipe.put("ingr1", ingr1);
        recipe.put("ingr2", ingr2);
        recipe.put("ingr3", ingr3);


        fFirestore.collection("users").document(uId).collection("recipeBook")
                .add(recipe)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                })
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

    public void getDownloadLink() {
        storageReference.child("images/"+imgUri.getLastPathSegment()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                imgUrl = uri.toString();
                storeData();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(NewRecipeActivity.this, "Deu erro", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void uploadImg(){
        uploadTask = storageReference.child("images/"+imgUri.getLastPathSegment()).putFile(imgUri);
        uploadTask.addOnSuccessListener(taskSnapshot -> getDownloadLink() //Toast.makeText(this, "rá", Toast.LENGTH_SHORT).show()
        ).addOnFailureListener(e -> Toast.makeText(NewRecipeActivity.this, "deu ruim", Toast.LENGTH_SHORT).show());

    }


    public void pickImg(View view){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        } else {
            Toast.makeText(this, "Permission Allowed", Toast.LENGTH_SHORT).show();
        }


        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,1);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode ==RESULT_OK){
            imgUri=data.getData();
            imageView.setImageURI(imgUri);
        }else {
            Toast.makeText(this, "you have not picked img", Toast.LENGTH_SHORT).show();
        }
    }



}