package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserInfoFragment extends Fragment {

    private TextView userIdTextView, nameTextView, ageTextView, emailTextView,
            passwordTextView, userNameTextView, heightTextView, weightTextView;

    public UserInfoFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_info, container, false);


        userIdTextView = view.findViewById(R.id.userIdTextView);
        nameTextView = view.findViewById(R.id.nameTextView);
        ageTextView = view.findViewById(R.id.ageTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        passwordTextView = view.findViewById(R.id.passwordTextView);
        userNameTextView = view.findViewById(R.id.userNameTextView);
        heightTextView = view.findViewById(R.id.heightTextView);
        weightTextView = view.findViewById(R.id.weightTextView);


        getSampleUserData();

        return view;
    }

    private void setUserData(UserResponse userData) {
        userIdTextView.setText("ID: " + userData.getId());
        nameTextView.setText("Name: " + userData.getName());
        ageTextView.setText("Age: " + userData.getAge());
        emailTextView.setText("Email: " + userData.getEmail());
        passwordTextView.setText("Password: " + userData.getPassword());
        userNameTextView.setText("User Name: " + userData.getUser_name());
        heightTextView.setText("Height: " + userData.getHeight());
        weightTextView.setText("Weight: " + userData.getWeight());
    }

    private void getSampleUserData() {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://10.12.154.44:8080/") // Replace with your base URL
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
        Retrofit retrofit = RetrofitClient.getInstance();

        ApiService apiService = retrofit.create(ApiService.class);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String user_id = sharedPreferences.getString("userId", "");

        Call<UserResponse> userInfoData = apiService.userInfo(user_id);
        userInfoData.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();

                    setUserData(userResponse);
                } else {
                    System.out.println("Error in User Info Page");

                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

            }
        });
    }
}
