package com.schedulesoccer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

         TextView login = findViewById(R.id.login);
         Button regis = findViewById(R.id.register);
        final EditText name = findViewById(R.id.username);
        final EditText pass = findViewById(R.id.password);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Register.this, Login.class);
                startActivity(in);
            }
        });
        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidNetworking.post("http://192.168.6.254/schedule/register.php")
                        .addBodyParameter("username", name.getText().toString() )
                        .addBodyParameter("password", pass.getText().toString() )
                        .setTag("test")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                String username = name.getText().toString();
                                String password = pass.getText().toString();


                                if ((username.isEmpty()) || (password.isEmpty())) {
                                    Toast.makeText(Register.this, "semuanya harus di isi", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(Register.this, "success", Toast.LENGTH_SHORT).show();
                                    Intent in = new Intent(Register.this, Login.class);
                                    startActivity(in);
                                }
                            }
                            @Override
                            public void onError(ANError error) {
                                Toast.makeText(Register.this, "gagal", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    }

