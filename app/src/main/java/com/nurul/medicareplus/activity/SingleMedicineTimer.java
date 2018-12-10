package com.nurul.medicareplus.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.kyleduo.switchbutton.SwitchButton;
import com.nurul.medicareplus.MedicarePlus;
import com.nurul.medicareplus.R;
import com.nurul.medicareplus.adapter.MedicineAdapter;
import com.nurul.medicareplus.adapter.MedicineDialogeAapter;
import com.nurul.medicareplus.adapter.MedicineTimerAdapter;
import com.nurul.medicareplus.nessary_classes.AlermReciver;
import com.nurul.medicareplus.pojos.MedicineTimer;

import java.util.ArrayList;
import java.util.Calendar;

public class SingleMedicineTimer extends AppCompatActivity implements MedicineDialogeAapter.MedicineRowListner {

    private static final String TAG = SingleMedicineTimer.class.getSimpleName();

    private TextView medicineNameTV, medicineTimeTV;
    private RecyclerView allmedicineRV, recyclerView;
    private ImageButton addIB;
    private SwitchButton alermSwitch;
    private LinearLayout medicinShowLayout;

    private RecyclerView.Adapter adapter;
    private ArrayList<String> list;
    boolean sh = true;

    private String timerId;
    private MedicineTimer timer;

    private AlarmManager alerm_Manager;
    private PendingIntent pendingIntent;
    private Intent mIntent;
    private LayoutInflater layoutInflater;
    private Calendar calendar;
    private final int mRequestCode = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_medicine_timer);

        calendar = Calendar.getInstance();
        alerm_Manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        medicineNameTV = findViewById(R.id.medicineNameTV);
        medicineTimeTV = findViewById(R.id.medicineTimeTV);
        allmedicineRV = findViewById(R.id.allmedicineRV);
        addIB = findViewById(R.id.addIB);
        alermSwitch = findViewById(R.id.alermSwitch);

        medicinShowLayout = findViewById(R.id.medicinShowLayout);
        recyclerView = findViewById(R.id.medicineRV);


        Intent intent = getIntent();

        timer = (MedicineTimer) intent.getSerializableExtra("medicineTimer");
        timerId = timer.getRowId();

        try {
            medicineNameTV.setText(timer.getName());
        }catch (Exception e){
            e.printStackTrace();
        }try {
            medicineTimeTV.setText(timer.getTimeString());
        }catch (Exception e){
            e.printStackTrace();
        }try {
            alermSwitch.setChecked(timer.isOnOFF());
            if (timer.isOnOFF()){
                setAlerm();
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        MedicarePlus.getMedicineTimerReference().child(timerId).child("MList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    list = new ArrayList<String>();
                    for (DataSnapshot d : dataSnapshot.getChildren()){
                        String s = d.getValue(String.class);
                        list.add(s);
                    }
                    adapter = new MedicineAdapter(SingleMedicineTimer.this, list);
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


        alermSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    MedicarePlus.getMedicineTimerReference().child(timerId).child("MDesc").child("onOFF").setValue(true)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SingleMedicineTimer.this, "Alerm ON", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SingleMedicineTimer.this, "Alerm ON Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    MedicarePlus.getMedicineTimerReference().child(timerId).child("MDesc").child("onOFF").setValue(false)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SingleMedicineTimer.this, "Alerm OFF", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SingleMedicineTimer.this, "Alerm OFF Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        });

    }

    private void setAlerm(){
        calendar.set(Calendar.HOUR_OF_DAY, timer.getHour());
        calendar.set(Calendar.MINUTE, timer.getMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        mIntent = new Intent(this, AlermReciver.class);
        mIntent.putExtra("extra", "alerm_on");
        mIntent.putExtra("timer", timer);
        pendingIntent = PendingIntent.getBroadcast(this, mRequestCode, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alerm_Manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

    @Override
    public void medicineRowId(String rowId) {
        MedicarePlus.getMedicineTimerReference().child(timerId).child("MList").child(rowId).setValue(rowId);
        adapter.notifyDataSetChanged();
    }

}
