package com.nurul.medicareplus.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nurul.medicareplus.MedicarePlus;
import com.nurul.medicareplus.R;
import com.nurul.medicareplus.activity.Patient_details;
import com.nurul.medicareplus.pojos.Medicine;
import com.nurul.medicareplus.pojos.MedicineTimer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MedicineDialogeAapter extends RecyclerView.Adapter<MedicineDialogeAapter.ViewHolder> {

    private Context context;
    private ArrayList<String> rows = new ArrayList<String>();
    private boolean isSelected = true;

    public MedicineDialogeAapter(Context context, ArrayList<String> rows) {
        this.context = context;
        this.rows = rows;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.medicine_raw_2_for_timer, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        final String rowId = rows.get(i);

        MedicarePlus.PatientMedicineDesc(rowId, Patient_details.getAppoinmentId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    Medicine medicine = dataSnapshot.getValue(Medicine.class);

                    try {
                        viewHolder.medicineNameTV.setText(medicine.getName());
                        viewHolder.timeDosTV.setText(medicine.getPerDos()+" pill "+ medicine.getDailyDos() + " times daily");
                        viewHolder.medicineStartTV.setText(medicine.getBeforeORafter());
                        if (medicine.getmImageUri() == null){
                            viewHolder.medicineIV.setImageResource(R.drawable.medicin_icon);
                        }else {
                            Picasso.get().load(medicine.getmImageUri())
                                    .placeholder(R.drawable.medicin_icon)
                                    .error(R.drawable.medicin_icon)
                                    .into(viewHolder.medicineIV);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        viewHolder.addOncCancelIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelected){
                    isSelected = false;
                    viewHolder.addOncCancelIB.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                    viewHolder.addOncCancelIB.setBackgroundColor(Color.RED);
                    ((MedicineRowListner)context).medicineRowId(rowId);
                    Toast.makeText(context, "Selected", Toast.LENGTH_SHORT).show();
                }else {
                    isSelected = true;
                    viewHolder.addOncCancelIB.setImageResource(android.R.drawable.ic_input_add);
                    viewHolder.addOncCancelIB.setBackgroundColor(Color.DKGRAY);
                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView medicineIV;
        private TextView medicineNameTV, timeDosTV, medicineStartTV;
        private ImageButton addOncCancelIB;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            medicineIV = itemView.findViewById(R.id.medicineIV);
            medicineNameTV = itemView.findViewById(R.id.medicineNameTV);
            timeDosTV = itemView.findViewById(R.id.timeDosTV);
            medicineStartTV = itemView.findViewById(R.id.medicineStartTV);
            addOncCancelIB = itemView.findViewById(R.id.addOrCancelIB);
        }
    }

    public interface MedicineRowListner{
        public void medicineRowId(String rowId);
    }
}
