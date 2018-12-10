package com.nurul.medicareplus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nurul.medicareplus.MedicarePlus;
import com.nurul.medicareplus.R;
import com.nurul.medicareplus.adapter.MedicineAdapter;
import com.nurul.medicareplus.adapter.MedicineDialogeAapter;
import com.nurul.medicareplus.adapter.PatientMedicineAdapter;
import com.nurul.medicareplus.pojos.MedicineTimer;

import java.util.ArrayList;

public class PatientSingleMedicineTimer extends AppCompatActivity implements MedicineDialogeAapter.MedicineRowListner {

    private static final String TAG = PatientSingleMedicineTimer.class.getSimpleName();

    private TextView medicineNameTV, medicineTimeTV;
    private RecyclerView allmedicineRV, recyclerView;
    private ImageButton addIB;
    private Button okButton;
    private LinearLayout medicinShowLayout;

    private RecyclerView.Adapter adapter;
    private ArrayList<String> medicineRows;
    private ArrayList<String> list;
    boolean sh = true;

    private String timerId;
    private MedicineTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_single_medicine_timer);

        medicineNameTV = findViewById(R.id.medicineNameTV);
        medicineTimeTV = findViewById(R.id.medicineTimeTV);
        allmedicineRV = findViewById(R.id.allmedicineRV);
        addIB = findViewById(R.id.addIB);
        okButton = findViewById(R.id.okButton);

        medicinShowLayout = findViewById(R.id.medicinShowLayout);
        recyclerView = findViewById(R.id.medicineRV);

        Intent intent = getIntent();

        timerId = (String) intent.getSerializableExtra("medicineTimer");

        MedicarePlus.patientMedicineTimerReference(Patient_details.getAppoinmentId()).child(timerId).child("MDesc").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    try {

                        timer = dataSnapshot.getValue(MedicineTimer.class);

                        medicineNameTV.setText(timer.getName());
                        medicineTimeTV.setText(timer.getTimeString());

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (medicinShowLayout.getVisibility() == View.VISIBLE){
                    medicinShowLayout.setVisibility(View.GONE);
                    okButton.setVisibility(View.GONE);
                    addIB.setVisibility(View.VISIBLE);
                    allmedicineRV.setVisibility(View.VISIBLE);
                }

            }
        });

        MedicarePlus.patientMedicineTimerReference(Patient_details.getAppoinmentId()).child(timerId).child("MList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    list = new ArrayList<String>();
                    for (DataSnapshot d : dataSnapshot.getChildren()){
                        String s = d.getValue(String.class);
                        list.add(s);
                    }
                    adapter = new PatientMedicineAdapter(PatientSingleMedicineTimer.this, list);
                    allmedicineRV.setAdapter(adapter);
                    if (list.size() == 0){
                        allmedicineRV.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Remove item from backing list here

                String rw = list.get(viewHolder.getAdapterPosition());

                if (swipeDir == ItemTouchHelper.LEFT){

                    MedicarePlus.patientMedicineTimerReference(Patient_details.getAppoinmentId()).child(timer.getRowId()).child("MList").child(rw).removeValue();

                    viewHolder.getAdapterPosition();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(PatientSingleMedicineTimer.this, "Delated", Toast.LENGTH_SHORT).show();
                }

                if (swipeDir == ItemTouchHelper.RIGHT){
                    adapter.notifyDataSetChanged();
                }

            }
        });
        itemTouchHelper.attachToRecyclerView(allmedicineRV);


        addIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MedicarePlus.patientMedicineReference(Patient_details.getAppoinmentId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null){
                            medicineRows = new ArrayList<String>();
                            for (DataSnapshot d : dataSnapshot.getChildren()){

                                String rowId = d.getKey();
                                medicineRows.add(rowId);
                            }
                            adapter = new MedicineDialogeAapter(PatientSingleMedicineTimer.this, medicineRows);
                            recyclerView.setAdapter(adapter);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                if (medicinShowLayout.getVisibility() == View.GONE){
                    medicinShowLayout.setVisibility(View.VISIBLE);
                }
                okButton.setVisibility(View.VISIBLE);
                allmedicineRV.setVisibility(View.GONE);
                addIB.setVisibility(View.GONE);

            }
        });


    }

    @Override
    public void medicineRowId(String rowId) {
        MedicarePlus.patientMedicineTimerReference(Patient_details.getAppoinmentId()).child(timer.getRowId()).child("MList").child(rowId).setValue(rowId);
        adapter.notifyDataSetChanged();
    }
}
