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
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewRecipeActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = null;
    ImageView imageView;
    EditText edName;
    EditText edDesc;
    EditText edHow;
    Button btnSaveRecipe;
    UploadTask uploadTask;

    String imgUrl;
    String username;
    String description;
    String howto;
    String activity;
    List<Ingredient> ingredients = new ArrayList<>();

    String uId;
    StorageReference storageReference;
    Uri imgUri;
    LinearLayout layoutList;
    Button btnNewIngredient;
    Controller controller = Controller.getInstance();

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
        edHow = findViewById(R.id.editTextHowTo);
        btnSaveRecipe = findViewById(R.id.btnSaveRecipe);

        uId = controller.getUid();

        storageReference = FirebaseStorage.getInstance().getReference();

        unitType.add("KG");
        unitType.add("G");
        unitType.add("UN");
        unitType.add("ML");
        unitType.add("L");


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            activity = extras.getString("activity");
            username = extras.getString("username");
            imgUrl = extras.getString("img");
            description = extras.getString("description");
            ingredients = getIntent().getParcelableArrayListExtra("ingredients");
            howto = extras.getString("howto");

            System.out.println(activity);
            System.out.println(username);
            System.out.println(imgUrl);
            System.out.println(description);
            System.out.println(ingredients);
            System.out.println(howto);
        }

        if(activity.equals("edit")){

            for (int i = 0; i < ingredients.size() ; i++) {
                addView();
            }

            for (int i = 0; i <layoutList.getChildCount() ; i++) {

                View recipeView = layoutList.getChildAt(i);

                EditText editTextName = (EditText)recipeView.findViewById(R.id.edit_recipe_name);
                AppCompatSpinner spinnerTeam = (AppCompatSpinner)recipeView.findViewById(R.id.spinner_unit);
                EditText edQtd = (EditText)recipeView.findViewById(R.id.edit_recipe_qtd);

                ingredients.get(i).getUnitType();

                spinnerTeam.setSelection(3);
                editTextName.setText(ingredients.get(i).getIngredient());
                edQtd.setText(ingredients.get(i).getQuantity());

            }

        }

        btnNewIngredient.setOnClickListener(this);


        btnSaveRecipe.setOnClickListener(view -> {
            if (imageView.getDrawable() == null) {
                Toast.makeText(this,"Select an image before saving!",Toast.LENGTH_SHORT).show();
            } else if (edName.getText().toString().trim().equals("")) {
                Toast.makeText(this,"Empty recipe name. Empty field is not allowed!",Toast.LENGTH_SHORT).show();
            } else if (edDesc.getText().toString().trim().equals("")) {
                Toast.makeText(this,"Empty description. Empty field is not allowed!",Toast.LENGTH_SHORT).show();
            } else if (edHow.getText().toString().trim().equals("")) {
                Toast.makeText(this,"Empty how to. Empty field is not allowed!",Toast.LENGTH_SHORT).show();
            } else {
                uploadImg();

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                startActivity(new Intent(getApplicationContext(), RecipeActivity.class));
                finish();
            }
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
            EditText edQtd = (EditText)recipeView.findViewById(R.id.edit_recipe_qtd);

            Ingredient ingredient = new Ingredient();
            ingredient.setIngredient(editTextName.getText().toString().trim());
            ingredient.setUnitType(unitType.get(spinnerTeam.getSelectedItemPosition()));
            ingredient.setQuantity(edQtd.getText().toString().trim());

            ingredientList.add(ingredient);

        }
    }


    //add new line to ingredient
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

    //remove line of ingredient
    private void removeView(View view){
        layoutList.removeView(view);
    }

    public void storeData(){

        String img = imgUrl;
        String name = edName.getText().toString().trim();
        String description = edDesc.getText().toString().trim();
        String howto = edHow.getText().toString().trim();

        Map<String, Object> recipe = new HashMap<>();

        recipe.put("img",img);
        recipe.put("name", name);
        recipe.put("description", description);
        recipe.put("howto", howto);
        recipe.put("ingredients", ingredientList);

        controller.fFirestore.collection("users").document(uId).collection("recipeBook")
                .add(recipe)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
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

    @Override
    public void onBackPressed(){
        startActivity(new Intent(this, RecipeActivity.class));
        finishAffinity();
        return;
    }
}