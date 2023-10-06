package com.example.mygrocerystore.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mygrocerystore.R;
import com.example.mygrocerystore.adapters.NavCategoryDetailedAdapter;
import com.example.mygrocerystore.models.HomeCategory;
import com.example.mygrocerystore.models.NavCategoryDetailedModel;
import com.example.mygrocerystore.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NavCategoryDetailedActivity extends AppCompatActivity {
    ImageView addItem, removeItem;
    TextView price,name, quantity;
    int totalQuantity = 1;
    int totalPrice =0;
    RecyclerView navCatDetRec;
    List<NavCategoryDetailedModel> navCategoryDetailedModelList;
    NavCategoryDetailedAdapter navCategoryDetailedAdapter;

    FirebaseFirestore firestore;
    ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_category_detailed);

        firestore= FirebaseFirestore.getInstance();
        String type = getIntent().getStringExtra("type");

        navCatDetRec = findViewById(R.id.nav_cat_det_rec);
        progressBar = findViewById(R.id.nav_cat_det_progressbar);
        progressBar.setVisibility(View.VISIBLE);
        navCatDetRec.setVisibility(View.GONE);




        navCatDetRec.setLayoutManager(new LinearLayoutManager(this));
        navCategoryDetailedModelList = new ArrayList<>();
        navCategoryDetailedAdapter = new NavCategoryDetailedAdapter(this,navCategoryDetailedModelList);
        navCatDetRec.setAdapter(navCategoryDetailedAdapter);


        //Get Drink
        if(type !=null && type.equalsIgnoreCase("drink")) {
            firestore.collection("NavCategoryDetailed").whereEqualTo("type", "drink")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                NavCategoryDetailedModel navCategoryDetailedModel = documentSnapshot.toObject(NavCategoryDetailedModel.class);
                                navCategoryDetailedModelList.add(navCategoryDetailedModel);
                                navCategoryDetailedAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                                navCatDetRec.setVisibility(View.VISIBLE);
                            }

                        }
                    });
        }

        //Get Meat
        if(type !=null && type.equalsIgnoreCase("meat")) {
            firestore.collection("NavCategoryDetailed").whereEqualTo("type", "meat")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                NavCategoryDetailedModel navCategoryDetailedModel = documentSnapshot.toObject(NavCategoryDetailedModel.class);
                                navCategoryDetailedModelList.add(navCategoryDetailedModel);
                                navCategoryDetailedAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                                navCatDetRec.setVisibility(View.VISIBLE);
                            }

                        }
                    });
        }
    }
}