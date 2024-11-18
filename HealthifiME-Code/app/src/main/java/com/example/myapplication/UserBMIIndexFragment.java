package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import java.text.DecimalFormat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserBMIIndexFragment extends Fragment {

    private TextView bmiResultTextView;
    private TextView bmiSuggestionTextView;
    private TextView userHeightTextView;
    private TextView userWeightTextView;

    public UserBMIIndexFragment() {

    }

    public static UserBMIIndexFragment newInstance() {
        return new UserBMIIndexFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_b_m_i_index, container, false);

        bmiResultTextView = view.findViewById(R.id.BMIResult);
        bmiSuggestionTextView = view.findViewById(R.id.BMISuggestion);
        userHeightTextView = view.findViewById(R.id.yourheight);
        userWeightTextView = view.findViewById(R.id.yourweight);

        Retrofit retrofit = RetrofitClient.getInstance();

        ApiService apiService = retrofit.create(ApiService.class);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String user_id = sharedPreferences.getString("userId", "");

        Call<UserResponse> userInfoData = apiService.userInfo(user_id);

        userInfoData.enqueue(new Callback<UserResponse>() {
            @SuppressLint("StringFormatInvalid")
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    Log.d("API Response", "UserResponse: " + userResponse.toString());

                    int height = userResponse.getHeight();
                    int weight = userResponse.getWeight();
                    System.out.println("Weight: " + userResponse.getWeight());


                    userHeightTextView.setText(getString(R.string.user_height, String.valueOf(height)));
                    userWeightTextView.setText(getString(R.string.user_weight, String.valueOf(weight)));


                    double bmi = calculateBMI(weight, height);
                    System.out.println("BMI: " + bmi);


                    displayBMIResult(bmi);
                } else {

                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

            }
        });

        return view;
    }

    private void displayBMIResult(double bmi) {
        DecimalFormat df = new DecimalFormat("#.##");

        bmiResultTextView.setText(getString(R.string.bmi_result, df.format(bmi)));


        String suggestion = getSuggestionForBMI(bmi);

        bmiSuggestionTextView.setText(getString(R.string.bmi_suggestion, suggestion));
    }

    private double calculateBMI(double weight, double height) {

        return weight / ((height/100) * (height/100));
    }

    private String getSuggestionForBMI(double bmi) {

        if (bmi < 18.5) {
            return getString(R.string.underweight_suggestion);
        } else if (bmi < 25) {
            return getString(R.string.normal_weight_suggestion);
        } else if (bmi < 30) {
            return "Overweight";
        } else {
            return getString(R.string.obese_suggestion);
        }
    }
}
