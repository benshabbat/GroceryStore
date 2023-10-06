package com.example.mygrocerystore.activities;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mygrocerystore.MainActivity;
import com.example.mygrocerystore.R;
import com.example.mygrocerystore.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DetailedActivity extends AppCompatActivity {


    EditText nameUpdate,priceuPdate;
    ImageView detailedImg , addItem, removeItem;
    TextView price,name, quantity;
    int totalQuantity = 1;
    int totalPrice =0;
    Button addToCart ,updateBtn;
    Toolbar toolbar;

    ViewAllModel viewAllModel = null;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

//        toolbar.findViewById(R.id.detailed_toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firestore = FirebaseFirestore.getInstance();
        auth =FirebaseAuth.getInstance();

        final Object object= getIntent().getSerializableExtra("detail");
        if(object instanceof ViewAllModel)
        {
            viewAllModel = (ViewAllModel) object;
        }

        detailedImg = findViewById(R.id.detailed_img);
        price = findViewById(R.id.detailed_price);
        name = findViewById(R.id.detailed_name);
        nameUpdate=findViewById(R.id.update_detailed_name);
        priceuPdate=findViewById(R.id.update_detailed_price);


        addItem = findViewById(R.id.detailed_add_item);
        removeItem = findViewById(R.id.detailed_remove_item);
        quantity = findViewById(R.id.detailed_quantity);

        if(viewAllModel!=null){
            Glide.with(getApplicationContext()).load(viewAllModel.getImg_url()).into(detailedImg);

            name.setText(viewAllModel.getName());
            nameUpdate.setText(viewAllModel.getName());
            price.setText("Price :$"+viewAllModel.getPrice()+"/kg");
            totalPrice = viewAllModel.getPrice()*totalQuantity;
            if(viewAllModel.getType().equals("egg"))
            {
                price.setText("Price :$"+viewAllModel.getPrice()+"/dozen");
                totalPrice = viewAllModel.getPrice()*totalQuantity;
            }
            if(viewAllModel.getType().equals("milk"))
            {
                price.setText("Price :$"+viewAllModel.getPrice()+"/liter");
                totalPrice = viewAllModel.getPrice()*totalQuantity;
            }
        }



        updateBtn = findViewById(R.id.detailed_update_btn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameProduct = nameUpdate.getText().toString();
                Integer priceProduct = Integer.valueOf(priceuPdate.getText().toString());
                updateProduct(nameProduct,priceProduct);
                startActivity(new Intent(DetailedActivity.this, MainActivity.class));
            }
        });
        addToCart = findViewById(R.id.detailed_add_to_cart);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addedToCart();
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalQuantity++;
                quantity.setText(String.valueOf(totalQuantity));
                totalPrice = viewAllModel.getPrice()*totalQuantity;
            }
        });

        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalQuantity>0) totalQuantity--;
                quantity.setText(String.valueOf(totalQuantity));
                totalPrice = viewAllModel.getPrice()*totalQuantity;
            }
        });
    }

    private void updateProduct(String nameProduct, Integer priceProduct) {
        Map<String,Object> updateProduct = new HashMap<>();
        updateProduct.put("name",nameProduct);
        updateProduct.put("price",priceProduct);
        firestore.collection("AllProducts").whereEqualTo("name",nameProduct)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            String documentId = documentSnapshot.getId();
                            firestore.collection("AllProducts")
                                    .document(documentId).update(updateProduct)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(DetailedActivity.this,"Successful",Toast.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(DetailedActivity.this,"Failed",Toast.LENGTH_SHORT).show();

                                        }
                                    });
                        }else {
                            Toast.makeText(DetailedActivity.this,"Failed we cant to do update",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void addedToCart() {
        String saveCurrentDate,saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String,Object> cartMap = new HashMap<>();

        cartMap.put("productName",viewAllModel.getName());
        cartMap.put("productPrice",price.getText().toString());
        cartMap.put("currentDate",saveCurrentDate);
        cartMap.put("currentTime",saveCurrentTime);
        cartMap.put("totalQuantity",quantity.getText().toString());
        cartMap.put("totalPrice",totalPrice);

        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(DetailedActivity.this,"Added To a Cart",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}