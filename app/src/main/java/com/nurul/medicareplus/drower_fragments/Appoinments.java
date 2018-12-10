package com.nurul.medicareplus.drower_fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nurul.medicareplus.MedicarePlus;
import com.nurul.medicareplus.R;
import com.nurul.medicareplus.adapter.AppoinmentsAdapter;

import java.util.ArrayList;


public class Appoinments extends Fragment {

    private static final String TAG = Appoinments.class.getSimpleName();

    private Context context;
    private ArrayList<String> appoinmentList = new ArrayList<String>();

    private CardView noAppoinmentCV;
    private RecyclerView appoinmentsRV;
    private RecyclerView.Adapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_appoinments, container, false);

        appoinmentsRV = v.findViewById(R.id.appoinmentsRV);
        noAppoinmentCV = v.findViewById(R.id.noAppoinmentCV);

        MedicarePlus.appoinmentsofDoctors().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    appoinmentList = new ArrayList<String>();
                    for (DataSnapshot d : dataSnapshot.getChildren()){
                        String apointRow = d.getKey();
                        appoinmentList.add(apointRow);
                    }
                    adapter = new AppoinmentsAdapter(getActivity(), appoinmentList);
                    appoinmentsRV.setAdapter(adapter);
                    adapter.notifyDataSetChanged();



                    if (appoinmentList.size() < 1){
                        noAppoinmentCV.setVisibility(View.VISIBLE);
                    }else {
                        noAppoinmentCV.setVisibility(View.GONE);
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

}
