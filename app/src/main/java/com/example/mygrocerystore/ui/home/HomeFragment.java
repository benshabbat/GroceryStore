package com.example.mygrocerystore.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygrocerystore.R;
import com.example.mygrocerystore.adapters.HomeAdapter;
import com.example.mygrocerystore.adapters.PopularAdapters;
import com.example.mygrocerystore.adapters.RecommendedAdapter;
import com.example.mygrocerystore.adapters.ViewAllAdapter;
import com.example.mygrocerystore.databinding.FragmentHomeBinding;
import com.example.mygrocerystore.models.HomeCategory;
import com.example.mygrocerystore.models.PopularModel;
import com.example.mygrocerystore.models.RecommendedModel;
import com.example.mygrocerystore.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    ScrollView scrollView;
    ProgressBar progressBar;

    RecyclerView popularRec,homeCatRec,recommendedRec;
    FirebaseFirestore db;


    //Search View
    EditText search_box;
    private List<ViewAllModel> viewAllModelList;
    private RecyclerView searchRec;
    private ViewAllAdapter viewAllAdapter;




    //Popular items
    List<PopularModel> popularModelList;
    PopularAdapters popularAdapters;

    //Home Category items

    List<HomeCategory> homeCategoryList;

    HomeAdapter homeAdapter;



    //Recommended items

    List<RecommendedModel> recommendedModelList;

    RecommendedAdapter recommendedAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_home, container, false);
        db= FirebaseFirestore.getInstance();

        popularRec = root.findViewById(R.id.pop_rec);
        homeCatRec = root.findViewById(R.id.explore_rec);
        recommendedRec = root.findViewById(R.id.recommended_rec);
        scrollView = root.findViewById(R.id.scroll_view);
        progressBar = root.findViewById(R.id.progressbar);

        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);

        //Popular items
        popularRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        popularModelList = new ArrayList<>();
        popularAdapters = new PopularAdapters(getActivity(),popularModelList);
        popularRec.setAdapter(popularAdapters);

        db.collection("PopularProduct")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document:task.getResult()){
                                PopularModel popularModel = document.toObject(PopularModel.class);
                                popularModelList.add(popularModel);
                                popularAdapters.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                                scrollView.setVisibility(View.VISIBLE);
                            }
                        }
                        else{
                            Toast.makeText(getActivity(),"Error"+task.getException(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });




        //Home Category items
        homeCatRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        homeCategoryList = new ArrayList<>();
        homeAdapter = new HomeAdapter(getActivity(),homeCategoryList);
        homeCatRec.setAdapter(homeAdapter);

        db.collection("HomeCategory")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document:task.getResult()){
                                HomeCategory homeCategory = document.toObject(HomeCategory.class);
                                homeCategoryList.add(homeCategory);
                                homeAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                                scrollView.setVisibility(View.VISIBLE);
                            }
                        }
                        else{
                            Toast.makeText(getActivity(),"Error"+task.getException(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //Recommended item
        recommendedRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        recommendedModelList = new ArrayList<>();
        recommendedAdapter = new RecommendedAdapter(getActivity(),recommendedModelList);
        recommendedRec.setAdapter(recommendedAdapter);

        db.collection("Recommended")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document:task.getResult()){
                                RecommendedModel recommendedModel = document.toObject(RecommendedModel.class);
                                recommendedModelList.add(recommendedModel);
                                recommendedAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                                scrollView.setVisibility(View.VISIBLE);
                            }
                        }
                        else{
                            Toast.makeText(getActivity(),"Error"+task.getException(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });




        //Search View
        search_box = root.findViewById(R.id.search_box);
        searchRec = root.findViewById(R.id.search_rec);

        viewAllModelList = new ArrayList<>();
        viewAllAdapter = new ViewAllAdapter(getActivity(),viewAllModelList);
        searchRec.setLayoutManager(new LinearLayoutManager(getContext()));
        searchRec.setAdapter(viewAllAdapter);
        searchRec.setHasFixedSize(true);

        search_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    viewAllModelList.clear();
                    viewAllAdapter.notifyDataSetChanged();
                }
                else{
                    searchProduct(s.toString());
                }
            }
        });


        return root;
    }

    private void searchProduct(String type) {
        if(!type.isEmpty()){
            db.collection("AllProducts")
                    .whereEqualTo("type",type).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful() &&task.getResult()!=null){
                        viewAllModelList.clear();
                        viewAllAdapter.notifyDataSetChanged();
                        for(DocumentSnapshot document:task.getResult().getDocuments()){
                            ViewAllModel viewAllModel = document.toObject(ViewAllModel.class);
                            viewAllModelList.add(viewAllModel);
                            viewAllAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                        }
                    }
                    else{
                        Toast.makeText(getActivity(),"Error"+task.getException(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}