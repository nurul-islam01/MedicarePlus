package com.nurul.medicareplus.drower_fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nurul.medicareplus.MedicarePlus;
import com.nurul.medicareplus.R;
import com.nurul.medicareplus.adapter.AppoinmentRequestAdapter;

import java.util.ArrayList;


public class AppoinmentsRequest extends Fragment {

    private static final String TAG = AppoinmentsRequest.class.getSimpleName();

    private Context context;

    private RecyclerView allRequestRV;

    private ArrayList<String> appoinmenRequestList = new ArrayList<String>();
    private RecyclerView.Adapter adapter;
    private TextView noAppoinmentTV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_appoinments_request, container, false);

        Snackbar.make(((Activity)context).findViewById(android.R.id.content), "Welcome Sir", Snackbar.LENGTH_LONG);

        allRequestRV = v.findViewById(R.id.allRequestRV);
        noAppoinmentTV = v.findViewById(R.id.noAppoinmentTV);

        MedicarePlus.getAppoinmentReq().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    appoinmenRequestList = new ArrayList<String>();
                    try {
                        for (DataSnapshot d: dataSnapshot.getChildren()){
                            String patinetID = d.getValue(String.class);
                            appoinmenRequestList.add(patinetID);
                        }
                        adapter = new AppoinmentRequestAdapter(getActivity(), appoinmenRequestList);
                        allRequestRV.setAdapter(adapter);
                        if (appoinmenRequestList.size() == 0){
                            noAppoinmentTV.setVisibility(View.VISIBLE);
                        }else {
                            noAppoinmentTV.setVisibility(View.GONE);
                        }
                    }catch (Exception e){
                        Log.i(TAG, "onDataChange: "+e.getMessage());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return v;
    }


    @Override
    public void onAttach(Context context_) {
        super.onAttach(context_);
        context = context_ = getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
