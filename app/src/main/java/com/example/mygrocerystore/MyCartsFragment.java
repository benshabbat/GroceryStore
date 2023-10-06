package com.example.mygrocerystore;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mygrocerystore.activities.DetailedActivity;
import com.example.mygrocerystore.activities.PlacedOrderActivity;
import com.example.mygrocerystore.adapters.MyCartAdapter;
import com.example.mygrocerystore.adapters.PopularAdapters;
import com.example.mygrocerystore.models.MyCartModel;
import com.example.mygrocerystore.models.PopularModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MyCartsFragment extends Fragment {

    RecyclerView myCartRec;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    MyCartAdapter myCartAdapter;
    List<MyCartModel> myCartModelList;
    TextView totalPriceAmount;
    ProgressBar progressBar;

    Button buyNow;
    public MyCartsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_my_carts, container, false);
        firestore= FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        buyNow= root.findViewById(R.id.buy_now);
        progressBar = root.findViewById(R.id.cart_progressbar);
        progressBar.setVisibility(View.VISIBLE);

        myCartRec= root.findViewById(R.id.rec_my_cart);
        myCartRec.setVisibility(View.GONE);
        totalPriceAmount= root.findViewById(R.id.total_price_amount);



        myCartRec.setLayoutManager(new LinearLayoutManager(getActivity()));
        myCartModelList = new ArrayList<>();
        myCartAdapter = new MyCartAdapter(getActivity(),myCartModelList);
        myCartRec.setAdapter(myCartAdapter);


        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){

                                String documentId = documentSnapshot.getId();

                                MyCartModel myCartModel = documentSnapshot.toObject(MyCartModel.class);
                                myCartModel.setDocumentId(documentId);
                                myCartModelList.add(myCartModel);
                                myCartAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                                myCartRec.setVisibility(View.VISIBLE);
                            }

                            calculateTotalAmount(myCartModelList);
                        }
                        else{
                            Toast.makeText(getActivity(),"Error"+task.getException(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            myCartRec.setVisibility(View.VISIBLE);
                        }
                    }
                });


        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PlacedOrderActivity.class);
                intent.putExtra("itemList", (Serializable) myCartModelList);
                startActivity(intent);
            }
        });
        return root;
    }

    private void calculateTotalAmount(List<MyCartModel> myCartModelList) {
        double totalAmount = 0.0;
        for(MyCartModel myCartModel:myCartModelList){
            totalAmount+= myCartModel.getTotalPrice();
        }
        totalPriceAmount.setText("The Total is:"+totalAmount);
    }


}