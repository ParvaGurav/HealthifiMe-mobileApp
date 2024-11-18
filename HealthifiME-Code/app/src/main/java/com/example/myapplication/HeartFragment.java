package com.example.myapplication;

import android.content.Intent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class HeartFragment extends Fragment {

    public HeartFragment() {

    }

    public static HeartFragment newInstance() {
        return new HeartFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_heart, container, false);


        View openGoogleFitButton = view.findViewById(R.id.GoogleFitButton);
        openGoogleFitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGoogleFit();
            }
        });

        return view;
    }

    private void openGoogleFit() {
        Intent launchIntent = requireActivity().getPackageManager().getLaunchIntentForPackage("com.google.android.apps.fitness");
        if (launchIntent != null) {
            startActivity(launchIntent);
        } else {
            Toast.makeText(requireContext(), "Google Fit app not installed", Toast.LENGTH_LONG).show();
        }
    }
}
