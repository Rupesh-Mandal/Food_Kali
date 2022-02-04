package com.softkali.foodkali.dashboard.adapter;

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
import com.google.gson.Gson;
import com.softkali.foodkali.R;
import com.softkali.foodkali.dashboard.activity.DetailActivity;
import com.softkali.foodkali.dashboard.model.Product;
import com.softkali.foodkali.utils.ProductOnclick;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.myViewHolder> {

    Context context;
    ArrayList<Product> productArrayList;
    ProductOnclick productOnclick;

    public ProductAdapter(Context context, ArrayList<Product> productArrayList, ProductOnclick productOnclick) {
        this.context = context;
        this.productArrayList = productArrayList;
        this.productOnclick = productOnclick;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myViewHolder(LayoutInflater.from(context).inflate(R.layout.product_item,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Product product=productArrayList.get(position);
        holder.productName.setText(product.getProductName());
        holder.productRate.setText("Rs."+product.getProductRate());
        Glide.with(context).load(product.getProductImageLink()).into(holder.productImageLink);
        holder.itemView.setOnClickListener(v -> {
            productOnclick.onClick(product);
        });
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageLink;
        TextView productName,productRate;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageLink=itemView.findViewById(R.id.productImageLink);
            productName=itemView.findViewById(R.id.productName);
            productRate=itemView.findViewById(R.id.productRate);
        }
    }
}
