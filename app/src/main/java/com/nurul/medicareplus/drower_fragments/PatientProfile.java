package com.nurul.medicareplus.drower_fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
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


public class PatientProfile extends Fragment {

    private static final String TAG = PagerAdapter.class.getSimpleName();

    private Context context;

    private TextView nameTV, ageTV, weightTV, phoneTV, emailTV, genderTV;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_patient_profile, container, false);

        nameTV = v.findViewById(R.id.nameTV);
        ageTV = v.findViewById(R.id.ageTV);
        weightTV = v.findViewById(R.id.weightTV);
        phoneTV = v.findViewById(R.id.phoneTV);
        emailTV = v.findViewById(R.id.emailTV);
        genderTV = v.findViewById(R.id.genderTV);

        MedicarePlus.getUserProfileDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    PatientSignupProfile profile = dataSnapshot.getValue(PatientSignupProfile.class);

                    nameTV.setText(profile.getName());
                    ageTV.setText("Age : "+profile.getAge() + " Years");
                    weightTV.setText("Weight : "+profile.getWeight() +" KG");
                    phoneTV.setText("Phone : "+profile.getPhone());
                    genderTV.setText("Gender : "+profile.getGender());
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
        context_ = getActivity();
        context = context_;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
