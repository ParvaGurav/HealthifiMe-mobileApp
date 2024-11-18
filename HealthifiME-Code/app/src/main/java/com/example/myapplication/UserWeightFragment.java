package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserWeightFragment extends Fragment {

    private EditText editTextWeight;
    private BarChart barChart;

    public UserWeightFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_weight, container, false);

        editTextWeight = view.findViewById(R.id.editTextNumberDecimal);
        Button buttonSubmit = view.findViewById(R.id.button5);
        barChart = view.findViewById(R.id.barChart);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String weight = editTextWeight.getText().toString();


                saveWeightData(weight);


                Toast.makeText(getActivity(), "Weight saved: " + weight, Toast.LENGTH_SHORT).show();


                editTextWeight.getText().clear();
            }
        });

        displayWeightData();

        return view;
    }

    private void saveWeightData(String weight) {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("WeightData", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        float bmiWeight = Float.parseFloat(weight);
        int weightInt = Math.round(bmiWeight);
        System.out.println("WWW: " + weightInt);

        Retrofit retrofit = RetrofitClient.getInstance();

        ApiService apiService = retrofit.create(ApiService.class);
        SharedPreferences sharedPreferencesweight = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String user_id = sharedPreferencesweight.getString("userId", "");

        Call<UserResponse> userWeightAPI = apiService.userWeight(weightInt, user_id);
        UserResponse userResponse = new UserResponse();
        userWeightAPI.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {

                    UserResponse userResponse = response.body();

                    Toast.makeText(getActivity(), "Weight updated successfully", Toast.LENGTH_SHORT).show();
                } else {

//                    Toast.makeText(getActivity(), "Failed to update weight", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

                System.out.println(t.getMessage());
//                Toast.makeText(getActivity(), "Failed to update weight", Toast.LENGTH_SHORT).show();
            }
        });



        String currentDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        editor.putString(currentDate, weight);
        editor.apply();
        displayWeightData();
        calculateBMI(bmiWeight);
    }

    private void displayWeightData() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("WeightData", getActivity().MODE_PRIVATE);

        for (int i = 0; i < 7; i++) {
            String currentDate = getDateDays(i);
            String weight = sharedPreferences.getString(currentDate, "70"); // Replace "0" with your default weight value

            entries.add(new BarEntry(i, Float.parseFloat(weight)));
            labels.add(currentDate);
        }

        BarDataSet barDataSet = new BarDataSet(entries, "Weight");
        barDataSet.setColor(Color.GREEN);

        BarData data = new BarData(barDataSet);
        barChart.setData(data);
        barChart.getDescription().setText("Weight Chart");


        barChart.getXAxis().setTextColor(Color.RED);
        barChart.getAxisLeft().setTextColor(Color.RED);
        barChart.getAxisRight().setTextColor(Color.RED);

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setLabelRotationAngle(45f);
        barChart.getXAxis().setGranularity(1f);
        barChart.getAxisRight().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.invalidate();
    }

    private String getDateDays(int n) {
        return new SimpleDateFormat("yyyyMMdd", Locale.getDefault())
                .format(new Date(System.currentTimeMillis() - n * 24 * 60 * 60 * 1000));
    }

    private void calculateBMI(float weight) {
        float height = 1.70F;
        float bmi = (weight / (height * height));
    }
}
