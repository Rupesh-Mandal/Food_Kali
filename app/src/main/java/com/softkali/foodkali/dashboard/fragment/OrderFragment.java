package com.softkali.foodkali.dashboard.fragment;

import static com.softkali.foodkali.dashboard.DashboardActivity.authUser;
import static com.softkali.foodkali.utils.Constant.getAllOrder;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.softkali.foodkali.dashboard.adapter.OrderAdapter;
import com.softkali.foodkali.dashboard.model.OrderModel;
import com.softkali.foodkali.utils.OrderOnclick;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class OrderFragment extends Fragment {
    View view;
    RecyclerView orderRecycler;

    ArrayList<OrderModel> orderModelArrayList=new ArrayList<>();

    private static ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false);
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
        orderRecycler=view.findViewById(R.id.orderRecycler);
        orderRecycler.setLayoutManager(new GridLayoutManager(getContext(),1));


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait");


        loadOrder();
    }

    private void loadOrder() {
        progressDialog.show();
        String URL = getAllOrder;

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
                    orderModelArrayList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<OrderModel>>() {}.getType());
                    setOrder();
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

    private void setOrder() {
        OrderAdapter orderAdapter=new OrderAdapter(getContext(), orderModelArrayList, new OrderOnclick() {
            @Override
            public void onReceived(OrderModel orderModel) {

            }

            @Override
            public void onCancel(OrderModel orderModel) {

            }
        });
        orderRecycler.setAdapter(orderAdapter);
    }
}