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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

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
    String recipeName;
    String description;
    String howto;
    String activity = "";
    String docId;
    String novaUrl;
    StorageReference httpsReference;
    List<Ingredient> ingredients = new ArrayList<>();

    Uri uri;
    Uri olduri;
    String huee;
    String hue;

    Boolean hasImg = false;
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
            recipeName = extras.getString("username");
            imgUrl = extras.getString("img");
            description = extras.getString("description");
            ingredients = getIntent().getParcelableArrayListExtra("ingredients");
            howto = extras.getString("howto");
            docId = extras.getString("docid");

            imgUri = Uri.parse(imgUrl);

            int[] spinner = new int[ingredients.size()];

            if(activity != null){

                Picasso.with(NewRecipeActivity.this).load(imgUrl).into(imageView);
                edName.setText(recipeName);
                edDesc.setText(description);
                edHow.setText(howto);


                for (int i = 0; i < ingredients.size(); i++) {
                    if(tipoSpinner(i,"KG")){
                        spinner[i] = 0;
                    }else if (tipoSpinner(i, "G")){
                        spinner[i] = 1;
                    }else if (tipoSpinner(i, "UN")){
                        spinner[i] = 2;
                    }else if (tipoSpinner(i, "ML")){
                        spinner[i] = 3;
                    }else{
                        spinner[i] = 4;
                    }
                }


                for (int i = 0; i < ingredients.size() ; i++) {
                    addView();
                }

                for (int i = 0; i <layoutList.getChildCount() ; i++) {

                    View recipeView = layoutList.getChildAt(i);

                    EditText editTextName = (EditText)recipeView.findViewById(R.id.edit_recipe_name);
                    AppCompatSpinner spinnerTeam = (AppCompatSpinner)recipeView.findViewById(R.id.spinner_unit);
                    EditText edQtd = (EditText)recipeView.findViewById(R.id.edit_recipe_qtd);

                    spinnerTeam.setSelection(spinner[i]);
                    editTextName.setText(ingredients.get(i).getIngredient());
                    edQtd.setText(ingredients.get(i).getQuantity());

                }

            }
        }



        btnNewIngredient.setOnClickListener(this);


        btnSaveRecipe.setOnClickListener(view -> {

            if (!hasImg && !activity.equals("edit")) {
                Toast.makeText(this,"Select an image before saving!",Toast.LENGTH_SHORT).show();
            } else if (edName.getText().toString().trim().equals("")) {
                Toast.makeText(this,"Empty recipe name. Empty field is not allowed!",Toast.LENGTH_SHORT).show();
            } else if (edDesc.getText().toString().trim().equals("")) {
                Toast.makeText(this,"Empty description. Empty field is not allowed!",Toast.LENGTH_SHORT).show();
            } else if (edHow.getText().toString().trim().equals("")) {
                Toast.makeText(this,"Empty how to. Empty field is not allowed!",Toast.LENGTH_SHORT).show();
            } else {

                uploadImg();

            }
        });
    }

    @Override
    public void onClick(View v) {
      addView();
    }

    public boolean tipoSpinner(int pos, String tipo){
        boolean result = false;

        if(ingredients.get(pos).getUnitType().equals(tipo)){
            result = true;
        };

        return result;
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
        recipeView.findViewById(R.id.edit_pantry).setVisibility(View.GONE);

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
        buildList();

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

        if(!activity.equals("")) {
            controller.fFirestore.collection("users").document(uId).collection("recipeBook").document(docId)
                    .set(recipe, SetOptions.merge())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    controller.countRecipes();
                    startActivity(new Intent(getApplicationContext(), RecipeActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(NewRecipeActivity.this, "edit erro: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            controller.fFirestore.collection("users").document(uId).collection("recipeBook")
                    .add(recipe)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            controller.countRecipes();
                            startActivity(new Intent(getApplicationContext(), RecipeActivity.class));
                            finish();
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Log.w(TAG, "Error adding document", e);
                }
            });
        }
    }

    public void uploadImg() {

        if (activity.equals("edit")) {

            if(hasImg)
            {
                uploadTask = storageReference
                        .child("images/" + imgUri.getLastPathSegment())
                        .putFile(imgUri);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Uri> task) {
                                novaUrl = task.getResult().toString();

                                if (activity.equals("edit")) {
                                    uri = Uri.parse(novaUrl);
                                    olduri = Uri.parse(imgUrl);
                                    huee = olduri.getLastPathSegment();
                                    hue = uri.getLastPathSegment();
                                } else {
                                    imgUrl = novaUrl;
                                }

                                Toast.makeText(NewRecipeActivity.this, "" + hue + "\n" + huee, Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "hue" + huee);

                                storageReference.child(huee).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        imgUrl = novaUrl;
                                        storeData();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {

                                    }
                                });
//
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception taskSnapshot) {
                        Toast.makeText(NewRecipeActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                storeData();
            }

        } else {
            uploadTask = storageReference.child("images/" + imgUri.getLastPathSegment()).putFile(imgUri);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Uri> task) {
                            novaUrl = task.getResult().toString();
                            imgUrl = novaUrl;
                            storeData();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {

                }
            });
        }
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
            hasImg = true;
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