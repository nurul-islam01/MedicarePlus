package com.nurul.medicareplus.Patient_Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nurul.medicareplus.MedicarePlus;
import com.nurul.medicareplus.R;
import com.nurul.medicareplus.activity.Patient_details;
import com.nurul.medicareplus.adapter.MedicineTimerAdapter;
import com.nurul.medicareplus.adapter.PatientMedicineTimerAdapter;

import java.util.ArrayList;

public class Patient_Medicine_Timer extends Fragment {

    private Context context;
    private RecyclerView medicineRV;
    private FloatingActionButton alermfb;
    private ArrayList<String> medicineTimerList = new ArrayList<String>();
    private RecyclerView.Adapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_patient__medicine__timer, container, false);

        medicineRV = v.findViewById(R.id.medicineRV);
        alermfb = v.findViewById(R.id.fab);

        alermfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater dialogInflater = getActivity().getLayoutInflater();
                View dialog = dialogInflater.inflate(R.layout.medicine_timer_dialoge, null);

                final EditText alertTitle = dialog.findViewById(R.id.alertTitle);
                final TimePicker alermTimePicker = dialog.findViewById(R.id.alermTimePicker);

                AlertDialog.Builder alertMedicine = new AlertDialog.Builder(getActivity());

                alertMedicine.setView(dialog).setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        com.nurul.medicareplus.pojos.MedicineTimer timer = new com.nurul.medicareplus.pojos.MedicineTimer();
                        timer.setName(alertTitle.getText().toString());
                        timer.setHour(alermTimePicker.getHour());
                        timer.setMinute(alermTimePicker.getMinute());

                        String pushkey =  MedicarePlus.patientMedicineTimerReference(Patient_details.getAppoinmentId()).push().getKey();
                        timer.setRowId(pushkey);
                        MedicarePlus.patientMedicineTimerReference(Patient_details.getAppoinmentId()).child(pushkey).child("MDesc").setValue(timer);
                        adapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("Cancel", null).show();

            }
        });


        MedicarePlus.patientMedicineTimerReference(Patient_details.getAppoinmentId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                medicineTimerList = new ArrayList<String>();
                if (dataSnapshot != null){
                    for (DataSnapshot d : dataSnapshot.getChildren()){
                        String row = d.getKey();

                        medicineTimerList.add(row);

                    }
                    adapter = new PatientMedicineTimerAdapter(getActivity(), medicineTimerList);
                    medicineRV.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }


}
