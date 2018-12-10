package com.nurul.medicareplus.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nurul.medicareplus.MedicarePlus;
import com.nurul.medicareplus.R;
import com.nurul.medicareplus.REG_MODEL.DoctorSignupProfile;
import com.nurul.medicareplus.activity.SingleAppoinment;
import com.nurul.medicareplus.pojos.AppoinmentedProfile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AppoinmentsAdapter extends RecyclerView.Adapter<AppoinmentsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> appoinmentList = new ArrayList<String>();

    public AppoinmentsAdapter(Context context, ArrayList<String> appoinmentList) {
        this.context = context;
        this.appoinmentList = appoinmentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.appoinments_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder,final int i) {

        MedicarePlus.patientAppointmentDesc(appoinmentList.get(i)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    AppoinmentedProfile profile = dataSnapshot.getValue(AppoinmentedProfile.class);
                    try {

                        viewHolder.serialandTimeTV.setText("Serial : "+profile.getAppoinmentSerial()+" | Time : "+profile.getTime());
                        viewHolder.dateTV.setText("Date : "+profile.getDate());

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        MedicarePlus.getUserIDdatabaseReference(appoinmentList.get(i)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    try {
                        DoctorSignupProfile doctorProfile = dataSnapshot.getValue(DoctorSignupProfile.class);
                        viewHolder.nameTV.setText(doctorProfile.getName());
                        Picasso.get().load(doctorProfile.getPic_uri()).placeholder(R.drawable.doctor_image_icon)
                                .error(R.drawable.doctor_image_icon).into(viewHolder.doctorIV);
                        viewHolder.specilityTV.setText("Specility : "+doctorProfile.getSepecility());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SingleAppoinment.class);
                intent.putExtra("rowId", appoinmentList.get(i));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return appoinmentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView nameTV, specilityTV, serialandTimeTV, dateTV;
        private ImageView doctorIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.nameTV);
            specilityTV = itemView.findViewById(R.id.specilityTV);
            serialandTimeTV = itemView.findViewById(R.id.serialandTimeTV);
            dateTV = itemView.findViewById(R.id.dateTV);
            doctorIV = itemView.findViewById(R.id.doctorIV);

        }
    }
}
