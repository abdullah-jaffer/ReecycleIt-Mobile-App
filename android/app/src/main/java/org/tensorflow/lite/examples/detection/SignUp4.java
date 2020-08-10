package org.tensorflow.lite.examples.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp4 extends AppCompatActivity {

    EditText address;
    Spinner country;
    Spinner city;
    Button button;
    String username;
    String password;
    String phone;
    String email;
    String selectedCountry;
    String selectedCity;
    RecycleItApi recycleItApi;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up4);

        address = findViewById(R.id.editText9);
        country = findViewById(R.id.country);
        city = findViewById(R.id.city);
        button = findViewById(R.id.finish);

        recycleItApi = RecycleItApi.retrofit.create(RecycleItApi.class);
        sharedpreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        String[] items = new String[]{"Pakistan", "United States", "India"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        country.setAdapter(adapter);
        HashMap<String, String[]> cities = new HashMap<>();
        cities.put("Pakistan",new String[]{"Lahore", "Karachi", "Islamabad"});
        cities.put("United States",new String[]{"New York", "Chicago", "Washington"});
        cities.put("India",new String[]{"Delho", "Mumbai"});
        final String[][] items1 = {null};

        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCountry = items[i];
                items1[0] = cities.get(selectedCountry);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUp4.this, android.R.layout.simple_spinner_dropdown_item, items1[0]);
                city.setAdapter(adapter);

                city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        selectedCity = items1[0][i];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Intent intent = getIntent();
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");
        phone = getIntent().getStringExtra("phone");
        email = getIntent().getStringExtra("email");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!address.getText().toString().isEmpty()){
                   createAcc();
                }else{
                    Toast.makeText(getApplicationContext(), "Please fill the address field", Toast.LENGTH_SHORT).show();
                }
            }
        } );
    }


    public void createAcc(){

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("username", username.trim());
        editor.putString("email", email.trim());
        editor.putString("password", password.trim());
        editor.putString("phone", phone.trim());
        editor.putString("address", address.getText().toString().trim());
        editor.putString("country", selectedCountry);
        editor.putString("city", selectedCity);

        editor.commit();

        create();
    }

    public void create(){
        Call<ResponseBody> call = recycleItApi.createAccount(email.trim(), username.trim(), password.trim(), address.getText().toString().trim(),
                selectedCountry, selectedCity, phone.trim());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(getApplicationContext(), "Account created successfully",Toast.LENGTH_LONG).show();
                Intent profile = new Intent(getApplicationContext(), Profile.class);
                startActivity(profile);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to create account",Toast.LENGTH_LONG).show();
            }
        });
    }
}
