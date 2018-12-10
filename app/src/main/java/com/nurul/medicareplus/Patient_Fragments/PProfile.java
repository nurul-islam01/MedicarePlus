package com.nurul.medicareplus.Patient_Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import com.nurul.medicareplus.REG_MODEL.PatientSignupProfile;
import com.nurul.medicareplus.activity.Patient_details;
import com.nurul.medicareplus.pojos.AppoinmentedProfile;

public class PProfile extends Fragment {

    private static final String TAG = PProfile.class.getSimpleName();

    private Context context;

    private TextView locationTV, nameTV, ageTV, weightTV, phoneTV, serialNoTV, timeTV, dateTV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pprofile, container, false);

        timeTV = v.findViewById(R.id.timeTV);
        serialNoTV = v.findViewById(R.id.serialNoTV);
        phoneTV = v.findViewById(R.id.phoneTV);
        weightTV = v.findViewById(R.id.weightTV);
        ageTV = v.findViewById(R.id.ageTV);
        nameTV = v.findViewById(R.id.nameTV);
        dateTV = v.findViewById(R.id.dateTV);
        locationTV = v.findViewById(R.id.locationTV);

        MedicarePlus.appoinmentedStore().child(Patient_details.getAppoinmentId()).child("AppoinmetDes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    AppoinmentedProfile profile = dataSnapshot.getValue(AppoinmentedProfile.class);
                    try {
                        timeTV.setText(profile.getTime());
                    }catch (Exception e){
                        Log.d(TAG, "onDataChange: "+e.getMessage());
                    }
                    try {
                        dateTV.setText(profile.getDate());
                    }catch (Exception e){
                        Log.d(TAG, "onDataChange: "+e.getMessage());
                    }
                    try {
                        serialNoTV.setText(profile.getAppoinmentSerial());
                    }catch (Exception e){
                        Log.d(TAG, "onDataChange: "+e.getMessage());
                    }

                    try {
                        serialNoTV.setText(profile.getAppoinmentSerial());
                    }catch (Exception e){
                        Log.d(TAG, "onDataChange: "+e.getMessage());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        MedicarePlus.getUserIDdatabaseReference(Patient_details.getAppoinmentId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot != null){
                    PatientSignupProfile patientSignupProfile = dataSnapshot.getValue(PatientSignupProfile.class);

                    try {
                        nameTV.setText(patientSignupProfile.getName());
                    }catch (Exception e){
                        Log.d(TAG, "onDataChange: "+e.getMessage());
                    }
                    try {
                        ageTV.setText(patientSignupProfile.getAge());
                    }catch (Exception e){
                        Log.d(TAG, "onDataChange: "+e.getMessage());
                    }
                    try {
                        weightTV.setText(patientSignupProfile.getWeight());
                    }catch (Exception e){
                        Log.d(TAG, "onDataChange: "+e.getMessage());
                    }
                    try {
                        phoneTV.setText(patientSignupProfile.getPhone());
                    }catch (Exception e){
                        Log.d(TAG, "onDataChange: "+e.getMessage());
                    }
                    try {
                        locationTV.setText(patientSignupProfile.getAddress());
                    }catch (Exception e){
                        Log.d(TAG, "onDataChange: "+e.getMessage());
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
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context = getActivity();
    }

}
