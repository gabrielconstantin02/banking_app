package com.example.banking_app.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.banking_app.R;
import com.example.banking_app.activity.AddPaymentActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PaymentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentsFragment extends Fragment {
    View view;


    public PaymentsFragment() {
        // Required empty public constructor
    }


    public static PaymentsFragment newInstance(String param1, String param2) {
        PaymentsFragment fragment = new PaymentsFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_payments, container, false);
        FragmentActivity activity = getActivity();
        activity.setTitle(R.string.nav_payments);
        TextView textView=view.findViewById(R.id.section_label);
        return view;

    }
    public void onAddPay(View view) {                                ///Signup button
        Intent intent = new Intent(getActivity(), AddPaymentActivity.class);
        startActivity(intent);
    }
}