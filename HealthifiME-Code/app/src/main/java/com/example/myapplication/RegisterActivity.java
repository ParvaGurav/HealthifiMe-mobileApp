package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private EditText name, age, email, password, userName, height, weight;
    private Button register;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);


        Retrofit retrofit = RetrofitClient.getInstance();


        apiService = retrofit.create(ApiService.class);


        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        userName = findViewById(R.id.userName);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        register = findViewById(R.id.registerButton);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (name.getText().toString().isEmpty() || age.getText().toString().isEmpty() ||
                        email.getText().toString().isEmpty() || password.getText().toString().isEmpty() ||
                        userName.getText().toString().isEmpty() || height.getText().toString().isEmpty() ||
                        weight.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please enter all values", Toast.LENGTH_SHORT).show();
                    return;
                }

                registerUser(
                        name.getText().toString(),
                        Integer.parseInt(age.getText().toString()),
                        email.getText().toString(),
                        password.getText().toString(),
                        userName.getText().toString(),
                        Integer.parseInt(height.getText().toString()),
                        Integer.parseInt(weight.getText().toString())
                );
            }
        });
    }

    private void registerUser(String name, int age, String email, String password,
                              String userName, int height, int weight) {

        UserResponse request = new UserResponse();
        request.setName(name);
        request.setAge(age);
        request.setEmail(email);
        request.setPassword(password);
        request.setUser_name(userName);
        request.setHeight(height);
        request.setWeight(weight);


        Call<UserResponse> call = apiService.registerUser(request);


        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);

                    Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                } else {

                    Log.e("RegistrationError", "Registration failed. Response code: " + response.code());
                    Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

                Log.e("RegistrationError", "Error: " + t.getMessage());
//                Toast.makeText(RegisterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}