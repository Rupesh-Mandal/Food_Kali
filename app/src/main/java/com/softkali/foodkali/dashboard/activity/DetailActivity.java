package com.softkali.foodkali.dashboard.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.softkali.foodkali.R;
import com.softkali.foodkali.dashboard.model.Product;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    ImageView productImageLink;
    TextView productName,productDescription,productRate,productType,productDeliverCharge,
            productHotelname,productEmail,productPhoneNumber,location;

    Product product;
    Button orderBtn;


    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAMv27dXQ:APA91bG-yTg2kMnSjyub49--SVrjU1TTS_CvRaK1E9JWxN3y50GvWB3HPIUAtbA0hsf-kKyV06OcOrnxmyTmhpfsAkoyV2gVjurqUKMxvyXL8LtpxCKDSEuxLxNSAd0mgdj7IRDxcrNM";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail);
        String object=getIntent().getStringExtra("product");
        product=new Gson().fromJson(object, new TypeToken<Product>() {}.getType());
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

        orderBtn.setOnClickListener(v -> {
            TOPIC = "/topics/"+product.getSellerId(); //topic must match with what the receiver subscribed to
            NOTIFICATION_TITLE = "New Order"+product.getProductName();
            NOTIFICATION_MESSAGE = product.getProductDescription();



// Send a message to the devices subscribed to the provided topic.
            JSONObject notification = new JSONObject();
            JSONObject notifcationBody = new JSONObject();
            try {
                notifcationBody.put("title", NOTIFICATION_TITLE);
                notifcationBody.put("body", NOTIFICATION_MESSAGE);
                notification.put("to", TOPIC);
                notification.put("notification", notifcationBody);
            } catch (JSONException e) {
                Log.e(TAG, "onCreate: " + e.getMessage() );
            }
            sendNotification(notification);
        });
    }

    private void sendNotification(JSONObject notification) {
        Log.e("abcd",notification.toString());
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Pleae wait");
        progressDialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        Log.e("abcd",response.toString());
                        progressDialog.dismiss();
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailActivity.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                        progressDialog.dismiss();
                    }
                }){
            @Override
            public Map getHeaders() throws AuthFailureError {
                Map params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    static class MySingleton {
        private static MySingleton instance;
        private RequestQueue requestQueue;
        private Context ctx;

        private MySingleton(Context context) {
            ctx = context;
            requestQueue = getRequestQueue();
        }
        public static synchronized MySingleton getInstance(Context context) {
            if (instance == null) {
                instance = new MySingleton(context);
            }
            return instance;
        }
        public RequestQueue getRequestQueue() {
            if (requestQueue == null) {
                // getApplicationContext() is key, it keeps you from leaking the
                // Activity or BroadcastReceiver if someone passes one in.
                requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
            }
            return requestQueue;
        }
        public  void addToRequestQueue(Request req) {
            getRequestQueue().add(req);
        }
    }
}