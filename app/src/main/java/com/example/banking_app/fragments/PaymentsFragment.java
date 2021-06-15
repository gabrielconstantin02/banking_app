package com.example.banking_app.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.banking_app.R;
import com.example.banking_app.activity.AddPaymentActivity;
import com.example.banking_app.activity.EditProfileActivity;
import com.example.banking_app.adapters.RecyclerAdapter;
import com.example.banking_app.config.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PaymentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentsFragment extends Fragment {
    View view;
    Boolean ok=false;
    String userEmail;
    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 60;
    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
    protected LayoutManagerType mCurrentLayoutManagerType;

    protected RadioButton mLinearLayoutRadioButton;
    protected RadioButton mGridLayoutRadioButton;

    protected RecyclerView mRecyclerView;
    protected RecyclerAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    // protected String[] mDataset;
    // protected String[] sDataset;
    ArrayList<String> mDataset = new ArrayList<String>();
    ArrayList<String> sDataset = new ArrayList<String>();
    ArrayList<String> fDataset = new ArrayList<String>();


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
        view.setTag(TAG);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
        //getting current user email

        userEmail=getActivity().getIntent().getStringExtra("extra_mail");

        //function for data load

       setDataPays();


        mAdapter = new RecyclerAdapter(mDataset,fDataset,sDataset);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // END_INCLUDE(initializeRecyclerView)


        return view;

    }
    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    public class getDataSQL implements Runnable{
        public void run () {

            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Log.d("ClassTag", "Failed1");
            }

            try {
                Connection con = DatabaseConnection.getConnection();
                Statement stmt = con.createStatement();

                ResultSet rs = stmt.executeQuery("select * from USER where email=\""+userEmail+"\"");
                try {
                    rs.next();
                    ResultSet rsDataPays=stmt.executeQuery("select * from TRANSACTION where sender_id in (select iban from ACCOUNT where user_id=\""+rs.getString("user_id")+"\")");
                    while(rsDataPays.next())
                    {
                        fDataset.add(rsDataPays.getString("sender_id"));
                        sDataset.add(rsDataPays.getString("receiver_id"));
                        mDataset.add(rsDataPays.getString("amount"));
                    }
                    ResultSet rsDataPaysRec=stmt.executeQuery("select * from TRANSACTION where receiver_id in (select iban from ACCOUNT where user_id=\""+rs.getString("user_id")+"\")");
                    while(rsDataPaysRec.next())
                    {
                        fDataset.add(rsDataPaysRec.getString("sender_id"));
                        sDataset.add(rsDataPaysRec.getString("receiver_id"));
                        mDataset.add(rsDataPaysRec.getString("amount"));
                    }



                    ok=true;

                }
                catch (Exception ignored){
                    ok=true;

                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                ok=true;
                Log.d("SQLTag", "Failed to execute");
            }
        }
    }
    public void setDataPays(){

        Thread sqlThread = new Thread(new getDataSQL());
        sqlThread.start();
        while (ok!=true){}
        ok=false;
    }
    public void onAddPay(View view) {                                ///Signup button
        Intent intent = new Intent(getActivity(), AddPaymentActivity.class);
        startActivity(intent);
    }
}
//ceva test