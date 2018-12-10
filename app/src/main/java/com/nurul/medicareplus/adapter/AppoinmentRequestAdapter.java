package com.nurul.medicareplus.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nurul.medicareplus.MedicarePlus;
import com.nurul.medicareplus.R;
import com.nurul.medicareplus.REG_MODEL.PatientSignupProfile;
import com.nurul.medicareplus.pojos.AppoinmentedProfile;

import java.util.ArrayList;

public class AppoinmentRequestAdapter extends RecyclerView.Adapter<AppoinmentRequestAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> requestIdList = new ArrayList<String>();
    PatientSignupProfile profile;

    public AppoinmentRequestAdapter(Context context, ArrayList<String> requestIdList) {
        this.context = context;
        this.requestIdList = requestIdList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.appoinment_request_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder,final int i) {

        MedicarePlus.getUserIDdatabaseReference(requestIdList.get(i)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                     profile = dataSnapshot.getValue(PatientSignupProfile.class);
                    try {
                        viewHolder.patientNameTV.setText(profile.getName());
                        viewHolder.addressTV.setText(profile.getAddress());
                        viewHolder.agetWeightTV.setText("Age : "+profile.getAge()+"    Weight : "+profile.getWeight());
                        viewHolder.phoneTV.setText(profile.getPhone());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        viewHolder.confirmBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.confirmBT.setText("Confirmed");

                LayoutInflater inflater = LayoutInflater.from(context);
                View alerView = inflater.inflate(R.layout.appoinemnt_confirm_dialoge, null);

                final DatePicker datePicker = alerView.findViewById(R.id.patientAppoinemntDateDP);
                final TimePicker timePicker = alerView.findViewById(R.id.appoinmentTimePicker);
                final EditText serial = alerView.findViewById(R.id.serialNoET);

                AlertDialog.Builder dialog = new AlertDialog.Builder(context);

                dialog.setView(alerView).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(TextUtils.isEmpty(serial.getText())){
                            Toast.makeText(context, "Serial Will never empty", Toast.LENGTH_SHORT).show();
                            return;
                        }else {

                            AppoinmentedProfile appoinmentedProfile = new AppoinmentedProfile();

                            appoinmentedProfile.setAppoinmentId(requestIdList.get(i));
                            appoinmentedProfile.setDay(datePicker.getDayOfMonth());
                            appoinmentedProfile.setMonth(datePicker.getMonth());
                            appoinmentedProfile.setYear(datePicker.getYear());
                            appoinmentedProfile.setHour(timePicker.getHour());
                            appoinmentedProfile.setMinute(timePicker.getMinute());
                            appoinmentedProfile.setAppoinmentSerial(Integer.parseInt(serial.getText().toString()));


                            MedicarePlus.appoinmentedStore().child(requestIdList.get(i)).child("AppoinmetDes").setValue(appoinmentedProfile)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();

                                                MedicarePlus.getAppoinmentReq().child(requestIdList.get(i)).removeValue();
                                                notifyDataSetChanged();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            });

                            appoinmentedProfile.setAppoinmentId(MedicarePlus.getUserId());

                            MedicarePlus.confirmRequest(requestIdList.get(i)).child(MedicarePlus.getUserId()).child("AppoinmentDes").setValue(appoinmentedProfile);
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
                    }
                }).show();

            }
        });

        viewHolder.deleteBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MedicarePlus.getAppoinmentReq().child(requestIdList.get(i)).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                            notifyDataSetChanged();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Not Deleted", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return requestIdList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView patientNameTV, agetWeightTV, phoneTV, addressTV;
        private Button confirmBT;
        private ImageButton deleteBT;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            patientNameTV = itemView.findViewById(R.id.patientNameTV);
            agetWeightTV = itemView.findViewById(R.id.agetWeightTV);
            phoneTV = itemView.findViewById(R.id.phoneTV);
            addressTV = itemView.findViewById(R.id.addressTV);
            confirmBT = itemView.findViewById(R.id.confirmBT);
            deleteBT = itemView.findViewById(R.id.deleteBT);
        }
    }
}
