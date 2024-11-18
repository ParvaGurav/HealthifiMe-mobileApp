package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Locale;

public class DistanceFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private static final int Threshold =17;
    private int stepCount = 0;
    private TextView stepTextView;
    private TextView distanceView;
    private TextView minutesTextView;
    private TextView secondsTextView;
    private Button startTrack;
    private Button stopTrack;
    private boolean isRunning = false;
    private long startTime = 0;
    private long elapsedTime = 0;

    public DistanceFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_distance, container, false);

        stepTextView = view.findViewById(R.id.stepDisplay);
        distanceView = view.findViewById(R.id.distanceDisplay);

        minutesTextView = view.findViewById(R.id.minutesTextView);
        secondsTextView = view.findViewById(R.id.secondsTextView);

        startTrack = view.findViewById(R.id.startTrack);
        stopTrack = view.findViewById(R.id.stopTrack);


        startTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStopwatch();
                startStepTracking();
            }
        });

        stopTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopStepTracking();
                pauseStopwatch();
            }
         });

        return view;
    }

    private void startStepTracking() {
        stepCount = 0;
        updateCount();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void stopStepTracking() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float xDirection, yDirection, zDirection;
        xDirection = event.values[0];
        yDirection = event.values[1];
        zDirection = event.values[2];

        double magnitude = Math.sqrt((xDirection * xDirection) + (yDirection * yDirection) + (zDirection * zDirection));
        if (magnitude > Threshold) {

            stepCount = stepCount + 1;

            updateCount();
            double distanceInKMeters = (stepCount * 0.7)/1000; // Example: Each step covers 0.7 meters

            updateDistance(distanceInKMeters);
        }
    }

    private void updateCount() {
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                stepTextView.setText("Step Count:" + stepCount);
            }
        });
    }

    private void updateDistance(double distanceInMeters) {

        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                distanceView.setText(String.format(Locale.getDefault(), "Distance: %.2f KiloMeters", distanceInMeters));
            }
        });
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        sensorManager.unregisterListener(this);
    }

    private void pauseStopwatch() {
        if (isRunning) {
            isRunning = false;
            elapsedTime = System.currentTimeMillis() - startTime;
        }
    }

    private void startStopwatch() {
        if (!isRunning) {
            isRunning = true;
            startTime = System.currentTimeMillis() - elapsedTime;


            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateTimeCounter();
                }
            });
        }
    }

    private void updateTimeCounter() {
        if (isRunning) {
            long currentTime = System.currentTimeMillis() - startTime;
            long totalSeconds = currentTime / 1000;
            long minutes = totalSeconds / 60;
            long seconds = totalSeconds % 60;

            minutesTextView.setText(String.format(Locale.getDefault(), "%02d", minutes));
            secondsTextView.setText(String.format(Locale.getDefault(), "%02d", seconds));


            minutesTextView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateTimeCounter();
                }
            }, 1000);
        }
    }
}
