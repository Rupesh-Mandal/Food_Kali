package com.softkali.foodkali.dashboard.fragment;

import static com.softkali.foodkali.dashboard.DashboardActivity.authUser;
import static com.softkali.foodkali.utils.Constant.getAllProductUrl;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.softkali.foodkali.R;
import com.softkali.foodkali.dashboard.activity.DetailActivity;
import com.softkali.foodkali.dashboard.adapter.ProductAdapter;
import com.softkali.foodkali.dashboard.model.Product;
import com.softkali.foodkali.utils.ProductOnclick;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    View view;
    RecyclerView product_recycle;
    ArrayList<Product> productArrayList=new ArrayList<>();
    private static ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        view=v;
        sharedPreferences = getContext().getSharedPreferences("Food_Kali", Context.MODE_PRIVATE);
        String user = sharedPreferences.getString("user", "");
        Log.e("abcd",user);
        try {
            authUser = new JSONObject(user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initView();
    }

    private void initView() {
        product_recycle=view.findViewById(R.id.product_recycle);
        product_recycle.setLayoutManager(new GridLayoutManager(getContext(),2));


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait");

        loadProduct();
    }

    private void loadProduct() {
        progressDialog.show();
        String URL = getAllProductUrl;

        final String mRequestBody = authUser.toString();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e("abcd", response);
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    Log.e("abcd",jsonArray.toString());
                    productArrayList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<Product>>() {}.getType());
                    setProdduct();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("abcd", error.toString());
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
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

    private void setProdduct() {
        ProductAdapter productAdapter=new ProductAdapter(getContext(), productArrayList, new ProductOnclick() {
            @Override
            public void onClick(Product product) {
                Intent intent=new Intent(getContext(), DetailActivity.class);
                intent.putExtra("product",new Gson().toJson(product));
                getContext().startActivity(intent);
            }
        });
        product_recycle.setAdapter(productAdapter);
    }
}