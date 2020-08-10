package org.tensorflow.lite.examples.detection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublicDisposal extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap map;
    String items;
    Button button;
    RecycleItApi recycleItApi;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_disposal);
        button = findViewById(R.id.button7);
        recycleItApi = RecycleItApi.retrofit.create(RecycleItApi.class);
        sharedpreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        Intent intent = getIntent();
        items = intent.getStringExtra("items");
        getCompleteAddress(Double.parseDouble(sharedpreferences.getString("long", "")),
                Double.parseDouble(sharedpreferences.getString("lat", "")) );
        SupportMapFragment mapFragment =  (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(PublicDisposal.this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAlert();
            }
        } );
    }

    public void sendAlert(){
        Call<ResponseBody> call = recycleItApi.addAlert(sharedpreferences.getString("email",""),
                Double.parseDouble(sharedpreferences.getString("lat", ""))
                ,  Double.parseDouble(sharedpreferences.getString("long", "")),
                getCompleteAddress(Double.parseDouble(sharedpreferences.getString("long", "")),
                        Double.parseDouble(sharedpreferences.getString("lat", "")) ),
                sharedpreferences.getString("country",""),
                sharedpreferences.getString("city",""),
                "PUB", items);
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
        Call<ResponseBody> call = recycleItApi.addPersonalDisposal(sharedpreferences.getString("email",""), items, "PUB");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(getApplicationContext(), "Saved",Toast.LENGTH_LONG).show();
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Could not save",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to save",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng currentLocation = new LatLng(Double.parseDouble(sharedpreferences.getString("lat", "")),
                Double.parseDouble(sharedpreferences.getString("long", "")));
        map.addMarker(new MarkerOptions().position(currentLocation).title(getCompleteAddress(Double.parseDouble(sharedpreferences.getString("long", "")),
                Double.parseDouble(sharedpreferences.getString("lat", "")) )));
        map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
    }

    private String getCompleteAddress(double longitude, double latitude){
        String address = "";
        Geocoder geocoder = new Geocoder(PublicDisposal.this, Locale.getDefault());

        try{

            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if(address != null){
                Address returnAddress = addresses.get(0);
                StringBuilder stringBuilderReturnAddress = new StringBuilder("");

                for(int i = 0; i < returnAddress.getMaxAddressLineIndex(); i++){
                    stringBuilderReturnAddress.append(returnAddress.getAddressLine(i)).append("\n");
                }

                address = stringBuilderReturnAddress.toString();
                Log.d("myLog", address);
            }else{
                Toast.makeText(getApplicationContext(),"No name for current location found", Toast.LENGTH_LONG);
            }
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG);
        }

        return  address;
    }



}
