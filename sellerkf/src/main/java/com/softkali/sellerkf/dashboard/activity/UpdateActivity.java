package com.softkali.sellerkf.dashboard.activity;

import static com.softkali.sellerkf.utils.Constant.addProductUrl;
import static com.softkali.sellerkf.utils.Constant.updateProductUrl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.softkali.sellerkf.R;
import com.softkali.sellerkf.utils.ProductType;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class UpdateActivity extends AppCompatActivity {
    private final int storegePermision = 13;
    private final int storegePermisionForCamera = 35;
    private final int cameraPermisionFor = 73;

    RadioGroup productDeliverChargeRadioGroup;
    ImageView product_photo;

    TextInputEditText productName, productDescription, productRate, productDeliverCharge;

    Spinner product_type;

    TextInputLayout productDeliverChargeLayout;
    Button upload_product;
    public static boolean isCamera = false;
    public static String currentPhotoPath;

    String deliverCharge="Free";

    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;
    private StorageReference storageReference;

    JSONObject authSeller;
    JSONObject productObject=new JSONObject();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        sharedPreferences = getSharedPreferences("Food_Kali", Context.MODE_PRIVATE);
        String user=sharedPreferences.getString("user","");
        String object=getIntent().getStringExtra("product");
        storageReference = FirebaseStorage.getInstance().getReference();

        try {
            if (object!=null){
                productObject=new JSONObject(object);
            }
            authSeller=new JSONObject(user);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");

        initView();
        checkEdit();
    }


    private void checkEdit() {
        if (productObject!=null){
            try {
                productName.setText(productObject.getString("productName"));
                productDescription.setText(productObject.getString("productDescription"));
                productRate.setText(productObject.getString("productRate"));
                productRate.setText(productObject.getString("productRate"));

                String dCharge=productObject.getString("productDeliverCharge");
                if (!dCharge.equals("Free")){
                    RadioButton radioPaid=findViewById(R.id.radioPaid);
                    radioPaid.setChecked(true);
                    productDeliverCharge.setText(dCharge);
                }
                Picasso.get().load(productObject.getString("productImageLink")).into(product_photo);
                if (productObject.getString("productType").equals("NonVeg")){
                    product_type.setSelection(1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void initView() {
        productDeliverChargeRadioGroup = findViewById(R.id.productDeliverChargeRadioGroup);
        product_photo = findViewById(R.id.product_photo);
        productName = findViewById(R.id.productName);
        productDescription = findViewById(R.id.productDescription);
        productRate = findViewById(R.id.productRate);
        productDeliverCharge = findViewById(R.id.productDeliverCharge);
        product_type = findViewById(R.id.product_type);
        productDeliverChargeLayout = findViewById(R.id.productDeliverChargeLayout);
        upload_product = findViewById(R.id.upload_product);

        product_type.setAdapter(new ArrayAdapter<ProductType>(this, android.R.layout.simple_spinner_item, ProductType.values()));

        productDeliverChargeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioPaid) {
                    productDeliverChargeLayout.setVisibility(View.VISIBLE);
                } else {
                    productDeliverChargeLayout.setVisibility(View.GONE);
                }
            }
        });
        upload_product.setOnClickListener(view -> {
            if (isValid()) {
                uploadImage();
            }
        });
        product_photo.setOnClickListener(view -> {
            selectImage();
        });

    }

    private void selectImage() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.bottom_shee_dailog_theam);
        View v = LayoutInflater.from(this).
                inflate(R.layout.select_image_bottomsheet, (ConstraintLayout) findViewById(R.id.bottom_sheet_layout));
        bottomSheetDialog.setContentView(v);

        ImageView gallerBtn = v.findViewById(R.id.gallerBtn);
        ImageView cameraBtn = v.findViewById(R.id.cameraBtn);

        gallerBtn.setOnClickListener(view -> {
            if (checkPermissionForGallery()) {
                bottomSheetDialog.dismiss();
                pickFromGallery();
            }
        });

        cameraBtn.setOnClickListener(view -> {
            if (checkPermissionForCamara()) {
                bottomSheetDialog.dismiss();
                pickFromCamera();
            }
        });

        bottomSheetDialog.show();
    }

    private void uploadProduct(String imageDownloadUri) throws JSONException {
        progressDialog.show();

        String URL=updateProductUrl;

        productObject.put("productName",productName.getText().toString().trim());
        productObject.put("productDescription",productDescription.getText().toString().trim());
        productObject.put("productRate",productRate.getText().toString().trim());
        productObject.put("productDeliverCharge",deliverCharge);
        productObject.put("productImageLink",imageDownloadUri);
        productObject.put("productType",product_type.getSelectedItem().toString());
        productObject.put("authSeller",authSeller);

        final String mRequestBody = productObject.toString();
        RequestQueue requestQueue = Volley.newRequestQueue(this);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        Toast.makeText(UpdateActivity.this, jsonObject.getString("messag"), Toast.LENGTH_SHORT).show();
                        setResult(2);
                        finish();
                    } else {
                        Toast.makeText(UpdateActivity.this, jsonObject.getString("messag"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(UpdateActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("abcd",e.toString());

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("abcd", error.toString());
                Toast.makeText(UpdateActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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

    private void uploadImage() {
        progressDialog.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable) product_photo.getDrawable()).getBitmap();

        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] finalimage = baos.toByteArray();
        final StorageReference filepath;
        filepath = storageReference.child("Teachers").child(finalimage+".jpg");
        final UploadTask uploadTask = filepath.putBytes(finalimage);
        uploadTask.addOnCompleteListener(UpdateActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    progressDialog.dismiss();
                                    String imageDownloadUri = String.valueOf(uri);
                                    Log.e("abcd",imageDownloadUri);
                                    try {
                                        uploadProduct(imageDownloadUri);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(UpdateActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                        Log.e("abcd",e.toString());
                                    }

                                }
                            });
                        }
                    });
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(UpdateActivity.this, "Something went worng", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    public void pickFromCamera() {
        isCamera = true;
        String fileName = "photo";
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            File imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);
            currentPhotoPath = imageFile.getAbsolutePath();
            Uri imageUri = FileProvider.getUriForFile(UpdateActivity.this, "com.softkali.sellerkf.fileprovider", imageFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, 110);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pickFromGallery() {
        isCamera = false;
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);

    }

    private boolean checkPermissionForCamara() {
        int resultStoreg = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int resultCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (resultStoreg == PackageManager.PERMISSION_GRANTED) {
            if (resultCamera == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, cameraPermisionFor);
                return false;
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, storegePermisionForCamera);
            return false;
        }

    }


    private boolean checkPermissionForGallery() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, storegePermision);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case storegePermision:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickFromGallery();
                } else {
                    checkPermissionForGallery();
                }
                break;

            case storegePermisionForCamera:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissionForCamara();
                }
                break;


            case cameraPermisionFor:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissionForCamara();
                    pickFromCamera();
                } else {
                    checkPermissionForCamara();
                }
                break;


            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri imageUri = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                    getContentResolver(), imageUri);
                            product_photo.setImageBitmap(bitmap);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
        if (isCamera == true) {
            if (resultCode == RESULT_OK)
            {
                Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                product_photo.setImageBitmap(bitmap);
            }


        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    boolean isValid() {

        if (product_photo.getDrawable() == null) {
            Toast.makeText(this, "Please Select Product Photo", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (productName.getText().toString().trim().isEmpty()) {
                productName.setError("Please enter product Name");
                productName.requestFocus();
                return false;
            } else {
                if (productRate.getText().toString().trim().isEmpty()) {
                    productRate.setError("Please enter product Rate");
                    productRate.requestFocus();
                    return false;
                } else {
                    int id = productDeliverChargeRadioGroup.getCheckedRadioButtonId();
                    if (id == R.id.radioPaid) {
                        if (productDeliverCharge.getText().toString().trim().isEmpty()) {
                            productDeliverCharge.setError("Please enter product Deliver Charge");
                            productDeliverCharge.requestFocus();
                            return false;
                        } else {
                            deliverCharge=productDeliverCharge.getText().toString().trim();
                            return true;
                        }
                    } else {
                        deliverCharge="Free";
                        return true;
                    }
                }
            }
        }
    }

}