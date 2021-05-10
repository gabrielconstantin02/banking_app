package com.example.banking_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ProfileFragment extends Fragment {

    Button btnEditProfile;
    Button btnSettings;
    Button btnAbout;
    String user_email;
    View view;
    TextView txtAccountName;

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


