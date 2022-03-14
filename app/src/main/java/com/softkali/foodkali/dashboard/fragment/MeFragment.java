package com.softkali.foodkali.dashboard.fragment;

import static com.softkali.foodkali.dashboard.DashboardActivity.authUser;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.softkali.foodkali.R;

import org.json.JSONException;
import org.json.JSONObject;


public class MeFragment extends Fragment {
    View view;

    SharedPreferences sharedPreferences;

    TextView name, phoneNumber, location;

    public MeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        view = v;

        sharedPreferences = getContext().getSharedPreferences("Food_Kali", Context.MODE_PRIVATE);
        String user = sharedPreferences.getString("user", "");
        Log.e("abcd", user);
        try {
            authUser = new JSONObject(user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            initView();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void initView() throws JSONException {

        name = view.findViewById(R.id.name);
        phoneNumber = view.findViewById(R.id.phoneNumber);
        location = view.findViewById(R.id.location);

        name.setText(authUser.getString("name"));
        phoneNumber.setText(authUser.getString("phoneNumber"));
        location.setText(authUser.getString("location"));

    }
}