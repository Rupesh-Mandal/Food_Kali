package com.softkali.sellerfk.dashboard.fragment;

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
import com.softkali.sellerfk.R;
import com.softkali.sellerfk.dashboard.adapter.OrderAdapter;
import com.softkali.sellerfk.dashboard.model.OrderModel;
import com.softkali.sellerfk.utils.OrderOnclick;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.softkali.sellerfk.dashboard.DashboardActivity.authSeller;
import static com.softkali.sellerfk.utils.Constant.acceptOrder;
import static com.softkali.sellerfk.utils.Constant.cancelOrder;
import static com.softkali.sellerfk.utils.Constant.deliverdFaildOrder;
import static com.softkali.sellerfk.utils.Constant.deliverdOrder;
import static com.softkali.sellerfk.utils.Constant.getAllOrder;

public class OrderFragment extends Fragment {
    View view;
    ArrayList<OrderModel> orderModelArrayList = new ArrayList<>();
    RecyclerView orderRecycler;

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
        view = v;
        sharedPreferences = getContext().getSharedPreferences("Food_Kali", Context.MODE_PRIVATE);
        String user = sharedPreferences.getString("user", "");
        try {
            authSeller = new JSONObject(user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initView();
    }

    private void initView() {
        orderRecycler = view.findViewById(R.id.orderRecycler);
        orderRecycler.setLayoutManager(new GridLayoutManager(getContext(), 1));

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait");

        loadOrder();
    }

    private void loadOrder() {
        progressDialog.show();
        String URL = getAllOrder;

        final String mRequestBody = authSeller.toString();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e("abcd", response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Log.e("abcd", jsonArray.toString());
                    orderModelArrayList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<OrderModel>>() {
                    }.getType());
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

        OrderAdapter orderAdapter= new OrderAdapter(getContext(), orderModelArrayList, new OrderOnclick() {
            @Override
            public void onDeliverd(OrderModel orderModel) {
                orderAction(orderModel,deliverdOrder);
            }

            @Override
            public void onDeliverdFaild(OrderModel orderModel) {
                orderAction(orderModel,deliverdFaildOrder);
            }

            @Override
            public void onCancel(OrderModel orderModel, String massege) {
                orderAction(orderModel,cancelOrder);
            }

            @Override
            public void onAccept(OrderModel orderModel) {
                orderAction(orderModel,acceptOrder);
            }
        });
        orderRecycler.setAdapter(orderAdapter);

    }

    private void orderAction(OrderModel orderModel,String url) {
        progressDialog.show();
        String URL = url;

        final String mRequestBody = new Gson().toJson(orderModel);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e("abcd", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")){
                        Toast.makeText(getContext(), jsonObject.getString("messag"), Toast.LENGTH_SHORT).show();
                        loadOrder();
                    }else {
                        Toast.makeText(getContext(), jsonObject.getString("messag"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Something went wrong "+e.toString(), Toast.LENGTH_SHORT).show();

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
}