package com.nurul.medicareplus.drower_fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nurul.medicareplus.MedicarePlus;
import com.nurul.medicareplus.R;
import com.nurul.medicareplus.adapter.AllDoctorAdapter;

import java.util.ArrayList;

public class AllDoctors extends Fragment {

    private Context context;

    private RecyclerView alldoctor;
    private RecyclerView.Adapter adapter;
    private ArrayList<String> doctorList = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View v = inflater.inflate(R.layout.fragment_all_doctiors, container, false);

        Snackbar.make(((Activity) context).findViewById(android.R.id.content), "Welcome", Snackbar.LENGTH_LONG);

        View parentLayout = v.findViewById(android.R.id.content);
//        Snackbar.make(parentLayout, "Welcome...", Snackbar.LENGTH_LONG).setActionTextColor(getResources().getColor(android.R.color.holo_green_light ))
//                .show();

        alldoctor = v.findViewById(R.id.alldoctor);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        alldoctor.setLayoutManager(llm);

        MedicarePlus.getDatabaseReference().child(MedicarePlus.TYPE_DOCTOR).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot != null){
                    for (DataSnapshot childrenShot : dataSnapshot.getChildren()){
                        String doctorID = childrenShot.getValue(String.class);
                        doctorList.add(doctorID);
                    }

                    adapter = new AllDoctorAdapter(doctorList, getActivity());
                    alldoctor.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
                if (dataSnapshot == null){
                    View parentLayout = v.findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "Error...", Snackbar.LENGTH_LONG).setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                            .show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                View parentLayout = v.findViewById(android.R.id.content);
                Snackbar.make(parentLayout, "Error...", Snackbar.LENGTH_LONG).setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                        .show();
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context_) {
        super.onAttach(context_);
        context_ = getActivity();
        context = context_;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
