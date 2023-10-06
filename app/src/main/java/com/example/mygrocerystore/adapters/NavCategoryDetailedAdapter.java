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
import com.example.mygrocerystore.activities.DetailedActivity;
import com.example.mygrocerystore.models.NavCategoryDetailedModel;
import com.example.mygrocerystore.models.NavCategoryModel;

import java.util.List;

public class NavCategoryDetailedAdapter extends RecyclerView.Adapter<NavCategoryDetailedAdapter.ViewHolder>{

    private Context context;
    private List<NavCategoryDetailedModel> navCategoryDetailedModelModelList;

    public NavCategoryDetailedAdapter(Context context, List<NavCategoryDetailedModel> navCategoryDetailedModelModelList) {
        this.context = context;
        this.navCategoryDetailedModelModelList = navCategoryDetailedModelModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_category_detailed_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(navCategoryDetailedModelModelList.get(position).getImg_url()).into(holder.imageView);
        holder.name.setText(navCategoryDetailedModelModelList.get(position).getName());
        holder.price.setText(navCategoryDetailedModelModelList.get(position).getPrice()+"$");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailedActivity.class);
                intent.putExtra("detail",navCategoryDetailedModelModelList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return navCategoryDetailedModelModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name,price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.nav_cat_det_img);
            name = itemView.findViewById(R.id.nav_cat_det_name);
            price = itemView.findViewById(R.id.nav_cat_det_price);
        }
    }
}
