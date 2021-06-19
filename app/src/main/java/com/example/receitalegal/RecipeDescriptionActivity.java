package com.example.receitalegal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class RecipeDescriptionActivity extends AppCompatActivity {
    Button btn;
    ArrayList<Recipe> recipeArrayList;

    Controller controller = Controller.getInstance();
    String uId = controller.getUid();

    FirebaseFirestore rootRef = controller.fFirestore;
    CollectionReference userRef = rootRef.collection("recipeBook");
    DocumentReference userIdRef = userRef.document(uId);
    List<Ingredient> ingredients = new ArrayList<>();
    private final String TAG = null;

    String username = "Username not set";
    String imgurl = "Img not set";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_description);

        btn = findViewById(R.id.btn);
        TextView nameTxt = findViewById(R.id.nameTextView);
        ImageView img = findViewById(R.id.imageViewRecipe);


        Bundle extras = getIntent().getExtras();
        if(extras != null){
            username = extras.getString("username");
            imgurl = extras.getString("img");
        }

        nameTxt.setText(username);

        Picasso.with(RecipeDescriptionActivity.this).load(imgurl).into(img);
//        img.setImageURI(Uri.parse(imgurl));


    }

    public void ativa(View view){
        controller.fFirestore.collection("users").document(uId).collection("recipeBook").whereEqualTo("img",imgurl)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                List<Ingredient> ingredients;
                                ingredients = document.toObject(IngredientsDocument.class).ingredient;
                            }
                    }else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                }
//                    recipeArrayList.add(.toObject(Recipe.class));

                });
    }



}