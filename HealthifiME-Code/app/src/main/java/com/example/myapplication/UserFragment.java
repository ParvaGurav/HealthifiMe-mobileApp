package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class UserFragment extends Fragment {

    private ListView userListView;

    public UserFragment() {

    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user, container, false);

        userListView = view.findViewById(R.id.userListView);


        ArrayList<String> userItems = new ArrayList<>();
        userItems.add("User Info");
        userItems.add("User Weight");
        userItems.add("User BMI Index");
        userItems.add("Recent Result");
        userItems.add("LogOut");


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                userItems
        );


        userListView.setAdapter(arrayAdapter);


        userListView.setOnItemClickListener((parent, view1, position, id) -> {

            String selectedItem = userItems.get(position);
            handleItemClick(selectedItem);
        });

        return view;
    }

    private void handleItemClick(String selectedItem) {

        switch (selectedItem) {
            case "User Info":
                navigateToUserInfoFragment();
                break;
            case "User Weight":
                navigateToUserWeightFragment();
                break;
            case "User BMI Index":
                navigateToUserBMIIndex();
                break;
            case "Recent Result":
                navigateToRecentResult();
                break;
            case "LogOut":
                appLogOut();
                break;

        }
    }

    private void appLogOut(){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    private void navigateToUserInfoFragment() {

        UserInfoFragment userInfoFragment = new UserInfoFragment();
        replaceFragment(userInfoFragment);
    }

    private void navigateToUserWeightFragment() {

        UserWeightFragment userWeightFragment = new UserWeightFragment();
        replaceFragment(userWeightFragment);
    }

    private void navigateToUserBMIIndex(){
        UserBMIIndexFragment userBMIIndexFragment = new UserBMIIndexFragment();
        replaceFragment(userBMIIndexFragment);
    }

    private void navigateToRecentResult(){
        RecentResultFragment recentResultFragment = new RecentResultFragment();
        replaceFragment(recentResultFragment);
    }
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
