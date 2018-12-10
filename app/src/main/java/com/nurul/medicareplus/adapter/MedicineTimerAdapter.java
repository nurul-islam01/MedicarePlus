package com.nurul.medicareplus.adapter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nurul.medicareplus.MedicarePlus;
import com.nurul.medicareplus.R;
import com.nurul.medicareplus.activity.SingleMedicineTimer;
import com.nurul.medicareplus.nessary_classes.AlermReciver;
import com.nurul.medicareplus.pojos.MedicineTimer;

import java.util.ArrayList;

public class MedicineTimerAdapter extends RecyclerView.Adapter<MedicineTimerAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private ArrayList<String> medicineTimerList = new ArrayList<String>();
    MedicineTimer timer;

    public MedicineTimerAdapter(Context context, ArrayList<String> medicineTimerList) {
        this.context = context;
        this.medicineTimerList = medicineTimerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.medicine_timer_raw, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        final String row = medicineTimerList.get(i);
        timer = new MedicineTimer();

        MedicarePlus.getMedicineTimerReference().child(row).child("MDesc").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    try {
                        timer = dataSnapshot.getValue(MedicineTimer.class);
                        viewHolder.alermNameIV.setText(timer.getName());
                        viewHolder.alermTimeTV.setText(timer.getTimeString());
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        viewHolder.textViewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(context, viewHolder.textViewOptions);
                menu.inflate(R.menu.edit_or_delete);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.edit:

                                LayoutInflater dialogInflater = LayoutInflater.from(context);
                                View dialog = dialogInflater.inflate(R.layout.medicine_timer_dialoge, null);

                                final EditText alertTitle = dialog.findViewById(R.id.alertTitle);
                                final TimePicker alermTimePicker = dialog.findViewById(R.id.alermTimePicker);
                                alertTitle.setText(timer.getName());
                                alermTimePicker.setHour(timer.getHour());
                                alermTimePicker.setMinute(timer.getMinute());

                                AlertDialog.Builder alertMedicine = new AlertDialog.Builder(context);

                                alertMedicine.setView(dialog).setPositiveButton("Set", new DialogInterface.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.M)
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        MedicineTimer newTimer = new MedicineTimer();
                                        newTimer.setName(alertTitle.getText().toString());
                                        newTimer.setHour(alermTimePicker.getHour());
                                        newTimer.setMinute(alermTimePicker.getMinute());
                                        newTimer.setRowId(timer.getRowId());

                                        MedicarePlus.getMedicineTimerReference().child(timer.getRowId()).setValue(newTimer);
                                    }
                                }).setNegativeButton("Cancel", null).show();

                                break;
                            case R.id.delete:
                                MedicarePlus.removeMedicineTimer(timer.getRowId()).removeValue();
                                break;
                            default:
                                    return false;
                        }
                        return false;
                    }
                });

                menu.show();
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MedicarePlus.getMedicineTimerReference().child(row).child("MDesc").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null){
                            try {
                                timer = dataSnapshot.getValue(MedicineTimer.class);
                                Intent intent = new Intent(context, SingleMedicineTimer.class);

                                intent.putExtra("medicineTimer", timer);

                                context.startActivity(intent);
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }
        });

    }


    @Override
    public int getItemCount() {
        return medicineTimerList.size();
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();

        Object object = getItemId(position);

        timer = (MedicineTimer) object;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView alermIconIV;
        private TextView alermTimeTV, alermNameIV, textViewOptions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            alermIconIV = itemView.findViewById(R.id.alermIconIV);
            alermTimeTV = itemView.findViewById(R.id.alermTimeTV);
            alermNameIV = itemView.findViewById(R.id.alermNameIV);
            textViewOptions = itemView.findViewById(R.id.textViewOptions);

        }
    }
}
