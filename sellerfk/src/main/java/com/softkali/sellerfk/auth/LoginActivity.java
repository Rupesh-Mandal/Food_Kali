package com.softkali.sellerfk.auth;


import static com.softkali.sellerfk.utils.Constant.sellerSignin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;
import com.softkali.sellerfk.R;
import com.softkali.sellerfk.dashboard.DashboardActivity;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity {

    EditText login_user_mobile,login_password;
    TextView login_btn_signup;
    Button login_btn_signin;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences("Food_Kali", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");

        initView();
    }

    private void initView() {
        login_user_mobile=findViewById(R.id.login_user_mobile);
        login_password=findViewById(R.id.login_password);
        login_btn_signin=findViewById(R.id.login_btn_signin);
        login_btn_signup=findViewById(R.id.login_btn_signup);

        login_btn_signup.setOnClickListener(v -> {
            startActivity(new Intent(this,SignUpActivity.class));
            finish();
        });
        login_btn_signin.setOnClickListener(v -> {
            if (isValid()){
                checkLogin();
            }
        });

    }


    boolean isValid() {
        if (login_user_mobile.getText().toString().trim().isEmpty()) {
            login_user_mobile.setError("Please enter mobile number");
            login_user_mobile.requestFocus();
            return false;

        } else {
            if (login_password.getText().toString().trim().isEmpty()) {
                login_password.setError("Please enter Password");
                login_password.requestFocus();
                return false;

            } else {
                return true;
            }
        }
    }

    private void checkLogin() {
        progressDialog.show();
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = sellerSignin;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("phoneNumber", login_user_mobile.getText().toString().trim());
            jsonBody.put("password", login_password.getText().toString().trim());
            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    JSONObject jsonObject;
                    try {
                        jsonObject=new JSONObject(response);
                        if (jsonObject.getBoolean("status")){
                            Toast.makeText(LoginActivity.this, jsonObject.getString("messag"), Toast.LENGTH_SHORT).show();
                            JSONObject userObject=jsonObject.getJSONObject("data");
                            editor.putString("user",userObject.toString());
                            editor.commit();
                            FirebaseMessaging.getInstance().subscribeToTopic(userObject.getString("sellerId"));
                            Log.e("abcd",userObject.getString("sellerId"));
                            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                            finish();
                        }else {
                            Toast.makeText(LoginActivity.this, jsonObject.getString("messag"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("abcd",e.toString());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Log.e("abcd",error.toString());
                    Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}