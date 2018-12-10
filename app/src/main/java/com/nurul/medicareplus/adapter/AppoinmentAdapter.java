package com.nurul.medicareplus.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nurul.medicareplus.MedicarePlus;
import com.nurul.medicareplus.R;
import com.nurul.medicareplus.REG_MODEL.PatientSignupProfile;
import com.nurul.medicareplus.activity.Patient_details;
import com.nurul.medicareplus.pojos.AppoinmentedProfile;

import java.util.ArrayList;

public class AppoinmentAdapter extends RecyclerView.Adapter<AppoinmentAdapter.ViewHolder> {

    private static final String TAG = AppoinmentAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<String> rowList = new ArrayList<String>();

    public AppoinmentAdapter(Context context, ArrayList<String> rowList) {
        this.context = context;
        this.rowList = rowList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.appoinmented_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        MedicarePlus.appoinmentedStore().child(rowList.get(i)).child("AppoinmetDes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    AppoinmentedProfile profile = dataSnapshot.getValue(AppoinmentedProfile.class);

                        try {
                            viewHolder.serialNoTV.setText("Serial : "+profile.getAppoinmentSerial());
                        }catch (Exception e){
                            Log.d(TAG, "onDataChange: "+e.getMessage());
                        }
                       try {
                           viewHolder.dateTV.setText("Date : "+profile.getDate());
                        }catch (Exception e){
                            Log.d(TAG, "onDataChange: "+e.getMessage());
                        }
                       try {
                           viewHolder.timeTV.setText("Time : "+profile.getTime());
                        }catch (Exception e){
                            Log.d(TAG, "onDataChange: "+e.getMessage());
                        }

                        MedicarePlus.getUserIDdatabaseReference(profile.getAppoinmentId()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot != null){
                                    PatientSignupProfile patientSignupProfile = dataSnapshot.getValue(PatientSignupProfile.class);

                                    try {
                                        viewHolder.phoneTV.setText(patientSignupProfile.getPhone());
                                    }catch (Exception e){
                                        Log.d(TAG, "onDataChange: "+e.getMessage());
                                    }
                                    try {
                                        viewHolder.phoneTV.setText(patientSignupProfile.getPhone());
                                    }catch (Exception e){
                                        Log.d(TAG, "onDataChange: "+e.getMessage());
                                    }
                                    try {
                                        viewHolder.nameTV.setText("Name : "+patientSignupProfile.getName());
                                    }catch (Exception e){
                                        Log.d(TAG, "onDataChange: "+e.getMessage());
                                    }
                                    try {
                                        viewHolder.weightTV.setText("Weight : "+patientSignupProfile.getWeight());
                                    }catch (Exception e){
                                        Log.d(TAG, "onDataChange: "+e.getMessage());
                                    }
                                    try {
                                        viewHolder.ageTV.setText("Age : "+patientSignupProfile.getAge());
                                    }catch (Exception e){
                                        Log.d(TAG, "onDataChange: "+e.getMessage());
                                    }

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Patient_details.class);
                intent.putExtra("patientId", rowList.get(i));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rowList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView nameTV, ageTV, weightTV, phoneTV, serialNoTV, timeTV, dateTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            timeTV = itemView.findViewById(R.id.timeTV);
            serialNoTV = itemView.findViewById(R.id.serialNoTV);
            phoneTV = itemView.findViewById(R.id.phoneTV);
            weightTV = itemView.findViewById(R.id.weightTV);
            ageTV = itemView.findViewById(R.id.ageTV);
            nameTV = itemView.findViewById(R.id.nameTV);
            dateTV = itemView.findViewById(R.id.dateTV);

        }
    }
}
