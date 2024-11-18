package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragments(new NoiseFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.noise) {
                replaceFragments(new NoiseFragment());
            } else if (itemId == R.id.distance) {
                replaceFragments(new DistanceFragment());
            } else if (itemId == R.id.heart) {
                replaceFragments(new HeartFragment());
            } else if (itemId == R.id.users) {
                replaceFragments(new UserFragment());
            } else if (itemId == R.id.GPSDistance) {
                replaceFragments(new GPSFragment());
            }

            return true;
        });
    }

    private void replaceFragments(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
