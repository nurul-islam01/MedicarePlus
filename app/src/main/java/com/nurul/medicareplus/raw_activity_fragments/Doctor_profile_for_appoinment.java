package com.nurul.medicareplus.raw_activity_fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nurul.medicareplus.MedicarePlus;
import com.nurul.medicareplus.R;
import com.nurul.medicareplus.REG_MODEL.DoctorSignupProfile;
import com.squareup.picasso.Picasso;


public class Doctor_profile_for_appoinment extends Fragment {

    private Context context;
    private ImageView userphoto;
    private TextView userName, userdegination, doctorEmailTV, doctorPhone, doctorchember;
    private Button appoinmentBT;



    private String doctorId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_doctor_profile_for_appoinment, container, false);

        userphoto = v.findViewById(R.id.userphoto);
        userName = v.findViewById(R.id.userName);
        userdegination = v.findViewById(R.id.userdegination);
        doctorEmailTV = v.findViewById(R.id.doctorEmailTV);
        doctorPhone = v.findViewById(R.id.doctorPhone);
        doctorchember = v.findViewById(R.id.doctorchember);
        appoinmentBT = v.findViewById(R.id.appoinmentBT);

        MedicarePlus.getUserIDdatabaseReference(doctorId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    DoctorSignupProfile profile = dataSnapshot.getValue(DoctorSignupProfile.class);
                    userName.setText(profile.getName());
                    userdegination.setText(profile.getDegination()+"\n"+profile.getSepecility());
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

        appoinmentBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Send Request For Appoinment");
                alertDialog.setPositiveButton("Send",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MedicarePlus.appoinmentRequestSend(doctorId, MedicarePlus.getUserId()).setValue(MedicarePlus.getUserId())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(getActivity(), "Request Send", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "Request Cancel", Toast.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });

        return v;
    }


    @Override
    public void onAttach(Context context_) {
        super.onAttach(context_);
       context = context_ = getActivity();

       Bundle bundle = getArguments();
       doctorId = bundle.getString("doctorId");

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
