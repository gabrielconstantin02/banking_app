package com.example.banking_app.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.banking_app.MApplication;
import com.example.banking_app.R;
import com.example.banking_app.activity.ChangePwdActivity;
import com.example.banking_app.activity.EditProfileActivity;
import com.example.banking_app.models.User;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {
    private static final int RESULT_OK = -1;
    Button btnEditProfile;
    Button btnSettings;
    Button btnAbout;
    String user_email;
    String user_name;
    View view;
    User user;
    private TextView txtAccountEmail;
    private TextView txtAccountName;
    SharedPreferences mPrefs;
    SharedPreferences.Editor editor;

    public ProfileFragment() {
        // Required empty public constructor
    }

    int SELECT_PICTURE = 200;
    int PROFILE_CODE = 2;


    void imageChooser() {

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            i.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            i.setAction(Intent.ACTION_OPEN_DOCUMENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
        }
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    getContext().getContentResolver().takePersistableUriPermission(selectedImageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                SharedPreferences mPrefs = getActivity().getSharedPreferences("my_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = mPrefs.edit();
                if (null != selectedImageUri) {
                    CircleImageView profileImg = (CircleImageView) view.findViewById(R.id.profileImage);
                    Picasso.get().load(selectedImageUri).into(profileImg);
                    editor.putString("imageUri", String.valueOf(selectedImageUri));
                    editor.apply();
                    Log.d("UriTag", String.valueOf(selectedImageUri));
                }
            } else if (requestCode == PROFILE_CODE) {
                FragmentTransaction ft = requireFragmentManager().beginTransaction();
                if (Build.VERSION.SDK_INT >= 26) {
                    ft.setReorderingAllowed(false);
                }
                ft.detach(this).attach(this).commit();
            }
        }
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
        user = MApplication.currentUser;
        btnSettings=view.findViewById(R.id.settingsButton);
        btnAbout=view.findViewById(R.id.aboutButton);
        txtAccountEmail=view.findViewById(R.id.email);
        txtAccountName=view.findViewById(R.id.name);
        user_email=getActivity().getIntent().getStringExtra("extra_mail");
        user_name = user.getFirstName() + " " + user.getLastName();
        setvalues();
        SharedPreferences.Editor editor = this.getActivity().getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit();
        editor.putString("email", user_email);
        editor.apply();
        FragmentActivity activity = getActivity();
        activity.setTitle(R.string.nav_profile);
        btnEditProfile.setOnClickListener(this::onEditProfile);
        
        CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.profileImage);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

        SharedPreferences mPrefs = getActivity().getSharedPreferences("my_prefs", MODE_PRIVATE);
        String imageUri = mPrefs.getString("imageUri",null);
        if (imageUri != null) {
            Log.d("UriTag2", imageUri);
            Uri selectedImageUri = Uri.parse(imageUri);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getContext().getContentResolver().takePersistableUriPermission(selectedImageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            Picasso.get().load(selectedImageUri).into(circleImageView);

        }
        return view;
    }

    private void setvalues(){
        txtAccountEmail.setText(user_email);
        txtAccountName.setText(user_name);
    }
    public void onEditProfile(View view) {                                ///EditProfilebutton
        Intent intent1 = new Intent(this.getActivity(), EditProfileActivity.class);
        intent1.putExtra("extra_mail", user_email);
        startActivityForResult(intent1, PROFILE_CODE);
    }


    public void onChange(View view) {
        Intent intent = new Intent(this.getActivity(), ChangePwdActivity.class);
        intent.putExtra("extra_mail", user_email);
        startActivity(intent);
    }
}


