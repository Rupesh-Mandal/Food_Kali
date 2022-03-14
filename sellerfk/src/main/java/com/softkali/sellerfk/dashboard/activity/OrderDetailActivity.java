package com.softkali.sellerfk.dashboard.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.softkali.sellerfk.R;
import com.softkali.sellerfk.dashboard.model.OrderModel;

public class OrderDetailActivity extends AppCompatActivity {

    ImageView productImageLink;
    TextView productName,productDescription,totalRate,productQuantity,deliverAddress,userName,userPhoneNumber,location;
    OrderModel orderModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        String object=getIntent().getStringExtra("OrderModel");
        orderModel=new Gson().fromJson(object, new TypeToken<OrderModel>() {
        }.getType());

        initView();
    }

    private void initView() {
        productImageLink=findViewById(R.id.productImageLink);
        productName=findViewById(R.id.productName);
        productDescription=findViewById(R.id.productDescription);
        totalRate=findViewById(R.id.totalRate);
        productQuantity=findViewById(R.id.productQuantity);
        deliverAddress=findViewById(R.id.deliverAddress);
        userName=findViewById(R.id.userName);
        userPhoneNumber=findViewById(R.id.userPhoneNumber);
        location=findViewById(R.id.location);


        Glide.with(this).load(orderModel.getProductImageLink()).into(productImageLink);
        productName.setText(orderModel.getProductName());
        productDescription.setText(orderModel.getProductDescription());
        totalRate.setText(orderModel.getTotalRate());
        productQuantity.setText(orderModel.getProductQuantity());
        deliverAddress.setText(orderModel.getDeliverAddress());
        userName.setText(orderModel.getUserName());
        userPhoneNumber.setText(orderModel.getUserPhoneNumber());
        location.setText(orderModel.getLocation());
    }
}