package com.nurul.medicareplus.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nurul.medicareplus.MedicarePlus;
import com.nurul.medicareplus.R;
import com.nurul.medicareplus.pojos.Medicine;
import com.nurul.medicareplus.pojos.RecentlyMedicine;

import java.util.HashMap;

public class SingleMedicine extends AppCompatActivity {

    private static final String TAG = SingleMedicine.class.getSimpleName();

    private TextView needTV ,medicineNameTV, timeDosTV, medicineStartTV, totalMedicineTV, medicineHaveTV, neetToaddTV, takenCardTV, medicineWorningTV;
    private ImageButton addBT, saveBT, cancelBT;
    private ImageView medicineIV;
    private EditText addET;
    private LinearLayout recentlyAddL, recentlyAddingL;

    private Medicine medicine;
    private boolean t = false, t2= false;
    private RecentlyMedicine recentlyMedicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_medicine);
        Intent intent = getIntent();
        try {
            medicine = (Medicine) intent.getSerializableExtra("medicine");
        }catch (Exception e){
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }



        medicineNameTV = findViewById(R.id.medicineNameTV);
        medicineHaveTV = findViewById(R.id.medicineHaveTV);
        timeDosTV = findViewById(R.id.timeDosTV);
        medicineStartTV = findViewById(R.id.medicineStartTV);
        totalMedicineTV = findViewById(R.id.totalMedicineTV);
        neetToaddTV = findViewById(R.id.neetToaddTV);
        takenCardTV = findViewById(R.id.takenCardTV);
        needTV = findViewById(R.id.needTV);
        medicineWorningTV = findViewById(R.id.medicineWorningTV);

        addET = findViewById(R.id.addET);

        recentlyAddL = findViewById(R.id.recentlyAddL);
        recentlyAddingL = findViewById(R.id.recentlyAddingL);

        addBT = findViewById(R.id.addBT);
        saveBT = findViewById(R.id.saveBT);
        cancelBT = findViewById(R.id.cancelBT);

        medicineIV = findViewById(R.id.medicineIV);


        try {
            medicineNameTV.setText(medicine.getName());
        }catch (Exception e){
            Log.d(TAG, "onDataChange: "+e.getMessage());
        }
        try {
            medicineStartTV.setText("Start Date : "+medicine.getDate());
        }catch (Exception e){
            Log.d(TAG, "onDataChange: "+e.getMessage());
        }
        try {
            timeDosTV.setText(medicine.getPerDos() +" pics, "+medicine.getDailyDos() +" times daily");
        }catch (Exception e){
            Log.d(TAG, "onDataChange: "+e.getMessage());
        }
        try {
            totalMedicineTV.setText("Total : " + medicine.getTotalPicsMedicine());
        }catch (Exception e){
            Log.d(TAG, "onDataChange: "+e.getMessage());
        }

        MedicarePlus.recentlyAddMedicineDesc(medicine.getRowId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    try {
                        recentlyMedicine = dataSnapshot.getValue(RecentlyMedicine.class);

                    }catch (Exception e){
                        Log.d(TAG, "onDataChange: "+e.getMessage());
                    }
                    try {
                        needTV.setText(" Taken : " + recentlyMedicine.getTaken());
                    }catch (Exception e){
                        Log.d(TAG, "onDataChange: "+e.getMessage());
                    }
                    try {
                        medicineHaveTV.setText("Your Have " + recentlyMedicine.getHave()+ " Pics Medicine");
                    }catch (Exception e){
                        Log.d(TAG, "onDataChange: "+ e.getMessage());
                    }
                    try {
                        neetToaddTV.setText( String.valueOf( medicine.getTotalPicsMedicine() - ( recentlyMedicine.getHave()+ recentlyMedicine.getTaken() ) ) );
                    }catch (Exception e){
                        Log.d(TAG, "onDataChangeneetToaddTV: "+ e.getMessage());
                    }
                    try {
                        int twoDays = medicine.getPerDos()*medicine.getDailyDos()*2;
                        if (recentlyMedicine.getTaken() + recentlyMedicine.getHave() + twoDays < medicine.getTotalPicsMedicine() && twoDays < recentlyMedicine.getHave()){

                            medicineWorningTV.setText(  medicine.getName() +" Will be finish After Two days"  );
                        }

                    }catch (Exception e){
                        Log.d(TAG, "onDataChangeneetToaddTV: "+ e.getMessage());
                    }

                }else if (dataSnapshot.getValue() == null){
                    recentlyMedicine = new RecentlyMedicine();
                    recentlyMedicine.setTotal(0);
                    recentlyMedicine.setHave(0);
                    recentlyMedicine.setTaken(0);
                    MedicarePlus.recentlyAddMedicineDesc(medicine.getRowId()).setValue(recentlyMedicine);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        takenCardTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SingleMedicine.this);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        t= true;
                        t2 = true;
                        takenMedicine();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SingleMedicine.this, "You are not taken medicine", Toast.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });

        addBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addBT.getVisibility() == View.VISIBLE){
                    recentlyAddL.setVisibility(View.GONE);
                    recentlyAddingL.setVisibility(View.VISIBLE);
                }else {
                    return;
                }
            }
        });

        saveBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recentlyAddingL.getVisibility() == View.VISIBLE){
                    t = true;
                    try {
                        final int recnt = Integer.parseInt(addET.getText().toString());

                        MedicarePlus.recentlyAddMedicineDesc(medicine.getRowId()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot != null){

                                    recentlyMedicine = dataSnapshot.getValue(RecentlyMedicine.class);
                                    if (recentlyMedicine.getHave() == 0 && t){
                                        MedicarePlus.recentlyAddMedicineDesc(medicine.getRowId()).child("have").setValue(recnt);
                                        t = false;
                                        recentlyAddL.setVisibility(View.VISIBLE);
                                        recentlyAddingL.setVisibility(View.GONE);
                                    }else if (recentlyMedicine.getHave() + recentlyMedicine.getTaken() + recnt <= medicine.getTotalPicsMedicine() && t){
                                        t = false;
                                        int hav = recentlyMedicine.getHave();
                                        hav = hav+recnt;

                                        MedicarePlus.recentlyAddMedicineDesc(medicine.getRowId()).child("have").setValue(hav);
                                        recentlyAddL.setVisibility(View.VISIBLE);
                                        recentlyAddingL.setVisibility(View.GONE);

                                    }else if (recentlyMedicine.getHave() + recentlyMedicine.getTaken()+recnt > medicine.getTotalPicsMedicine() && t){
                                        recentlyAddL.setVisibility(View.VISIBLE);
                                        recentlyAddingL.setVisibility(View.GONE);
                                        t = false;
                                        Toast.makeText(SingleMedicine.this, "Your Can't add more medicine", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }catch (Exception e){
                        Log.d(TAG, "onClick: "+e.getMessage());
                        Toast.makeText(SingleMedicine.this, "Not Added", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    return;
                }
            }
        });

        cancelBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recentlyAddingL.getVisibility() == View.VISIBLE){
                    recentlyAddL.setVisibility(View.VISIBLE);
                    recentlyAddingL.setVisibility(View.GONE);
                }else {
                    return;
                }
            }
        });

    }


    private void takenMedicine( ){
        MedicarePlus.recentlyAddMedicineDesc(medicine.getRowId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot != null){
                            recentlyMedicine = dataSnapshot.getValue(RecentlyMedicine.class);
                            if (recentlyMedicine.getTaken() < medicine.getTotalPicsMedicine() && t && recentlyMedicine.getHave() > 0){
                                t = false;
                                int h = recentlyMedicine.getTaken();
                                int h2 = recentlyMedicine.getHave();
                                h = h + medicine.getPerDos();
                                h2 = h2 - medicine.getPerDos();
                                if (h >= medicine.getTotalPicsMedicine()){
                                    h = h - medicine.getPerDos();
                                    Toast.makeText(SingleMedicine.this, "Medicine Complete", Toast.LENGTH_SHORT).show();
                                }else if (h <= medicine.getTotalPicsMedicine() && h2 >= 0){
                                    t = false;
                                    recentlyMedicine.setTaken(h);
                                    recentlyMedicine.setHave(h2);
                                    MedicarePlus.recentlyAddMedicineDesc(medicine.getRowId()).setValue(recentlyMedicine);
                                }
                            }else if (recentlyMedicine.getHave() <= 0 && t){
                                t = false;
                                Toast.makeText(SingleMedicine.this, "You have not medicine", Toast.LENGTH_SHORT).show();
                            }


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}
