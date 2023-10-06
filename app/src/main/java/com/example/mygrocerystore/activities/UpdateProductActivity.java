package com.example.mygrocerystore.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mygrocerystore.MainActivity;
import com.example.mygrocerystore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class UpdateProductActivity extends AppCompatActivity {
    Button updateBtn;
    ImageView img;
    EditText name,price;
    FirebaseFirestore firestore;
    DatabaseReference databaseReference;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        firestore =  FirebaseFirestore.getInstance();
        name=findViewById(R.id.update_name);
        price=findViewById(R.id.update_price);

        updateBtn=findViewById(R.id.update_product_btn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameProduct = name.getText().toString();
                Integer priceProduct = Integer.valueOf(price.getText().toString());
                updateProduct(nameProduct,priceProduct);
                startActivity(new Intent(UpdateProductActivity.this, MainActivity.class));
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
                                            Toast.makeText(UpdateProductActivity.this,"Successful",Toast.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(UpdateProductActivity.this,"Failed",Toast.LENGTH_SHORT).show();

                                        }
                                    });
                        }else {
                            Toast.makeText(UpdateProductActivity.this,"Failed we cant to do update",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }


}