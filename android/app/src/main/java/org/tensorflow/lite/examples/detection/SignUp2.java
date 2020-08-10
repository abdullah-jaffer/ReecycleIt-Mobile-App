package org.tensorflow.lite.examples.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp2 extends AppCompatActivity {
    private EditText email;
    private EditText phone;
    private Button button;
    RecycleItApi recycleItApi;
    String username;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        email = findViewById(R.id.editText6);
        phone = findViewById(R.id.editText7);
        button = findViewById(R.id.next2);
        recycleItApi = RecycleItApi.retrofit.create(RecycleItApi.class);
        Intent intent = getIntent();
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().trim().isEmpty() || phone.getText().toString().trim().isEmpty()){
                    Toast.makeText(getApplicationContext(), "All fields are required",Toast.LENGTH_LONG).show();
                }else if(!isNumeric(phone.getText().toString())){
                    Toast.makeText(getApplicationContext(), "phone number must be numeric",Toast.LENGTH_LONG).show();
                }else if(phone.getText().toString().length() < 11 || phone.getText().toString().length() > 11){
                    Toast.makeText(getApplicationContext(), "phone number not valid",Toast.LENGTH_LONG).show();
                }else if(!isValidEmail(email.getText().toString())){
                    Toast.makeText(getApplicationContext(), "not a valid email",Toast.LENGTH_LONG).show();
                }else{
                    verifyEmail();
                }
            }
        } );

    }

    public void verifyEmail(){
        Call<ResponseBody> call = recycleItApi.email(email.getText().toString().trim());
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
                    Toast.makeText(getApplicationContext(), "this email is already in use",Toast.LENGTH_LONG).show();

                }else if(data.equals("\"{'result': 'no'}\"")){
                    verifyPhone();

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

    public void verifyPhone(){
        Call<ResponseBody> call = recycleItApi.phone(phone.getText().toString().trim());
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
                    Toast.makeText(getApplicationContext(), "this number is already in use",Toast.LENGTH_LONG).show();

                }else if(data.equals("\"{'result': 'no'}\"")){
                    Intent signup3 = new Intent(getApplicationContext(), SignUp3.class);
                    signup3.putExtra("username", username);
                    signup3.putExtra("password", password);
                    signup3.putExtra("email", email.getText().toString());
                    signup3.putExtra("phone", phone.getText().toString());
                    startActivity(signup3);
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

    public static boolean isValidEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static boolean isNumeric(String str)
    {
        for (char c : str.toCharArray())
        {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }
}
