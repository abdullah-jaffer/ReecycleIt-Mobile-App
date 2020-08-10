package org.tensorflow.lite.examples.detection;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrivateDisposal extends AppCompatActivity {
    Button button;
    RecycleItApi recycleItApi;
    EditText address;
    String items;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_disposal);

        button = findViewById(R.id.button6);
        address = findViewById(R.id.editText);
        recycleItApi = RecycleItApi.retrofit.create(RecycleItApi.class);
        sharedpreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        address.setText(sharedpreferences.getString("address",""));
        Intent intent = getIntent();
        items = intent.getStringExtra("items");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAlert();
            }
        } );
    }

    public void sendAlert(){
        Call<ResponseBody> call = recycleItApi.addAlert(sharedpreferences.getString("email",""), Double.parseDouble(sharedpreferences.getString("lat", ""))
                , Double.parseDouble(sharedpreferences.getString("long", "")), address.getText().toString(),
                sharedpreferences.getString("country",""),
                sharedpreferences.getString("city",""),
                "PER",
                items);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(getApplicationContext(), "Alert Sent Successfully",Toast.LENGTH_LONG).show();
                addDisposal();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to Send Alert",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void addDisposal(){
        Call<ResponseBody> call = recycleItApi.addPersonalDisposal(sharedpreferences.getString("email",""), items, "PER");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(getApplicationContext(), "Saved",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to save",Toast.LENGTH_LONG).show();
            }
        });
    }

}
