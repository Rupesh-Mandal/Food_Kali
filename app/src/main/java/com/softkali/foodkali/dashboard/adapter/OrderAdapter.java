package com.softkali.foodkali.dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.softkali.foodkali.R;
import com.softkali.foodkali.dashboard.model.OrderModel;
import com.softkali.foodkali.utils.OrderOnclick;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.myViewHolder> {
    Context context;
    ArrayList<OrderModel> orderModelArrayList;
    OrderOnclick orderOnclick;

    public OrderAdapter(Context context, ArrayList<OrderModel> orderModelArrayList, OrderOnclick orderOnclick) {
        this.context = context;
        this.orderModelArrayList = orderModelArrayList;
        this.orderOnclick = orderOnclick;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myViewHolder(LayoutInflater.from(context).inflate(R.layout.order_item,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        OrderModel orderModel=orderModelArrayList.get(position);
        Glide.with(context).load(orderModel.getProductImageLink()).into(holder.productImageLink);

        holder.productName.setText(orderModel.getProductName());
        holder.totalRate.setText(orderModel.getTotalRate());
        holder.productQuantity.setText(orderModel.getProductQuantity());
        holder.statusMessage.setText(orderModel.getStatusMessage());

    }

    @Override
    public int getItemCount() {
        return orderModelArrayList.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageLink;
        TextView productName,totalRate,productQuantity,statusMessage;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageLink=itemView.findViewById(R.id.productImageLink);
            productName=itemView.findViewById(R.id.productName);
            totalRate=itemView.findViewById(R.id.totalRate);
            productQuantity=itemView.findViewById(R.id.productQuantity);
            statusMessage=itemView.findViewById(R.id.statusMessage);

        }
    }
}
