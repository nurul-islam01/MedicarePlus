package com.nurul.medicareplus.SingleAppoinment;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nurul.medicareplus.MedicarePlus;
import com.nurul.medicareplus.R;
import com.nurul.medicareplus.REG_MODEL.DoctorSignupProfile;
import com.nurul.medicareplus.activity.SingleAppoinment;
import com.nurul.medicareplus.pojos.AppoinmentedProfile;
import com.squareup.picasso.Picasso;


public class ApDoctorProfile extends Fragment implements SingleAppoinment.AppoinmenttDoctor {

    private static final String TAG = ApDoctorProfile.class.getSimpleName();
    private Context context;

    private ImageView userphotoIV;
    private TextView userNameTV, userdeginationTV, specilityTV, emailTV, phoneTV, doctorchemberTV, serialandTimeTV, dateTV;

    private String appoinmentId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ap_doctor_profile, container, false);

        dateTV = view.findViewById(R.id.dateTV);
        userphotoIV = view.findViewById(R.id.userphotoIV);
        userNameTV = view.findViewById(R.id.userNameTV);
        userdeginationTV = view.findViewById(R.id.userdeginationTV);
        specilityTV = view.findViewById(R.id.specilityTV);
        emailTV = view.findViewById(R.id.emailTV);
        phoneTV = view.findViewById(R.id.phoneTV);
        doctorchemberTV = view.findViewById(R.id.doctorchemberTV);
        serialandTimeTV = view.findViewById(R.id.serialandTimeTV);


        MedicarePlus.patientAppointmentDesc(appoinmentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    try {
                        AppoinmentedProfile profile = dataSnapshot.getValue(AppoinmentedProfile.class);

                        dateTV.setText(profile.getDate());
                        serialandTimeTV.setText("Serial : "+profile.getAppoinmentSerial()+"  |  Time : "+profile.getTime());

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        MedicarePlus.getUserIDdatabaseReference(appoinmentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    try {
                        DoctorSignupProfile doctorProfile = dataSnapshot.getValue(DoctorSignupProfile.class);

                        Picasso.get().load(doctorProfile.getPic_uri()).placeholder(R.drawable.doctor_image_icon)
                                .error(R.drawable.doctor_image_icon).into(userphotoIV);

                        userNameTV.setText(doctorProfile.getName());
                        userdeginationTV.setText(doctorProfile.getDegination());
                        specilityTV.setText(doctorProfile.getSepecility());
                        phoneTV.setText(doctorProfile.getPhone());
                        doctorchemberTV.setText(doctorProfile.getChember());

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context = getActivity();
        ((SingleAppoinment) context).setAppoinmenttDoctor(this);
    }

    @Override
    public void appoinment(String appoinmentId) {
        this.appoinmentId = appoinmentId;
    }
}
