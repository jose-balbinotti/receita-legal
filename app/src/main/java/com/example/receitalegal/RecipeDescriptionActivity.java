package com.example.receitalegal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;


public class RecipeDescriptionActivity extends AppCompatActivity {
    Button btn;
    ArrayList<RecipePreview> recipePreviewArrayList;

    Controller controller = Controller.getInstance();
    String uId = controller.getUid();
    String docId;
    Uri uriToDelete;

    FirebaseFirestore rootRef = controller.fFirestore;
    CollectionReference userRef = rootRef.collection("recipeBook");
    DocumentReference userIdRef = userRef.document(uId);
    List<Ingredient> ingredients = new ArrayList<>();
    List<Ingredient> productsList = new ArrayList<>();
    ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
    ArrayList<String> listOne = new ArrayList<>();
    ArrayList<String> listTwo = new ArrayList<>();
    StorageReference storageReference;

    private final String TAG = null;

    LinearLayout layout;
    String username = "Username not set";
    String imgurl = "Img not set";
    String description = "Description not set";
    String howto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_description);

        layout = findViewById(R.id.linearlayout);
        TextView nameTxt = findViewById(R.id.nameTextView);
        TextView descTxt = findViewById(R.id.descriptionTextView);
        ImageView img = findViewById(R.id.imageViewRecipe);
        ImageButton btnDeleteRecipe = findViewById(R.id.btnDeleteRecipe);
        ImageButton btnEditRecipe = findViewById(R.id.btnEditRecipe);

        storageReference = FirebaseStorage.getInstance().getReference();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            username = extras.getString("username");
            imgurl = extras.getString("img");
            description = extras.getString("description");
        }

        getPantry();

        Picasso.with(RecipeDescriptionActivity.this).load(imgurl).into(img);
        nameTxt.setText(username);
        descTxt.setText(description);

        btnEditRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), NewRecipeActivity.class);
                intent.putExtra("activity","edit");
                intent.putExtra("username", username);
                intent.putExtra("img", imgurl);
                intent.putExtra("description", description);
                intent.putExtra("howto", howto);
                intent.putExtra("docid", docId);

                for (int i = 0; i < ingredients.size() ; i++) {
                    ingredientArrayList.add(ingredients.get(i));
                }

                intent.putParcelableArrayListExtra("ingredients", ingredientArrayList);
                startActivity(intent);
            }
        });


        btnDeleteRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.fFirestore
                .collection("users")
                .document(uId).collection("recipeBook")
                .document(docId)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        Log.d(TAG,"DocumentSnapshot sucessfully deleted!");
                        uriToDelete = Uri.parse(imgurl);
                        storageReference.child(uriToDelete.getLastPathSegment()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                startActivity(new Intent(getApplicationContext(), RecipeActivity.class));
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.w(TAG,"Error deleting document", e);
                    }
                });
            }
        });
    }

    public void getRecipes(){
        controller.fFirestore.collection("users").document(uId).collection("recipeBook").whereEqualTo("img",imgurl)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                docId = document.getId();

                                ingredients = document.toObject(IngredientsDocument.class).ingredients;
                                howto = document.getString("howto");

                                int step= 1;

                                String[] result = howto.split(",");

                                for (int i = 0; i <result.length ; i++) {
                                    final View recipeView = getLayoutInflater().inflate(R.layout.row_add_recipe,null,false);
                                    recipeView.findViewById(R.id.spinner_unit).setVisibility(View.GONE);
                                    recipeView.findViewById(R.id.image_remove).setVisibility(View.GONE);
                                    recipeView.findViewById(R.id.edit_recipe_qtd).setVisibility(View.GONE);
                                    recipeView.findViewById(R.id.edit_pantry).setVisibility(View.GONE);

                                    EditText editText = (EditText)recipeView.findViewById(R.id.edit_recipe_name);

                                    editText.setEnabled(false);
                                    editText.setText("Step: " + step + " - " + result[i]);
                                    step++;

                                    layout.addView(recipeView);
//
                                }
                                step = 0;

                                TextView tv = new TextView(RecipeDescriptionActivity.this);
                                tv.setText("Ingredient List");
                                tv.setGravity(Gravity.CENTER);
                                step++;
                                layout.addView(tv);

                                int n = productsList.size();



                                for (int i = 0; i < ingredients.size(); i++) {
                                    final View recipeView = getLayoutInflater().inflate(R.layout.row_add_recipe,null,false);
                                    recipeView.findViewById(R.id.spinner_unit).setVisibility(View.GONE);
                                    recipeView.findViewById(R.id.image_remove).setVisibility(View.GONE);

                                    EditText editText = (EditText)recipeView.findViewById(R.id.edit_recipe_name);
                                    EditText editQtd = (EditText)recipeView.findViewById(R.id.edit_recipe_qtd);
                                    EditText editPantry = (EditText)recipeView.findViewById(R.id.edit_pantry);

                                    editText.setEnabled(false);
                                    editQtd.setEnabled(false);
                                    editPantry.setEnabled(false);

                                    if(n > 0) {
                                        for (int j = 0; j < n ; j++) {
                                            Boolean found = false;
                                            if(ingredients.get(i).getIngredient().equals(productsList.get(j).getIngredient())){
                                                editText.setText(ingredients.get(i).getIngredient());
                                                editPantry.setTextColor(Color.parseColor("#ff0000"));
                                                editPantry.setText(productsList.get(j).getQuantity() + " " + productsList.get(j).getUnitType());
                                                editQtd.setTextColor(Color.parseColor("#59981A"));
                                                editQtd.setText(ingredients.get(i).getQuantity() + " " + ingredients.get(i).getUnitType());
                                                found = true;
                                            }else{
                                                editText.setText(ingredients.get(i).getIngredient());
                                                editQtd.setText(ingredients.get(i).getQuantity() + " " + ingredients.get(i).getUnitType());
                                            }
                                        }

                                    }else{
                                        editText.setText(ingredients.get(i).getIngredient());
                                        editQtd.setText(ingredients.get(i).getQuantity());
                                    }

                                    layout.addView(recipeView);
                                }
                            }
                        }else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    public void getPantry(){

        controller.fFirestore.collection("users")
                .document(uId).collection("pantry")
                .document(uId)
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                if(document.exists()){
                    productsList = document.toObject(IngredientsDocument.class).ingredients;

                    getRecipes();
                    }else {
                    getRecipes();
                  }
                }
        });
    }
}