package com.example.receitalegal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class NewRecipeActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = null;
    ImageView imageView;
    EditText edName;
    EditText edDesc;
    Button btnSaveRecipe;
    UploadTask uploadTask;
    CollectionReference collectionReference;

    String imgUrl;

    FirebaseAuth fAuth;
    FirebaseFirestore fFirestore;
    CollectionReference ref;
    String uId;
    StorageReference storageReference;
    Uri imgUri;
    String groupID;
    LinearLayout layoutList;
    Button btnNewIngredient;

    List<String> unitType = new ArrayList<>();
    ArrayList<Ingredient> ingredientList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipe);

        layoutList = findViewById(R.id.layout_list);
        btnNewIngredient = findViewById(R.id.button_add);


        imageView = findViewById(R.id.imageViewRecipe);
        edName = findViewById(R.id.editTextRecipe);
        edDesc = findViewById(R.id.editTextDescription);
        btnSaveRecipe = findViewById(R.id.btnSaveRecipe);


        fAuth = FirebaseAuth.getInstance();
        uId = fAuth.getCurrentUser().getUid();

        fFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        unitType.add("Kg");
        unitType.add("g");
        unitType.add("Un");
        unitType.add("ml");
        unitType.add("l");


        btnNewIngredient.setOnClickListener(this);
        btnSaveRecipe.setOnClickListener(view -> {

            uploadImg();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            startActivity(new Intent(getApplicationContext(), RecipeActivity.class));
            finish();
        });


    }

    @Override
    public void onClick(View v) {
      addView();
    }

    public void buildList() {
        ingredientList.clear();

        for (int i = 0; i <layoutList.getChildCount() ; i++) {

            View recipeView = layoutList.getChildAt(i);

            EditText editTextName = (EditText)recipeView.findViewById(R.id.edit_recipe_name);
            AppCompatSpinner spinnerTeam = (AppCompatSpinner)recipeView.findViewById(R.id.spinner_unit);

            Ingredient ingredient = new Ingredient();
            ingredient.setIngredient(editTextName.getText().toString().trim());
            ingredient.setUnitType(unitType.get(spinnerTeam.getSelectedItemPosition()));


//            if(!editTextName.getText().toString().equals("")){
//                ingredient.setIngredient(editTextName.getText().toString());
//            }else{
//                result = false;
//                  break;
//           }
//
//            if(spinnerTeam.getSelectedItemPosition()!=0){
//                ingredient.setUnitType(unitType.get(spinnerTeam.getSelectedItemPosition()));
//            }else {
//              result = false;
//                break;
//            }
//
//            if(ingredientList.size()==0){
//                result = false;
//                Toast.makeText(this,"Ingrediente adicionado",Toast.LENGTH_SHORT).show();
//            }else if(!result){
//                Toast.makeText(this, "Faltando algo", Toast.LENGTH_SHORT).show();
//            }

            ingredientList.add(ingredient);

        }

//        if(ingredientList.size()==0){
//            result=false;
//            Toast.makeText(this, "add unit type", Toast.LENGTH_SHORT).show();
//        }else if(!result){
//            Toast.makeText(this, "enter unit type for all ingredientes", Toast.LENGTH_SHORT).show();
//        }

//

    }


    private void addView(){

        final View recipeView = getLayoutInflater().inflate(R.layout.row_add_recipe,null,false);

        EditText editText = (EditText)recipeView.findViewById(R.id.edit_recipe_name);
        AppCompatSpinner spinnerTeam = (AppCompatSpinner)recipeView.findViewById(R.id.spinner_unit);
        ImageView imageClose = (ImageView)recipeView.findViewById(R.id.image_remove);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, unitType);
        spinnerTeam.setAdapter(arrayAdapter);


        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeView(recipeView);
            }
        });

        layoutList.addView(recipeView);
    }

    private void removeView(View view){
        layoutList.removeView(view);
    }


    public void createMap(){

        ArrayList<Map<String, Object>> hue = new ArrayList<>();

    }

    public void storeData(){

        String vamo;
        String img = imgUrl;
        String name = edName.getText().toString().trim();
        String description = edDesc.getText().toString().trim();

        Map<String, Object> recipe = new HashMap<>();

        recipe.put("img",img);
        recipe.put("name", name);
        recipe.put("description", description);

        Map<String, Object> ingredients = new HashMap<>();
        for (int i = 0; i <ingredientList.size() ; i++) {
            ingredients.put("name"+i,ingredientList.get(i).getIngredient());
            ingredients.put("unit"+i,ingredientList.get(i).getUnitType());
        }


        fFirestore.collection("users").document(uId).collection("recipeBook")
                .add(recipe)
                .addOnSuccessListener(documentReference -> {
//                    groupID = documentReference.getId();
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));



        fFirestore.collection("users").document(uId).collection("recipeBook").document().collection("ingredients")
                .add(ingredients)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                })
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

    public void getDownloadLink() {
        buildList();

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