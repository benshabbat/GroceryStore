package com.example.mygrocerystore.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mygrocerystore.R;
import com.example.mygrocerystore.activities.ViewAllActivity;
import com.example.mygrocerystore.models.HomeCategory;


import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {



    private Context context;
    private List<HomeCategory> homeCategoryList;

    public HomeAdapter(Context context, List<HomeCategory> homeCategoryList) {
        this.context = context;
        this.homeCategoryList = homeCategoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_cat_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(homeCategoryList.get(position).getImg_url()).into(holder.catImg);
        holder.name.setText(homeCategoryList.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewAllActivity.class);
                intent.putExtra("type",homeCategoryList.get(position).getType());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return homeCategoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView catImg;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            catImg = itemView.findViewById(R.id.home_cat_img);
            name = itemView.findViewById(R.id.home_cat_name);
        }
    }
}
