package com.example.banking_app.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.banking_app.R;
import com.example.banking_app.activity.EditProfileActivity;

public class ProfileFragment extends Fragment {
    Button btnEditProfile;
    Button btnSettings;
    Button btnAbout;
    private String user_email;
    View view;
    private TextView txtAccountName;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_profile, container, false);
        btnEditProfile=view.findViewById(R.id.editProfileButton);
        btnSettings=view.findViewById(R.id.settingsButton);
        btnAbout=view.findViewById(R.id.aboutButton);
        txtAccountName=view.findViewById(R.id.tv_name);
        user_email=getActivity().getIntent().getStringExtra("extra_mail");
        setvalues();

        FragmentActivity activity = getActivity();
        activity.setTitle(R.string.nav_profile);

        return view;
    }

    private void setvalues(){
        txtAccountName.setText(user_email);
    }

    public void onEditProfile(View view) {                                ///Signup button
        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
        intent.putExtra("extra_mail", user_email);
        startActivity(intent);
    }

}


