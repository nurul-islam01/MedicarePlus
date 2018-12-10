package com.nurul.medicareplus.drower_fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nurul.medicareplus.MedicarePlus;
import com.nurul.medicareplus.R;
import com.nurul.medicareplus.REG_MODEL.DoctorSignupProfile;
import com.nurul.medicareplus.REG_MODEL.SignUp;
import com.squareup.picasso.Picasso;

public class DoctorProfile extends Fragment {

    private ImageView userphoto;
    private TextView userName, userdegination, doctorEmailTV, doctorPhone, doctorchember;

    private Context context;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_doctor_profile, container, false);
        userphoto = v.findViewById(R.id.userphoto);
        userName = v.findViewById(R.id.userName);
        userdegination = v.findViewById(R.id.userdegination);
        doctorEmailTV = v.findViewById(R.id.doctorEmailTV);
        doctorPhone = v.findViewById(R.id.doctorPhone);
        doctorchember = v.findViewById(R.id.doctorchember);

        MedicarePlus.getUserProfileDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    DoctorSignupProfile profile = dataSnapshot.getValue(DoctorSignupProfile.class);
                    userName.setText(profile.getName());
                    userdegination.setText(profile.getDegination());
                    doctorchember.setText(profile.getChember());
                    doctorPhone.setText(profile.getPhone());

                    Picasso.get().load(profile.getPic_uri()).placeholder(R.drawable.doctor_image_icon)
                            .error(R.drawable.doctor_image_icon).into(userphoto);
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
