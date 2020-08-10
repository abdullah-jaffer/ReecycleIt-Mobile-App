package org.tensorflow.lite.examples.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button button;
    private Button button1;
    RecycleItApi recycleItApi;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.editText2);
        password = findViewById(R.id.editText3);
        button = findViewById(R.id.login);
        button1 = findViewById(R.id.signup);
        recycleItApi = RecycleItApi.retrofit.create(RecycleItApi.class);
        sharedpreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty()){
                    Toast.makeText(getApplicationContext(), "All fields are required",Toast.LENGTH_LONG).show();
                }else{
                    login();
                }
            }
        } );

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup = new Intent(getApplicationContext(), SignUp1.class);
                startActivity(signup);
            }
        } );

    }

    public void login(){
        Call<ResponseBody> call = recycleItApi.login(email.getText().toString().trim(), password.getText().toString().trim());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String data = response.body().string();
                    if(data.equals("\"{'result': 'no'}\"")){
                        Toast.makeText(getApplicationContext(), "wrong email or password",Toast.LENGTH_LONG).show();
                    }else{
                        JSONArray jsonArray = new JSONArray(data);
                        JSONObject object = jsonArray.getJSONObject(0);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("username", object.getString("username"));
                        editor.putString("email", object.getString("email"));
                        editor.putString("password", object.getString("password"));
                        editor.putString("phone", object.getString("phone"));
                        editor.putString("address", object.getString("address"));
                        editor.putString("country", object.getString("country"));
                        editor.putString("city", object.getString("city"));

                        editor.commit();

                        Toast.makeText(getApplicationContext(), "Logged in",Toast.LENGTH_LONG).show();
                        Intent profile = new Intent(getApplicationContext(), Profile.class);
                        startActivity(profile);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to Login",Toast.LENGTH_LONG).show();
            }
        });
    }
}
