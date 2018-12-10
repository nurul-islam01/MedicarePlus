package com.nurul.medicareplus.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import com.nurul.medicareplus.activity.RawActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllDoctorAdapter extends RecyclerView.Adapter<AllDoctorAdapter.DoctorViewHolder> {

    private ArrayList<String> doctorList;
    private Context context;

    public AllDoctorAdapter(ArrayList<String> doctorList, Context context) {
        this.doctorList = doctorList;
        this.context = context;
    }


    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.doctor_list_row, viewGroup, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DoctorViewHolder viewHolder, int i) {


        final String profileID = doctorList.get(i);

        MedicarePlus.getUserIDdatabaseReference(profileID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    try {
                        DoctorSignupProfile profile = dataSnapshot.getValue(DoctorSignupProfile.class);
                        viewHolder.nameTV.setText(profile.getName());
                        viewHolder.chemberTV.setText(profile.getChember());
                        viewHolder.designationTV.setText(profile.getDegination());
                        viewHolder.specilityTV.setText(profile.getSepecility());
                        Picasso.get().load(profile.getPic_uri()).placeholder(R.drawable.doctor_image_icon).error(R.drawable.doctor_image_icon).into(viewHolder.doctorIV);
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
                Intent intent = new Intent(viewHolder.itemView.getContext(), RawActivity.class);
                intent.putExtra("fragment", "doctorProfile");
                intent.putExtra("doctorId", profileID);
                viewHolder.itemView.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public class DoctorViewHolder extends RecyclerView.ViewHolder {

        private ImageView doctorIV;
        private TextView nameTV, designationTV, specilityTV, chemberTV;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.nameTV);
            designationTV = itemView.findViewById(R.id.designationTV);
            specilityTV = itemView.findViewById(R.id.specilityTV);
            chemberTV = itemView.findViewById(R.id.chemberTV);
            doctorIV = itemView.findViewById(R.id.doctorIV);

        }
    }
}
