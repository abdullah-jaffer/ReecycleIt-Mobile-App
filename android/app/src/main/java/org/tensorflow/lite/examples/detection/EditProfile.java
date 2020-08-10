package org.tensorflow.lite.examples.detection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfile extends AppCompatActivity {

    private FloatingActionButton floatingButton;
    EditText username;
    EditText email;
    EditText phone;
    EditText address;
    EditText country;
    EditText password;
    EditText city;
    SharedPreferences sharedpreferences;
    Button update;
    RecycleItApi recycleItApi;
    String originalEmail;
    ProgressDialog progressDoalog;

    private View.OnClickListener buttonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), DetectorActivity.class));
            overridePendingTransition(0,0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        floatingButton = findViewById(R.id.floatingActionButton);
        floatingButton.setOnClickListener(buttonOnClickListener);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        recycleItApi = RecycleItApi.retrofit.create(RecycleItApi.class);
        sharedpreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        country = findViewById(R.id.country);
        city = findViewById(R.id.city);
        password = findViewById(R.id.password);
        update = findViewById(R.id.update);
        progressDoalog = new ProgressDialog(EditProfile.this);
        progressDoalog.setMax(50);
        progressDoalog.setMessage("Updating data, please wait....");
        progressDoalog.setTitle("Saving");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        originalEmail = sharedpreferences.getString("email","");
        username.setText(sharedpreferences.getString("username",""));
        email.setText(sharedpreferences.getString("email",""));
        phone.setText(sharedpreferences.getString("phone",""));
        address.setText(sharedpreferences.getString("address",""));
        country.setText(sharedpreferences.getString("country",""));
        city.setText(sharedpreferences.getString("city",""));
        password.setText(sharedpreferences.getString("password",""));

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDoalog.show();
                update();
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.navigation_cam:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_goal:
                        startActivity(new Intent(getApplicationContext(), Goals.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_stats:
                        startActivity(new Intent(getApplicationContext(), Stats.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_history:
                        startActivity(new Intent(getApplicationContext(), History.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_requests:
                        startActivity(new Intent(getApplicationContext(), Requests.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });
    }


    public void update(){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("username", username.getText().toString().trim());
        editor.putString("email", email.getText().toString().trim());
        editor.putString("password", password.getText().toString().trim());
        editor.putString("phone", phone.getText().toString().trim());
        editor.putString("address", address.getText().toString().trim());
        editor.putString("country", country.getText().toString().trim());
        editor.putString("city", city.getText().toString().trim());
        editor.commit();
        Call<ResponseBody> call = recycleItApi.updateAccount(originalEmail,
                email.getText().toString().trim(),
                username.getText().toString().trim(),
                password.getText().toString().trim(),
                address.getText().toString().trim(),
                country.getText().toString().trim(),
                city.getText().toString().trim(),
                phone.getText().toString().trim());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDoalog.dismiss();
                if(response.body() == null){
                    Toast.makeText(getApplicationContext(), "Problem occurred",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Updated successfully",Toast.LENGTH_LONG).show();
                    originalEmail = email.getText().toString().trim();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to Send Alert",Toast.LENGTH_LONG).show();
            }
        });
    }
}
