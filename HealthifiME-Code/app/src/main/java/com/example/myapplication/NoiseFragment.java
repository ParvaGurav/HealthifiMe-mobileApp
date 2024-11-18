package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NoiseFragment extends Fragment {

    private static final int RECORD_AUDIO_PERMISSION_REQUEST = 123;
    private Handler handler;
    private AudioRecord audioData;
    private boolean isRecording = false;
    private boolean hit80 = false;

    private double decibel = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_noise, container, false);

        handler = new Handler(Looper.getMainLooper());

        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{android.Manifest.permission.RECORD_AUDIO},
                    RECORD_AUDIO_PERMISSION_REQUEST);
        } else {
            beginAudioRecord();
        }

        Button startCheckButton = view.findViewById(R.id.startCheckButton);
        startCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMicRecording();
            }
        });

        Button stopCheckButton = view.findViewById(R.id.stopCheckButton);
        stopCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopMicRecording();
            }
        });

        return view;
    }

    @SuppressLint("MissingPermission")
    private void beginAudioRecord() {
        int bufferSize = AudioRecord.getMinBufferSize(
                44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);

        audioData = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                44100, AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize);
    }

    private void startMicRecording() {
        if (audioData == null || audioData.getState() != AudioRecord.STATE_INITIALIZED) {
            Toast.makeText(requireContext(), "Microphone permission not granted or initialization failed", Toast.LENGTH_SHORT).show();
            return;
        }

        audioData.startRecording();
        isRecording = true;


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRecording) {
                    analyzeAudioData();
                }
            }
        }).start();
    }

    private void stopMicRecording() {

        if (audioData != null) {
            isRecording = false;
            audioData.stop();
        }

        System.out.println(decibel);
    }

    private void analyzeAudioData() {
        short[] sample = new short[1024];
        int bytesRead = audioData.read(sample, 0, sample.length);
        if (bytesRead > 0) {
            double decibel = calculateDecibel(sample);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    updateNoiseLevelUI(decibel);
                }
            });
        }
    }

    private double calculateDecibel(short[] samples) {
        double sum = 0;
        for (short value : samples) {
            sum += Math.abs(value);
        }
        double amplitude = sum / samples.length;
        decibel = 20  * Math.log10(amplitude);
        if (decibel>80){
            stopMicRecording();
        }
        return decibel;
    }
    private void displayWarning(){
        TextView warningText = requireView().findViewById(R.id.warningText);
        warningText.setText("Environment too noisy");
    }

    private void updateNoiseLevelUI(double decibel) {
        TextView noiseLevelTextView = requireView().findViewById(R.id.NoiseLevel);
        noiseLevelTextView.setText(String.format(" %.2f", decibel));

        if (decibel > 80 && !hit80) {

            displayWarning();

            hit80 = true;
        } else if (decibel <= 80) {
            hit80 = false;
            // Clear the warning
            TextView warningText = requireView().findViewById(R.id.warningText);
            warningText.setText("");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RECORD_AUDIO_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                beginAudioRecord();
            } else {
                Toast.makeText(requireContext(), "Microphone permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (audioData != null) {
            audioData.release();
            audioData = null;
        }
    }
}
