package org.tensorflow.lite.examples.detection;

import androidx.appcompat.app.AppCompatActivity;

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

public class SignUp1 extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button button;
    RecycleItApi recycleItApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up1);

        username = findViewById(R.id.editText4);
        password = findViewById(R.id.editText5);
        button = findViewById(R.id.next1);
        recycleItApi = RecycleItApi.retrofit.create(RecycleItApi.class);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty()){
                    Toast.makeText(getApplicationContext(), "All fields are required",Toast.LENGTH_LONG).show();
                }else{
                    verifyUsername();
                }
            }
        } );
    }

    public void verifyUsername(){
        Call<ResponseBody> call = recycleItApi.username(username.getText().toString().trim());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String data = null;
                try {
                    data = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(data.equals("\"{'result': 'ok'}\"")){
                        Toast.makeText(getApplicationContext(), "this username is already taken",Toast.LENGTH_LONG).show();
                    }else if(data.equals("\"{'result': 'no'}\"")){
                        Intent signup2 = new Intent(getApplicationContext(), SignUp2.class);
                        signup2.putExtra("username", username.getText().toString());
                        signup2.putExtra("password", password.getText().toString());
                        startActivity(signup2);
                    }else{
                        Toast.makeText(getApplicationContext(), "a problem occurred, please try again later",Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "A problem occurred, please try again",Toast.LENGTH_LONG).show();
            }
        });
    }
}
