package com.example.receitalegal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class RecipeDescriptionActivity extends AppCompatActivity {
    Button btn;
    ArrayList<RecipePreview> recipePreviewArrayList;

    Controller controller = Controller.getInstance();
    String uId = controller.getUid();
    String docId;

    FirebaseFirestore rootRef = controller.fFirestore;
    CollectionReference userRef = rootRef.collection("recipeBook");
    DocumentReference userIdRef = userRef.document(uId);
    List<Ingredient> ingredients = new ArrayList<>();
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


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            username = extras.getString("username");
            imgurl = extras.getString("img");
            description = extras.getString("description");
        }



        Picasso.with(RecipeDescriptionActivity.this).load(imgurl).into(img);
        nameTxt.setText(username);
        descTxt.setText(description);



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
                        startActivity(new Intent(getApplicationContext(), RecipeActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.w(TAG,"Error deleting document", e);
                    }
                });
            }
        });


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

                                for (int i = 0; i < ingredients.size(); i++) {
                                    final View recipeView = getLayoutInflater().inflate(R.layout.row_add_recipe,null,false);
                                    recipeView.findViewById(R.id.spinner_unit).setVisibility(View.GONE);
                                    recipeView.findViewById(R.id.image_remove).setVisibility(View.GONE);

                                    EditText editText = (EditText)recipeView.findViewById(R.id.edit_recipe_name);
                                    EditText editQtd = (EditText)recipeView.findViewById(R.id.edit_recipe_qtd);
                                    ImageView imageClose = (ImageView)recipeView.findViewById(R.id.image_remove);

                                    editText.setEnabled(false);
                                    editQtd.setEnabled(false);

                                    editText.setText(ingredients.get(i).getIngredient());
                                    editQtd.setText(ingredients.get(i).getQuantity() + " " + ingredients.get(i).getUnitType());


                                    layout.addView(recipeView);

                                }

//

                            }
                        }else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
//

                });
    }
}