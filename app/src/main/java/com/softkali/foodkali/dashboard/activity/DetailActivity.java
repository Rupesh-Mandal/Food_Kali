package com.softkali.foodkali.dashboard.activity;


import static com.softkali.foodkali.utils.Constant.addOrder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.softkali.foodkali.R;
import com.softkali.foodkali.dashboard.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    ImageView productImageLink;
    TextView productName,productDescription,productRate,productType,productDeliverCharge,
            productHotelname,productEmail,productPhoneNumber,location;

    Product product;
    Button orderBtn;

    JSONObject useerObject;
    JSONObject productObject;

    SharedPreferences sharedPreferences;

    private static ProgressDialog progressDialog;

    boolean isOtherPhone=false;
    String otherNo="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail);
        String object=getIntent().getStringExtra("product");
        product=new Gson().fromJson(object, new TypeToken<Product>() {}.getType());
        sharedPreferences = getSharedPreferences("Food_Kali", Context.MODE_PRIVATE);

        String user = sharedPreferences.getString("user", "");

        try {
            productObject=new JSONObject(object);
            useerObject=new JSONObject(user);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        initView();
    }

    private void initView() {
        productImageLink=findViewById(R.id.productImageLink);
        productName=findViewById(R.id.productName);
        productDescription=findViewById(R.id.productDescription);
        productRate=findViewById(R.id.productRate);
        productType=findViewById(R.id.productType);
        productDeliverCharge=findViewById(R.id.productDeliverCharge);
        productHotelname=findViewById(R.id.productHotelname);
        productEmail=findViewById(R.id.productEmail);
        productPhoneNumber=findViewById(R.id.productPhoneNumber);
        location=findViewById(R.id.location);
        orderBtn=findViewById(R.id.orderBtn);

        Glide.with(this).load(product.getProductImageLink()).into(productImageLink);

        productName.setText(product.getProductName());
        productDescription.setText(product.getProductDescription());
        productRate.setText(product.getProductRate());
        productType.setText(product.getProductType().toString());
        productDeliverCharge.setText(product.getProductDeliverCharge());
        productHotelname.setText(product.getProductHotelname());
        productEmail.setText(product.getProductEmail());
        productPhoneNumber.setText(product.getProductPhoneNumber());
        location.setText(product.getLocation().toString());
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");

        orderBtn.setOnClickListener(v -> {
            showBottomsheet();
        });
    }

    private void showBottomsheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(DetailActivity.this, R.style.bottom_shee_dailog_theam);
        View v = LayoutInflater.from(DetailActivity.this).
                inflate(R.layout.add_to_cart_bottomsheet, (ConstraintLayout) findViewById(R.id.bottom_sheet_layout));
        bottomSheetDialog.setContentView(v);
        RadioGroup userPhoneNumberRadioGroup;
        userPhoneNumberRadioGroup=v.findViewById(R.id.userPhoneNumberRadioGroup);

        EditText otherPhone=v.findViewById(R.id.otherPhone);

        userPhoneNumberRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.otherPhoneRadio){
                    isOtherPhone=true;
                    otherPhone.setVisibility(View.VISIBLE);
                }else {
                    isOtherPhone=false;
                    otherPhone.setVisibility(View.GONE);
                }
            }
        });

        ImageView imageView = v.findViewById(R.id.img);
        EditText delivery_address = v.findViewById(R.id.delivery_address);
        TextView count_negative, count, count_positive,item_name,item_rate;
        AppCompatButton add_to_cart_btn;

        count_negative = v.findViewById(R.id.count_negative);
        count = v.findViewById(R.id.count);
        count_positive = v.findViewById(R.id.count_positive);
        item_name = v.findViewById(R.id.item_name);
        item_rate = v.findViewById(R.id.item_rate);
        add_to_cart_btn = v.findViewById(R.id.add_to_cart_btn);

        item_name.setText(product.getProductName());
        item_rate.setText(product.getProductRate());

        Glide.with(this).load(product.getProductImageLink()).into(imageView);

        count_negative.setOnClickListener(view1 -> {

            double r= Double.parseDouble(product.getProductRate());
            int c = Integer.parseInt(String.valueOf(count.getText()));

            if (c>1){
                count.setText(String.valueOf(c-1));
                item_rate.setText(String.valueOf((c-1)*r));
            }
        });
        count_positive.setOnClickListener(view1 -> {

            double r= Double.parseDouble(product.getProductRate());
            int c = Integer.parseInt(String.valueOf(count.getText()));

            count.setText(String.valueOf(c+1));
            item_rate.setText(String.valueOf((c+1)*r));

        });
        add_to_cart_btn.setOnClickListener(view1 -> {
            if (delivery_address.getText().toString().trim().isEmpty()){
                delivery_address.setError("Please give Delivery Address");
                delivery_address.requestFocus();
            }else {
                try {
                    if (isOtherPhone){
                        if (otherPhone.getText().toString().trim().isEmpty()){
                            otherPhone.setError("Please Provide Phone Number");
                            otherPhone.requestFocus();
                        }else {
                            otherNo=otherPhone.getText().toString().trim();
                            order(delivery_address.getText().toString().trim(),count.getText().toString().trim(),item_rate.getText().toString());
                            bottomSheetDialog.dismiss();
                        }
                    }else {
                        order(delivery_address.getText().toString().trim(),count.getText().toString().trim(),item_rate.getText().toString());
                        bottomSheetDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DetailActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("abcd",e.toString());
                }
            }

        });

        bottomSheetDialog.show();

    }

    private void order(String deliverAddress, String productQuantity, String totalRate) throws JSONException {
        progressDialog.show();
        String URL = addOrder;
        if (isOtherPhone){
            useerObject.put("phoneNumber",otherNo);
        }

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("product",productObject);
        jsonObject.put("authUser",useerObject);
        jsonObject.put("deliverAddress",deliverAddress);
        jsonObject.put("productQuantity",productQuantity);
        jsonObject.put("totalRate",totalRate);

        final String mRequestBody = jsonObject.toString();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e("abcd", response);
                try {
                    JSONObject responseObject=new JSONObject(response);
                    if (responseObject.getBoolean("status")){
                        Toast.makeText(DetailActivity.this, responseObject.getString("messag"), Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(DetailActivity.this, responseObject.getString("messag"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DetailActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("abcd",e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("abcd", error.toString());
                Toast.makeText(DetailActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }


}