package org.tensorflow.lite.examples.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp3 extends AppCompatActivity {
    EditText code;
    Button button;
    Button button1;
    String username;
    String password;
    String phone;
    String email;
    String generatedCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up3);

        code = findViewById(R.id.editText8);
        button = findViewById(R.id.next3);
        button1 = findViewById(R.id.resend);

        Intent intent = getIntent();
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");
        phone = getIntent().getStringExtra("phone");
        email = getIntent().getStringExtra("email");

        sendSMS();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(code.getText().toString().trim().equals(generatedCode)){
                    Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
                    Intent signup4 = new Intent(getApplicationContext(), SignUp4.class);
                    signup4.putExtra("username", username);
                    signup4.putExtra("password", password);
                    signup4.putExtra("email", email);
                    signup4.putExtra("phone", phone);
                    startActivity(signup4);
                }else{
                    Toast.makeText(getApplicationContext(), "Incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        } );

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS();
            }
        } );
    }

    public void sendSMS(){
        int randomNumber = (int)(Math.random()*((9999-1000)+1))+1000;
        generatedCode = Integer.toString(randomNumber);
        try{
            SmsManager smgr = SmsManager.getDefault();
            smgr.sendTextMessage(phone,null,generatedCode,null,null);
            Toast.makeText(getApplicationContext(), "Code Sent", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
        }
    }
}
