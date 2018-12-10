package com.nurul.medicareplus.drower_fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nurul.medicareplus.MedicarePlus;
import com.nurul.medicareplus.R;
import com.nurul.medicareplus.adapter.MedicineTimerAdapter;

import java.util.ArrayList;

public class MedicineTimer extends Fragment {

     private Context context;
     private RecyclerView medicineRV;
     private CardView noTimerCV;
     private ArrayList<String> medicineTimerList = new ArrayList<String>();
     private RecyclerView.Adapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_medicine_timer, container, false);

        medicineRV = v.findViewById(R.id.medicineRV);
        noTimerCV = v.findViewById(R.id.noTimerCV);


        MedicarePlus.getMedicineTimerReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                medicineTimerList = new ArrayList<String>();
                if (dataSnapshot != null){
                    for (DataSnapshot d : dataSnapshot.getChildren()){
                        String row = d.getKey();
                        medicineTimerList.add(row);
                    }
                    adapter = new MedicineTimerAdapter(getActivity(), medicineTimerList);
                    medicineRV.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    if (medicineTimerList.equals(0)){
                        noTimerCV.setVisibility(View.VISIBLE);
                        medicineRV.setVisibility(View.GONE);
                    }else {
                        noTimerCV.setVisibility(View.GONE);
                        medicineRV.setVisibility(View.VISIBLE);
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
        context = context_;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    
}
