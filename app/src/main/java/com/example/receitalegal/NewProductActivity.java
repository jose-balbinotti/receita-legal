package com.example.receitalegal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NewProductActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = null;
    ImageView imageView;
    EditText edName;
    EditText edDesc;
    EditText edHow;
    Button btnSaveRecipe;
//    List<Ingredient> ingredients = new ArrayList<>();
    LinearLayout layout;


    String uId;
    StorageReference storageReference;
    DocumentReference doc;

    LinearLayout layoutList;
    Button btnNewIngredient;
    Controller controller = Controller.getInstance();

    List<String> unitType = new ArrayList<>();
    List<Ingredient> productsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        layout = findViewById(R.id.linearlayout);
        layoutList = findViewById(R.id.layout_list);
        btnNewIngredient = findViewById(R.id.button_add);
        imageView = findViewById(R.id.imageViewRecipe);
        edName = findViewById(R.id.editTextRecipe);
        edDesc = findViewById(R.id.editTextDescription);
        edHow = findViewById(R.id.editTextHowTo);
        btnSaveRecipe = findViewById(R.id.btnSaveRecipe);

        uId = controller.getUid();

        storageReference = FirebaseStorage.getInstance().getReference();

        btnNewIngredient = findViewById(R.id.button_add);

        uId = controller.getUid();

        storageReference = FirebaseStorage.getInstance().getReference();

        unitType.add("KG");
        unitType.add("G");
        unitType.add("UN");
        unitType.add("ML");
        unitType.add("L");

        getPantry();

        btnNewIngredient.setOnClickListener(this);

        btnSaveRecipe.setOnClickListener(view -> {

            storeData();

        });

    }

    public void getPantry(){
        controller.fFirestore.collection("users").document(uId).collection("pantry").document(uId)
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                if(document.exists()){
                    productsList = document.toObject(IngredientsDocument.class).ingredients;

                    int[] spinner = new int[productsList.size()];

                    for (int i = 0; i < productsList.size(); i++) {
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

                    for (int i = 0; i <productsList.size() ; i++) {
                        addView();
                    }

                    for (int i = 0; i < layoutList.getChildCount(); i++) {


                        View recipeView = layoutList.getChildAt(i);

                        EditText editText = (EditText)recipeView.findViewById(R.id.edit_recipe_name);
                        EditText editQtd = (EditText)recipeView.findViewById(R.id.edit_recipe_qtd);
                        AppCompatSpinner spinnerTeam = (AppCompatSpinner)recipeView.findViewById(R.id.spinner_unit);

                        spinnerTeam.setSelection(spinner[i]);
                        editText.setText(productsList.get(i).getIngredient());
                        editQtd.setText(productsList.get(i).getQuantity());
                    }

                }else{}
            }
        });
    }

    public boolean tipoSpinner(int pos, String tipo){
        boolean result = false;

        if(productsList.get(pos).getUnitType().equals(tipo)){
            result = true;
        };

        return result;
    }

    public void buildList() {
        productsList.clear();

        for (int i = 0; i <layoutList.getChildCount() ; i++) {

            View recipeView = layoutList.getChildAt(i);

            EditText editTextName = (EditText)recipeView.findViewById(R.id.edit_recipe_name);
            AppCompatSpinner spinnerTeam = (AppCompatSpinner)recipeView.findViewById(R.id.spinner_unit);
            EditText edQtd = (EditText)recipeView.findViewById(R.id.edit_recipe_qtd);

            Ingredient ingredient = new Ingredient();
            ingredient.setIngredient(editTextName.getText().toString().trim());
            ingredient.setUnitType(unitType.get(spinnerTeam.getSelectedItemPosition()));
            ingredient.setQuantity(edQtd.getText().toString().trim());

            productsList.add(ingredient);

        }
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

    public void storeData() {

        buildList();

        Map<String, Object> product = new HashMap<>();

        product.put("ingredients", productsList);


        controller.fFirestore.collection("users").document(uId).collection("pantry").document(uId)
                .set(product, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        startActivity(new Intent(getApplicationContext(), ProductActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

            }
        });
    }


    private void removeView(View view){
        layoutList.removeView(view);
    }

    @Override
    public void onClick(View view) { addView();}
}